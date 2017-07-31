package com.limin.blog.controller;

import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import com.limin.blog.dto.*;
import com.limin.blog.entity.*;
import com.limin.blog.util.JedisAdapter;
import com.limin.blog.util.RedisKeyUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.limin.blog.service.ArticleService;
import com.limin.blog.service.CategoryService;
import com.limin.blog.service.CommentService;
import com.limin.blog.service.UserService;
import com.limin.blog.util.PageResult;

import static com.limin.blog.util.RedisKeyUtil.getSupportKey;

@Controller
@RequestMapping("/article")
public class ArticleController {

    @Resource
    private ArticleService articleService;

    @Resource
    private UserService userService;

    @Resource
    private CategoryService categoryService;

    @Resource
    private CommentService commentService;


    // 文章详细信息，包括评论
    @RequestMapping("/detail/{aid}")
    public ModelAndView articleDetail(@PathVariable(name = "aid") long aid) {

        ModelAndView mv = new ModelAndView("/article/detail");

        Article article = articleService.findById(aid);
        if (article == null) {
            mv.addObject("info","没有数据");
            mv.setViewName("/info");
            return mv;
        }
        mv.addObject("article", article);

        Long viewCount = articleService.incrViewCount(aid);

        mv.addObject("viewCount", viewCount);
        mv.addObject("commentCount", articleService.getCommentCount(aid));
        mv.addObject("support", articleService.getSupport(aid));
        mv.addObject("against", articleService.getAgainst(aid));

        List<Category> articleCategories = categoryService.findByArticleId(aid);
        mv.addObject("articleCategories", articleCategories);

        User user = userService.findById(article.getUserId());
        mv.addObject("user", user);

        CategoryExample example = new CategoryExample();
        example.createCriteria().andUserIdEqualTo(user.getId());
        List<Category> categoryList = categoryService.findByExample(example);
        List<ValueObject> categoryVos = new ArrayList<>();
        for (Category category : categoryList) {
            categoryVos.add(new ValueObject().set("category", category)
                    .set("articleCount", categoryService.findArticleCountById(category.getId())));
        }
        mv.addObject("categoryVos", categoryVos);

        CommentExample commentExample = new CommentExample();
        commentExample.setOrderByClause("release_date desc");
        commentExample.createCriteria().andArticleIdEqualTo(aid);
        List<Comment> comments = commentService.findByExampelWithBlob(commentExample);
        List<ValueObject> commentAndUsers = new ArrayList<>();
        for (int i = 0; i < comments.size(); i++) {
            Comment comment = comments.get(i);
            User commentUser = userService.findById(comment.getUserId());
            commentAndUsers.add(new ValueObject().set("comment", comment).set("user", commentUser));
        }
        commentAndUsers = formatCommentAndUser(commentAndUsers);
        mv.addObject("commentAndUsers", commentAndUsers);

        return mv;
    }

    private List<ValueObject> formatCommentAndUser(List<ValueObject> vos) {

        //找出所有父级评论，并在评论列表中删除父级评论
        ArrayList<ValueObject> rootList = new ArrayList<ValueObject>();
        ListIterator<ValueObject> voIterator = vos.listIterator();
        while (voIterator.hasNext()) {
            ValueObject vo = voIterator.next();
            Comment comment = (Comment) vo.get("comment");
            if (comment.getParentId() == null) {
                rootList.add(0, vo);
                voIterator.remove();
            }
        }

        //利用深度遍历找到每个父级评论的子评论和子评论的子评论
        ArrayList<ValueObject> resultList = new ArrayList<ValueObject>();
        ListIterator<ValueObject> rootIterator = rootList.listIterator();
        int build = 0;
        while (rootIterator.hasNext()) {
            ValueObject root = rootIterator.next();
            Comment comment = (Comment) root.get("comment");
            CommentVo commentVo = new CommentVo(comment);
            root.set("comment", commentVo);
            // 父级评论 层中深度为0
            commentVo.setGrade(0);
            Stack<ValueObject> stack = new Stack<ValueObject>();
            stack.push(root);
            ArrayList<ValueObject> list = new ArrayList<ValueObject>();
            // 找出父级评论的子评论和子评论的子评论
            while (!stack.empty()) {
                ValueObject pop = stack.pop();
                list.add(pop);
                CommentVo popComment = (CommentVo) pop.get("comment");
                User popUse = (User) pop.get("user");
                ListIterator<ValueObject> cIterator = vos.listIterator(vos.size());
                while (cIterator.hasPrevious()) {
                    ValueObject next = cIterator.previous();
                    Comment nextComment = (Comment) next.get("comment");
                    if (popComment.getId().equals(nextComment.getParentId())) {
                        CommentVo nextCommentVo = new CommentVo(nextComment);
                        nextCommentVo.setGrade(popComment.getGrade() + 1);
                        nextCommentVo.setRefUsername(popUse.getUsername());
                        next.set("comment", nextCommentVo);
                        stack.push(next);
                        cIterator.remove();
                    }
                }
            }
            // 楼层+1
            commentVo.setBuild(++build);
            resultList.addAll(0, list);
        }
        return resultList;
    }

