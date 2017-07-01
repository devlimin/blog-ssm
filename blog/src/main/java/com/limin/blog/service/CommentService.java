package com.limin.blog.service;

import java.util.List;

import com.limin.blog.dto.CommentAndArticle;
import com.limin.blog.dto.ValueObject;
import com.limin.blog.entity.Comment;
import com.limin.blog.entity.CommentExample;
import com.limin.blog.util.PageResult;

public interface CommentService {
	
	void add(Comment comment);
	void deleteCascadeById(Long id);
	void update(Comment comment);
	
	Comment findById(Long id);

	List<Comment> findByExampelWithBlob(CommentExample example);
	ValueObject findPageByExample(CommentExample example, int pageNum, int pageSize);
	

	
	PageResult<CommentAndArticle> findPageCommentAndArticleMyArticle(Long userId, int pageNum, int pageSize);
	PageResult<CommentAndArticle> findPageCommentAndArticleMyPublish(Long userId, int pageNum, int pageSize);
}
