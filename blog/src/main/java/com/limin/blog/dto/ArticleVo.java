package com.limin.blog.dto;

import com.limin.blog.entity.Article;

import java.util.ArrayList;
import java.util.List;

public class ArticleVo extends Article{
	
	private Integer commentCount;
	private List<Long> categoryIds = new ArrayList<Long>();

	public ArticleVo() {
	}

	public ArticleVo(Article article) {
		super(article.getId(), article.getUserId(), article.getSysCategory(), article.getTitle(),
				article.getReleaseDate(), article.getSupport(), article.getAgainst(), article.getViewCount(), article.getContent());
	}

	public Integer getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(Integer commentCount) {
		this.commentCount = commentCount;
	}

	public List<Long> getCategoryIds() {
		return categoryIds;
	}

	public void setCategoryIds(List<Long> categoryIds) {
		this.categoryIds = categoryIds;
	}
}
