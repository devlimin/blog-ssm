package com.limin.blog.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.limin.blog.dto.ValueObject;
import com.limin.blog.entity.CommentExample;
import com.limin.blog.util.JedisAdapter;
import com.limin.blog.util.RedisKeyUtil;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.limin.blog.dto.CommentAndArticle;
import com.limin.blog.entity.Comment;
import com.limin.blog.mapper.CommentMapper;
import com.limin.blog.service.CommentService;
import com.limin.blog.util.PageResult;
import org.springframework.web.bind.annotation.ResponseBody;

@Service("commentService")
public class CommentServiceImpl implements CommentService {

	@Resource
	private CommentMapper commentMapper;

	@Resource
	private JedisAdapter jedisAdapter;
	
	@Override
	public void add(Comment comment) {
		commentMapper.insertSelective(comment);
	}

	@Override
	public void deleteCascadeById(Long id) {
		commentMapper.deleteCascadeByPrimaryKey(id);
	}
	

	@Override
	public void update(Comment comment) {
		commentMapper.updateByPrimaryKeySelective(comment);
	}

	@Override
	public Comment findById(Long id) {
		return commentMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<Comment> findByExampelWithBlob(CommentExample example) {
		List<Comment> comments = null;
		comments = commentMapper.selectByExampleWithBLOBs(example);
		return comments;
	}


	@Override
	public ValueObject findPageByExample(CommentExample example, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
        List<Comment> comments = commentMapper.selectByExample(example);
        PageInfo<Comment> pageInfo = new PageInfo<>(comments);
        long totalRow = pageInfo.getTotal();

        ValueObject objs = new ValueObject();
        objs.set("comments", comments);
        objs.set("totalRow", totalRow);
        return objs;
	}


	@Override
	public PageResult<CommentAndArticle> findPageCommentAndArticleMyArticle(Long userId, int pageNum, int pageSize){
		PageHelper.startPage(pageNum, pageSize);
		List<CommentAndArticle> list = commentMapper.selectCommentAndArticleMyArticle(userId);

		long total = new PageInfo<CommentAndArticle>(list).getTotal();
		PageResult<CommentAndArticle> pageResult = new PageResult<CommentAndArticle>(pageNum, pageSize, list, total);
		return pageResult;
	}

	@Override
	public PageResult<CommentAndArticle> findPageCommentAndArticleMyPublish(Long userId, int pageNum, int pageSize){
		PageHelper.startPage(pageNum, pageSize);
		List<CommentAndArticle> list = commentMapper.selectCommentAndArticleMyPublish(userId);

		long total = new PageInfo<CommentAndArticle>(list).getTotal();
		PageResult<CommentAndArticle> pageResult = new PageResult<CommentAndArticle>(pageNum, pageSize, list, total);
		return pageResult;
	}

}
