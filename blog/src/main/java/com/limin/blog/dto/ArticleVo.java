package com.limin.blog.dto;

import com.limin.blog.entity.Article;

import java.util.ArrayList;
import java.util.List;

public class ArticleVo extends Article{
	private String username;
	private Integer commentCount;
	private List<String> categoryIds = new ArrayList<>();

	public ArticleVo() {
	}

	public ArticleVo(Article article) {
		super(article.getId(), article.getUserId(), article.getSysCategory(), article.getTitle(),
				article.getReleaseDate(), article.getContent());
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(Integer commentCount) {
		this.commentCount = commentCount;
	}

	public List<String> getCategoryIds() {
		return categoryIds;
	}

	public void setCategoryIds(List<String> categoryIds) {
		this.categoryIds = categoryIds;
	}
}