    // 用户文章分页
    @RequestMapping(value = {"/list/{uid}", "/list/{uid}/{categoryId}"})
    public ModelAndView articleList(
            @PathVariable(name = "uid") Long uid,
            @PathVariable(name ="categoryId", required = false) Long categoryId,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {

        ModelAndView mv = new ModelAndView("/article/list");
        mv.addObject("categoryId", categoryId);

        User user = userService.findById(uid);
        mv.addObject("user", user);

        CategoryExample example = new CategoryExample();
        example.createCriteria().andUserIdEqualTo(user.getId());
        List<Category> categoryList = categoryService.findByExample(example);
        List<ValueObject> categoryVos = new ArrayList<>();
        for (Category category : categoryList) {
            categoryVos.add(new ValueObject().set("category", category)
                    .set("articleCount", categoryService.findArticleCountById(category.getId())));
        }
        mv.addObject("categoryVos", categoryVos);

        ArticleExample articleExample = new ArticleExample();
        if (categoryId != null) {
            Set<String> articleIdStrings = categoryService.findArticleIdsById(categoryId);
            if (articleIdStrings.size() == 0) {
                return mv;
            }
            List<Long> articleIds = new ArrayList<>();
            for (String articleId  : articleIdStrings) {
                articleIds.add(Long.valueOf(articleId));
            }
            articleExample.createCriteria().andIdIn(articleIds);
        } else {
            articleExample.createCriteria().andUserIdEqualTo(uid);
        }
        ValueObject vo = articleService.findPageByExample(articleExample, page, size);

        List<Article> articles = (List<Article>) vo.get("articles");
        for (int i = 0; i < articles.size(); i++) {
            Article article = articles.get(i);
            article.setContent(articleService.excepeHtml(article.getContent().trim()));
            article.setContent(article.getContent().substring(0, article.getContent().length() < 100 ? article.getContent().length() : 100));
        }
        List<ValueObject> articleVos = new ArrayList<>();
        for (Article article : articles) {
            Long viewCount = articleService.getViewCount(article.getId());
            Long commentCount = articleService.getCommentCount(article.getId());
            articleVos.add(new ValueObject().set("article", article).set("viewCount", viewCount).set("commentCount", commentCount));
        }
        Long totalRow = (Long) vo.get("totalRow");
        PageResult<ValueObject> pageResult = new PageResult<>(page, size, articleVos, totalRow);
        mv.addObject("pageResult", pageResult);

        return mv;
    }

    @RequestMapping("/support")
    @ResponseBody
    public String support(long articleId) {
        Article article = articleService.findById(articleId);
        if (article != null) {
            Long support = articleService.support(articleId);
            return String.valueOf(support);
        }
        return "fail";
    }
    @RequestMapping("/unsupport")
    @ResponseBody
    public String unsupport(long articleId) {
        Article article = articleService.findById(articleId);
        if (article != null) {
            Long support = articleService.unsupport(articleId);
            return String.valueOf(support);
        }
        return "fail";
    }

    @RequestMapping("/against")
    @ResponseBody
    public String against(long articleId) {
        Article article = articleService.findById(articleId);
        if (article != null) {
            Long against = articleService.against(articleId);
            return String.valueOf(against);
        }
        return "fail";
    }
    @RequestMapping("/unagainst")
    @ResponseBody
    public String unagainst(long articleId) {
        Article article = articleService.findById(articleId);
        if (article != null) {
            Long against = articleService.unagainst(articleId);
            return String.valueOf(against);
        }
        return "fail";
    }




    // back
    @RequestMapping(value = {"/manage/list","/manage/list/{cId}"})
    public ModelAndView list(
            @PathVariable(value = "cId", required = false) Long categoryId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            HttpSession session) {
        ModelAndView mv = new ModelAndView("manage/article/list");
        mv.addObject("option", "articleManage");
        mv.addObject("categoryId", categoryId);

        User user = (User) session.getAttribute("loginUser");
        CategoryExample categoryExample = new CategoryExample();
        categoryExample.createCriteria().andUserIdEqualTo(user.getId());
        List<Category> list = categoryService.findByExample(categoryExample);
        mv.addObject("categoryList", list);


        ArticleExample example = new ArticleExample();
        if (categoryId == null) {
            example.createCriteria().andUserIdEqualTo(user.getId());
        } else {
            Set<String> articleIdStrings = categoryService.findArticleIdsById(categoryId);
            if (articleIdStrings.size() == 0) {
                return mv;
            }
            List<Long> articleIds = new ArrayList<>();
            for (String articleId  : articleIdStrings) {
                articleIds.add(Long.valueOf(articleId));
            }
            example.createCriteria().andIdIn(articleIds);
        }
        ValueObject vo = articleService.findPageByExample(example, page, size);
        List<Article> articles = (List<Article>) vo.get("articles");
        List<ValueObject> articleVos = new ArrayList<>();
        for (Article article : articles) {
            articleVos.add(new ValueObject().set("article", article)
                    .set("viewCount",articleService.getViewCount(article.getId()))
                    .set("commentCount", articleService.getCommentCount(article.getId())));
        }
        long totalRow = (long) vo.get("totalRow");
        PageResult<ValueObject> pageResult = new PageResult<>(page, size, articleVos, totalRow);
        mv.addObject("pageResult", pageResult);
        return mv;
    }

    @RequestMapping("/manage/delete")
    @ResponseBody
    public String delete(Long articleId) {
        try {
            articleService.deleteById(articleId);
            return "success";
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
        }
    }


    @RequestMapping("/manage/edit")
    public ModelAndView edit(
            @RequestParam("articleId") Long articleId,
            HttpSession session) {
        ModelAndView mv = new ModelAndView("manage/article/publish");
        mv.addObject("option", "articleCreate");
        mv.addObject("publish", "edit");

        Article article = articleService.findById(articleId);
        List<Category> articleCategories = categoryService.findByArticleId(articleId);
        ArticleVo articleVo = new ArticleVo(article);
        for (int i = 0; i < articleCategories.size(); i++) {
            articleVo.getCategoryIds().add(String.valueOf(articleCategories.get(i).getId()));
        }
        mv.addObject("articleVo", articleVo);


        User user = (User) session.getAttribute("loginUser");
        CategoryExample categoryExample = new CategoryExample();
        categoryExample.createCriteria().andUserIdEqualTo(user.getId());
        List<Category> categories = categoryService.findByExample(categoryExample);
        mv.addObject("categories", categories);
        return mv;
    }


    @RequestMapping("/manage/edithandler")
    public String edithandler(ArticleVo articleVo, HttpSession session) {

        User user = (User) session.getAttribute("loginUser");
        articleVo.setUserId(user.getId());
        articleVo.setUsername(user.getUsername());
        articleService.update((Article)articleVo, articleVo.getCategoryIds());
        return "redirect:/article/detail/" + articleVo.getId();
    }



    @RequestMapping("/manage/create")
    public ModelAndView create(HttpSession session) {
        ModelAndView mv = new ModelAndView("manage/article/publish");
        mv.addObject("option", "articleCreate");
        mv.addObject("publish", "create");
        mv.addObject("articleVo", new ArticleVo());

        User user = (User) session.getAttribute("loginUser");
        CategoryExample categoryExample = new CategoryExample();
        categoryExample.createCriteria().andUserIdEqualTo(user.getId());
        List<Category> categories = categoryService.findByExample(categoryExample);
        mv.addObject("categories", categories);
        return mv;
    }

    @RequestMapping("/manage/createhandler")
    public String createhandler(ArticleVo articleVo, HttpSession session) {

        User user = (User) session.getAttribute("loginUser");
        articleVo.setUserId(user.getId());

        articleService.add((Article) articleVo, articleVo.getCategoryIds());

        return "redirect:/article/detail/" + articleVo.getId();
    }
}
