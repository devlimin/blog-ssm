package com.limin.blog.entity;

import java.util.Date;

public class Article {
    private Long id;

    private Long userId;

    private String sysCategory;

    private String title;

    private Date releaseDate;

    private String content;

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

    public String getSysCategory() {
        return sysCategory;
    }

    public void setSysCategory(String sysCategory) {
        this.sysCategory = sysCategory == null ? null : sysCategory.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}