package com.limin.blog.dto;

import com.limin.blog.entity.Comment;

public class CommentAndArticle extends Comment {
	private String articleTitle;
	private String username;

	public String getArticleTitle() {
		return articleTitle;
	}

	public void setArticleTitle(String articleTitle) {
		this.articleTitle = articleTitle;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
