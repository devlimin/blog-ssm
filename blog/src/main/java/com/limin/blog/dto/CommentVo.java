package com.limin.blog.dto;

import com.limin.blog.entity.Comment;

/**
 * Created by devlimin on 2017/6/29.
 */
public class CommentVo extends Comment{
    private Integer build;// 楼层
    private Integer grade;// 深度
    private String refUsername;// 回复某人

    public CommentVo(Comment comment) {
        super(comment.getId(),comment.getUserId(),comment.getArticleId(),comment.getReleaseDate(),comment.getParentId(),comment.getContent());
    }

    public Integer getBuild() {
        return build;
    }

    public void setBuild(Integer build) {
        this.build = build;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public String getRefUsername() {
        return refUsername;
    }

    public void setRefUsername(String refUsername) {
        this.refUsername = refUsername;
    }
}
