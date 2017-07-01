package com.limin.blog.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.limin.blog.dto.CategoryVo;
import com.limin.blog.entity.Category;
import com.limin.blog.entity.User;
import com.limin.blog.service.CategoryService;

@Controller
@RequestMapping("/category")
public class CategoryController {
	
	@Resource
	private CategoryService categoryService;
	
	
	@RequestMapping("/manage/list")
	public ModelAndView list(HttpSession session) {
		ModelAndView mv = new ModelAndView("manage/category/list");
		mv.addObject("option", "categoryManage");
		User user = (User) session.getAttribute("loginUser");
		List<CategoryVo> categories = categoryService.findWithArticleCountByUid(user.getId());
		mv.addObject("categories", categories);
		return mv;
	}
	
	@RequestMapping("/manage/delete")
	@ResponseBody
	public String delete(Long categoryId) {
		categoryService.deleteById(categoryId);
		return "categoryId";
	}
	
	@RequestMapping("/manage/edit")
	@ResponseBody
	public String edit(Long categoryId, String type, HttpSession session) {
		Category category = new Category();
		category.setId(categoryId);
		category.setType(type);
		User user= (User) session.getAttribute("loginUser");
		category.setUserId(user.getId());
		
		categoryService.update(category);
		return "categoryId";
	}
	
	@RequestMapping("/manage/add")
	@ResponseBody
	public Category add(String type, HttpSession session) {
		Category category = new Category();
		category.setType(type);
		User user= (User) session.getAttribute("loginUser");
		category.setUserId(user.getId());
		
		categoryService.add(category);
		
		return category;
	}
	
	
}
