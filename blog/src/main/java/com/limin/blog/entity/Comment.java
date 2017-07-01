package com.limin.blog.entity;

import java.util.Date;

public class Comment {
    private Long id;

    private Long userId;

    private Long articleId;

    private Date releaseDate;

    private Long parentId;

    private String content;

    public Comment() {
    }

    public Comment(Long id, Long userId, Long articleId, Date releaseDate, Long parentId, String content) {
        this.id = id;
        this.userId = userId;
        this.articleId = articleId;
        this.releaseDate = releaseDate;
        this.parentId = parentId;
        this.content = content;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
	
}