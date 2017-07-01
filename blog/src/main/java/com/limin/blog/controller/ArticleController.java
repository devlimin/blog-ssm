package com.limin.blog.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Stack;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import com.limin.blog.dto.*;
import com.limin.blog.entity.*;
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

		Article article = articleService.findById(aid);
		List<Category> articleCategories = categoryService.findByArticleId(aid);

		User user = userService.findById(article.getUserId());

		List<CategoryVo> categoryList = categoryService.findWithArticleCountByUid(user.getId());

		CommentExample example = new CommentExample();
		example.setOrderByClause("release_date desc");
		example.createCriteria().andArticleIdEqualTo(aid);
		List<Comment> comments = commentService.findByExampelWithBlob(example);

		List<ValueObject> commentAndUsers = new ArrayList<>();
		for(int i = 0; i < comments.size(); i++) {
			Comment comment = comments.get(i);
			User commentUser = userService.findById(comment.getUserId());
			commentAndUsers.add(new ValueObject().set("comment",comment).set("user", commentUser));
		}
		commentAndUsers = formatCommentAndUser(commentAndUsers);

		ModelAndView mv = new ModelAndView("/article/detail");
		mv.addObject("user", user);
		mv.addObject("article", article);
		mv.addObject("articleCategories", articleCategories);
		mv.addObject("categoryList", categoryList);
		mv.addObject("commentAndUsers", commentAndUsers);

		return mv;
	}

	private List<ValueObject> formatCommentAndUser(List<ValueObject> vos) {

		//找出所有父级评论，并在评论列表中删除父级评论
		ArrayList<ValueObject> rootList = new ArrayList<ValueObject>();
		ListIterator<ValueObject> voIterator = vos.listIterator();
		while(voIterator.hasNext()) {
			ValueObject vo = voIterator.next();
			Comment comment = (Comment) vo.get("comment");
			if(comment.getParentId() == null) {
				rootList.add(0, vo);
				voIterator.remove();
			}
		}

		//利用深度遍历找到每个父级评论的子评论和子评论的子评论
		ArrayList<ValueObject> resultList = new ArrayList<ValueObject>();
		ListIterator<ValueObject> rootIterator = rootList.listIterator();
		int build = 0;
		while(rootIterator.hasNext()) {
			ValueObject root = rootIterator.next();
			Comment comment = (Comment) root.get("comment");
			CommentVo commentVo = new CommentVo(comment);
			root.set("comment", commentVo);
			// 父级评论 层中深度为0
			commentVo.setGrade(0);
			Stack<ValueObject> stack = new Stack<ValueObject>();
			stack.push(root);
			ArrayList<ValueObject> list = new ArrayList<ValueObject>();
			// 找出父级评论的自评论和子评论的子评论
			while(!stack.empty()) {
				ValueObject pop = stack.pop();
				list.add(pop);
				CommentVo popComment = (CommentVo) pop.get("comment");
				User popUse = (User) pop.get("user");
				ListIterator<ValueObject> cIterator = vos.listIterator(vos.size());
				while(cIterator.hasPrevious()) {
					ValueObject next = cIterator.previous();
					Comment nextComment = (Comment) next.get("comment");
					if(popComment.getId().equals(nextComment.getParentId())) {
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
	@RequestMapping("/list/{uid}")
	public ModelAndView articleList(@PathVariable(name = "uid") long uid,
			@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "size", defaultValue = "1") int size) {

		ModelAndView mv = new ModelAndView("/article/list");

		User user = userService.findById(uid);

		List<CategoryVo> categoryList = categoryService.findWithArticleCountByUid(user.getId());

		ArticleExample articleExample = new ArticleExample();
		articleExample.createCriteria().andUserIdEqualTo(uid);
		ValueObject vo = articleService.findPageByExample(articleExample, page, size);
		List<Article> articles = (List<Article>) vo.get("articles");
		Long totalRow = (Long) vo.get("totalRow");
		PageResult<Article> pageResult = new PageResult<>(page, size,articles, totalRow);


		mv.addObject("user", user);
		mv.addObject("categoryList", categoryList);
		mv.addObject("pageResult", pageResult);

		return mv;
	}

	// 根据用户分类查询分页文章
	@RequestMapping("/list/{uid}/{categoryId}")
	public ModelAndView articleListByCategory(
			@PathVariable(name = "uid") long uid,
			@PathVariable(name = "categoryId") long cid,
			@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "size", defaultValue = "1") int size) {

		ModelAndView mv = new ModelAndView("article/list");
		mv.addObject("categoryId", cid);

		User user = userService.findById(uid);

		List<CategoryVo> categoryList = categoryService.findWithArticleCountByUid(user.getId());

		PageResult<Article> pageResult = articleService
				.findPageWithContentByCid(cid, page, size);

		mv.addObject("user", user);
		mv.addObject("categoryList", categoryList);
		mv.addObject("pageResult", pageResult);

		return mv;
	}

	@RequestMapping("/support")
	@ResponseBody
	public String support(long articleId) {
		Article article = articleService.findById(articleId);
		if (article != null) {
			article.setSupport(article.getSupport() + 1);
			articleService.update(article);
			return "success";
		}
		return "fail";
	}

	@RequestMapping("/against")
	@ResponseBody
	public String against(long articleId) {
		Article article = articleService.findById(articleId);
		if (article != null) {
			article.setAgainst(article.getAgainst() + 1);
			articleService.update(article);
			return "success";
		}
		return "fail";
	}


	@RequestMapping("/manage/list/{cId}")
	public ModelAndView list(
			@PathVariable(value = "cId", required = true) Long categoryId,
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "size", defaultValue = "1") int size,
			HttpSession session) {
		ModelAndView mv = new ModelAndView("manage/article/list");
		mv.addObject("option", "articleManage");
		
        mv.addObject("categoryId", categoryId);
        if(categoryId == -1) {
            categoryId = null;
        }

		User user = (User) session.getAttribute("loginUser");
		PageResult<ArticleVo> pageResult = null;
		ValueObject vo = articleService.findPageArticleWithCommentCountByUidAndCid(user.getId(), categoryId, page, size);
		List<ArticleVo> articles = (List<ArticleVo>) vo.get("articles");
		long totalRow = (long) vo.get("totalRow");
		pageResult = new PageResult<ArticleVo>(page, size,articles, totalRow);
		mv.addObject("pageResult", pageResult);

		CategoryExample categoryExample = new CategoryExample();
		categoryExample.createCriteria().andUserIdEqualTo(user.getId());
		List<Category> list = categoryService.findByExample(categoryExample);
		mv.addObject("categoryList", list);

		return mv;
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
		for(int i = 0; i < articleCategories.size(); i++) {
			articleVo.getCategoryIds().add(articleCategories.get(i).getId());
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
		articleService.updateWithCategories(articleVo);
		return "redirect:/article/detail/"+articleVo.getId();
	}
	

	@RequestMapping("/manage/delete")
	public String delete(@RequestParam("articleId")Long articleId) {
		articleService.deleteById(articleId);
		return "";
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
		articleService.addWithCategories(articleVo);
		return "redirect:/article/detail/"+articleVo.getId();
	}
}
