package com.limin.blog.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import com.limin.blog.dto.CategoryVo;
import com.limin.blog.entity.Category;
import com.limin.blog.entity.CategoryExample;
import com.limin.blog.mapper.CategoryMapper;
import com.limin.blog.service.CategoryService;

@Service("categoryService")
public class CategoryServiceImpl implements CategoryService {

	@Resource
	private CategoryMapper categoryMapper;
	
	@Override
	public void add(Category category) {
		category.setType(HtmlUtils.htmlEscape(category.getType()));//过滤标签
		categoryMapper.insert(category);
	}

	@Override
	public void deleteById(Long id) {
		categoryMapper.deleteByPrimaryKey(id);
		categoryMapper.deleteArticleRelationByCid(id);
	}

	@Override
	public void update(Category category) {
		category.setType(HtmlUtils.htmlEscape(category.getType()));//过滤标签
		categoryMapper.updateByPrimaryKeySelective(category);
	}

	@Override
	public Category findById(Long id) {
		return categoryMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<Category> findByExample(CategoryExample example) {
		List<Category> list = categoryMapper.selectByExample(example);
		return list;
	}

	@Override
	public List<Category> findByArticleId(long articleId) {
		
		List<Category> categoryList = categoryMapper.selectByArticleId(articleId);
		
		return categoryList;
	}

	@Override
	public List<CategoryVo> findWithArticleCountByUid(Long userId) {
		List<CategoryVo> list = categoryMapper.selectWithArticleCountByUid(userId);
		
		return list;
	}

}
