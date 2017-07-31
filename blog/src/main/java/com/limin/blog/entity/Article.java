package com.limin.blog.entity;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class Article {
    private Long id;

    private Long userId;

    private String sysCategory;

    private String title;

    private Date releaseDate;

    private String content;

    public Article() {

    }
    public Article(Long id, Long userId, String sysCategory, String title, Date releaseDate, String content) {
        this.id = id;
        this.userId = userId;
        this.sysCategory = sysCategory;
        this.title = title;
        this.releaseDate = releaseDate;
        this.content = content;
    }

    public static final String MOBILE_DEVELOPEMENT="1";
    public static final String WEB_FRONT_END="2";
    public static final String ARCHITECTURE_DESIGN="3";
    public static final String PROGRAMMING_LANGUAGE="4";
    public static final String INTERNET="5";
    public static final String DATABASE="6";
    public static final String SYSTEM_OPERATIONAL="7";
    public static final String CLOUD_COMPUTING="8";
    public static final String RESEARCH_DEVELOPMENT_MANAGEMENT="9";
    public static final String COMPREHENSIVE="10";

    public static final Map<String, String> sysCategoryType;
    static {
        sysCategoryType = new LinkedHashMap<String, String>();
        sysCategoryType.put(MOBILE_DEVELOPEMENT, "移动开发");
        sysCategoryType.put(WEB_FRONT_END, "Web前端");
        sysCategoryType.put(ARCHITECTURE_DESIGN, "架构设计");
        sysCategoryType.put(PROGRAMMING_LANGUAGE, "编程语言");
        sysCategoryType.put(INTERNET, "互联网");

        sysCategoryType.put(DATABASE, "数据库");
        sysCategoryType.put(SYSTEM_OPERATIONAL, "系统运维");
        sysCategoryType.put(CLOUD_COMPUTING, "云计算");
        sysCategoryType.put(RESEARCH_DEVELOPMENT_MANAGEMENT, "研发管理");
        sysCategoryType.put(COMPREHENSIVE, "综合");
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