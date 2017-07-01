package com.limin.blog.controller;

import javax.annotation.Resource;

import com.limin.blog.dto.ValueObject;
import com.limin.blog.entity.Article;
import com.limin.blog.entity.ArticleExample;
import com.limin.blog.entity.User;
import com.limin.blog.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.limin.blog.service.ArticleService;
import com.limin.blog.util.PageResult;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class IndexController {

	@Resource
	private ArticleService articleService;

	@Resource
	private UserService userService;


	@RequestMapping(value = {"/", "/sys/{option}/{category}"})
	public ModelAndView index(
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "size", defaultValue = "1") int size,
			@PathVariable(value = "option", required = false) String option,
			@PathVariable(value = "category",  required = false) String category) {

		ModelAndView mv = new ModelAndView("index");//首页

		ArticleExample articleExample = new ArticleExample();

		if(option == null && category == null) {
			articleExample.setOrderByClause("release_date desc");
		} else if(option != null && category != null) { //文章系统分类
			mv.addObject("option", option);
			mv.addObject("category", category);
			articleExample.createCriteria().andSysCategoryEqualTo(category);
			String orderClause = null;
			if("new".equals(option)) {//最新
				orderClause = "release_date desc";
			} else if("hot".equals(option)) {//最热
				Date date = Date.from(Instant.now().minusSeconds(3600 * 24 * 7));
				articleExample.createCriteria().andReleaseDateGreaterThanOrEqualTo(date);
				orderClause = "(support + view_count) desc";
			}
			articleExample.setOrderByClause(orderClause);
		}
		ValueObject objs = articleService.findPageByExample(articleExample, page, size);

		List<Article> articles = (List<Article>) objs.get("articles");
		List<ValueObject> vos = new ArrayList<>();
		for(int i = 0; i < articles.size(); i++) {
			Article article = articles.get(i);
			User user = userService.findById(article.getUserId());
			vos.add(new ValueObject().set("article", article).set("user",user));
		}

		Long totalRow = (Long) objs.get("totalRow");
		PageResult pageResult = new PageResult(page, size, vos, totalRow);
		mv.addObject("pageResult", pageResult);

		return mv;
	}
}
