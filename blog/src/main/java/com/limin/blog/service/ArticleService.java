package com.limin.blog.service;

import com.limin.blog.dto.ArticleVo;
import com.limin.blog.dto.ValueObject;
import com.limin.blog.entity.Article;
import com.limin.blog.entity.ArticleExample;
import com.limin.blog.util.PageResult;

public interface ArticleService {
	
	void add(Article article);
	void update(Article article);
	void deleteById(Long id);
	
	
	Article findById(Long id);

	
	ValueObject findPageByExample(ArticleExample articleExample, int pageNum, int pageSize);
	PageResult<Article> findPageWithContentByCid(long cid, int pageNum, int pageSize);

	ValueObject findPageArticleWithCommentCountByUidAndCid(
            Long userId, Long categoryId, int pageSize, int pageNum);

	void addWithCategories(ArticleVo articleVo);
	
	void updateWithCategories(ArticleVo articleVo);
}
