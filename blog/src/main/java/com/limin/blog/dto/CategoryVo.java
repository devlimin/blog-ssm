package com.limin.blog.dto;

import com.limin.blog.entity.Category;

public class CategoryVo extends Category{

	private Integer articlecount;

	public Integer getArticlecount() {
		return articlecount;
	}

	public void setArticlecount(Integer articlecount) {
		this.articlecount = articlecount;
	}


}
