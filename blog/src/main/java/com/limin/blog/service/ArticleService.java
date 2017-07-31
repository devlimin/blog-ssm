package com.limin.blog.service;

import com.limin.blog.dto.ArticleVo;
import com.limin.blog.dto.ValueObject;
import com.limin.blog.entity.Article;
import com.limin.blog.entity.ArticleExample;
import com.limin.blog.util.PageResult;

import java.util.List;
import java.util.Set;

public interface ArticleService {
	

	Long unagainst(long articleId);

	Set<String> getCategoryIds(Long articleId);

	void deleteById(Long id);
	
	
	Article findById(Long id);


    Long incrViewCount(long articleId);

    ValueObject findPageByExample(ArticleExample articleExample, int pageNum, int pageSize);



	Long getViewCount(Long articleId);

    Long getCommentCount(Long articleId);

    Long getSupport(Long articleId);

	Long getAgainst(Long articleId);

	Long support(long articleId);

	Long unsupport(long articleId);

	Long against(long articleId);

	void add(Article article, List<String> categoryIds);

	void update(Article article, List<String> categoryIds);

    List<Article> findByExample(ArticleExample articleExample);

	int getHeavy(Article article);

	String excepeHtml(String inputString);
}
