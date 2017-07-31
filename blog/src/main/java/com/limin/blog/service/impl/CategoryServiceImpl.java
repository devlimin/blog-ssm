package com.limin.blog.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import com.limin.blog.util.JedisAdapter;
import com.limin.blog.util.RedisKeyUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import com.limin.blog.entity.Category;
import com.limin.blog.entity.CategoryExample;
import com.limin.blog.mapper.CategoryMapper;
import com.limin.blog.service.CategoryService;

@Service("categoryService")
public class CategoryServiceImpl implements CategoryService {

	@Resource
	private CategoryMapper categoryMapper;

	@Resource
	private JedisAdapter jedisAdapter;
	
	@Override
	public void add(Category category) {
		category.setType(HtmlUtils.htmlEscape(category.getType()));//过滤标签
		categoryMapper.insert(category);
	}

	@Override
	public void deleteById(Long id) {
		categoryMapper.deleteByPrimaryKey(id);

		String categorykey = RedisKeyUtil.getCategory(id);
		for (String articleId :jedisAdapter.smembers(categorykey)) {
			String articlekey = RedisKeyUtil.getArticle(Long.parseLong(articleId));
			jedisAdapter.srem(articlekey, String.valueOf(id));
		}
		jedisAdapter.del(categorykey);

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
		
		String articlekey = RedisKeyUtil.getArticle(articleId);
		Set<String> categoryIds = jedisAdapter.smembers(articlekey);
		List<Category> categories = new ArrayList<>();
		for(String categoryId : categoryIds) {
			categories.add(findById(Long.parseLong(categoryId)));
		}
		return categories;
	}


	@Override
	public Long findArticleCountById(Long id) {
		String categorykey = RedisKeyUtil.getCategory(id);
		return jedisAdapter.scard(categorykey);
	}

	@Override
	public Set<String> findArticleIdsById(Long categoryId) {
		return jedisAdapter.smembers(RedisKeyUtil.getCategory(categoryId));
	}


}
