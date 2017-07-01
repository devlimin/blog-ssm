package com.limin.blog.mapper;

import com.limin.blog.dto.ArticleVo;
import com.limin.blog.entity.Article;
import com.limin.blog.entity.ArticleExample;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface ArticleMapper {
    long countByExample(ArticleExample example);

    int deleteByExample(ArticleExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Article record);

    int insertSelective(Article record);

    List<Article> selectByExampleWithBLOBs(ArticleExample example);

    List<Article> selectByExample(ArticleExample example);

    Article selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Article record, @Param("example") ArticleExample example);

    int updateByExampleWithBLOBs(@Param("record") Article record, @Param("example") ArticleExample example);

    int updateByExample(@Param("record") Article record, @Param("example") ArticleExample example);

    int updateByPrimaryKeySelective(Article record);

    int updateByPrimaryKeyWithBLOBs(Article record);

    int updateByPrimaryKey(Article record);



	List<Article> selectByCid(long cid);

	List<ArticleVo> selectArticleWithCommentCountByUid(Long userId);

	List<ArticleVo> selectArticleWithCommentCountByUidAndCid(
            @Param("userId") Long userId, @Param("categoryId") Long categoryId);


	void deleteCategoryRelationByArticleId(Long articleId);

	void insertRelationCategories(@Param("articleId") Long articleId, @Param("categoryIds") List<Long> categoryIds);
}