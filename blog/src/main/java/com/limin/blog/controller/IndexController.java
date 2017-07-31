package com.limin.blog.controller;

import com.limin.blog.dto.ArticleVo;
import com.limin.blog.dto.ValueObject;
import com.limin.blog.entity.Article;
import com.limin.blog.entity.ArticleExample;
import com.limin.blog.entity.User;
import com.limin.blog.service.ArticleService;
import com.limin.blog.service.UserService;
import com.limin.blog.util.ArticleLucene;
import com.limin.blog.util.PageResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.time.Instant;
import java.util.*;

@Controller
public class IndexController {

    @Resource
    private ArticleService articleService;

    @Resource
    private UserService userService;

    @Resource
    private ArticleLucene articleLucene;

    @RequestMapping(value = {"/search"})
    public ModelAndView search(
            @RequestParam(value = "query") String query,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        ModelAndView mv = new ModelAndView("search");
        mv.addObject("query", query);
        ValueObject vo = articleLucene.searchIndex(query, page, size);
        List<ArticleVo> articles = null;
        if (vo.get("articles") != null) {
            articles = (List<ArticleVo>) vo.get("articles");
        }
        int totalHits = 0;
        if (vo.get("totalHits") != null) {
            totalHits = (int) vo.get("totalHits");
        }
        PageResult<ArticleVo> pageResult = new PageResult<>(page, size, articles, totalHits);
        mv.addObject("pageResult", pageResult);

        return mv;
    }


    @RequestMapping(value = {"/", "/sys/{option}/{category}"})
    public ModelAndView index(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @PathVariable(value = "option", required = false) String option,
            @PathVariable(value = "category", required = false) String category) {

        ModelAndView mv = new ModelAndView("index");//首页

        ArticleExample articleExample = new ArticleExample();

        ValueObject objs = null;
        if (option == null && category == null) {
            Date date = Date.from(Instant.now().minusSeconds(3600 * 24 * 30));// 30天前的文章
            articleExample.createCriteria().andReleaseDateGreaterThan(date);
            articleExample.setOrderByClause("release_date desc");
            objs = articleService.findPageByExample(articleExample, page, size);
            if ((objs.get("articles") == null)) {
                return mv;
            }
        } else if (option != null && category != null) { //文章系统分类
            mv.addObject("option", option);
            mv.addObject("category", category);
            Date date = Date.from(Instant.now().minusSeconds(3600 * 24 * 7));// 7天前的文章
            articleExample.createCriteria().andSysCategoryEqualTo(category).andReleaseDateGreaterThanOrEqualTo(date);
            List<Article> articles = articleService.findByExample(articleExample);
            if (articles == null) {
                return mv;
            }
            objs = new ValueObject();
            objs.set("totalRow",Long.parseLong(""+articles.size()));

            if ("new".equals(option)) {//最新
                Collections.sort(articles, new Comparator<Article>() {
                    @Override
                    public int compare(Article o1, Article o2) {
                        return o1.getReleaseDate().compareTo(o2.getReleaseDate());
                    }
                });
            } else if ("hot".equals(option)) {//最热
                Collections.sort(articles, new Comparator<Article>() {
                    @Override
                    public int compare(Article o1, Article o2) {
                        return articleService.getHeavy(o1) - articleService.getHeavy(o2);
                    }
                });
            }
            int startIndex = (page - 1) * size;
            int endIndex = startIndex + size > articles.size() ? articles.size() : startIndex + size;
            objs.set("articles", articles.subList(startIndex, endIndex));
        }

        List<Article> articles = (List<Article>) objs.get("articles");
        for (int i = 0; i < articles.size(); i++) {
            Article article = articles.get(i);
            article.setContent(articleService.excepeHtml(article.getContent().trim()));
            article.setContent(article.getContent().substring(0, article.getContent().length() < 100 ? article.getContent().length() : 100));
        }
        List<ValueObject> vos = new ArrayList<>();
        for (int i = 0; i < articles.size(); i++) {
            Article article = articles.get(i);
            User user = userService.findById(article.getUserId());
            vos.add(new ValueObject().set("article", article).set("user", user)
                    .set("viewCount", articleService.getViewCount(article.getId()))
                    .set("commentCount", articleService.getCommentCount(article.getId())));
        }

        Long totalRow = (Long) objs.get("totalRow");
        PageResult pageResult = new PageResult(page, size, vos, totalRow);
        mv.addObject("pageResult", pageResult);

        return mv;
    }
}
