package com.limin.blog.service;

import java.util.List;

import com.limin.blog.dto.CategoryVo;
import com.limin.blog.entity.Category;
import com.limin.blog.entity.CategoryExample;

public interface CategoryService {
	
	void add(Category category);
	void deleteById(Long id);
	void update(Category category);
	
	Category findById(Long id);
	
	List<Category> findByExample(CategoryExample example);
	
	List<Category> findByArticleId(long articleId);
	List<CategoryVo> findWithArticleCountByUid(Long userId);
}
