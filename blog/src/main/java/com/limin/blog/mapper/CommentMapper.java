package com.limin.blog.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.limin.blog.dto.CommentAndArticle;
import com.limin.blog.entity.Comment;
import com.limin.blog.entity.CommentExample;

public interface CommentMapper {
    long countByExample(CommentExample example);

    int deleteByExample(CommentExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Comment record);

    int insertSelective(Comment record);

    List<Comment> selectByExampleWithBLOBs(CommentExample example);

    List<Comment> selectByExample(CommentExample example);

    Comment selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Comment record, @Param("example") CommentExample example);

    int updateByExampleWithBLOBs(@Param("record") Comment record, @Param("example") CommentExample example);

    int updateByExample(@Param("record") Comment record, @Param("example") CommentExample example);

    int updateByPrimaryKeySelective(Comment record);

    int updateByPrimaryKeyWithBLOBs(Comment record);

    int updateByPrimaryKey(Comment record);
    


	void deleteCascadeByPrimaryKey(Long id);


	List<CommentAndArticle> selectCommentAndArticleMyArticle(Long userId);

	List<CommentAndArticle> selectCommentAndArticleMyPublish(Long userId);
}