package com.limin.blog.mapper;

import com.limin.blog.dto.CategoryVo;
import com.limin.blog.entity.Category;
import com.limin.blog.entity.CategoryExample;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface CategoryMapper {
    long countByExample(CategoryExample example);

    int deleteByExample(CategoryExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Category record);

    int insertSelective(Category record);

    List<Category> selectByExample(CategoryExample example);

    Category selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Category record, @Param("example") CategoryExample example);

    int updateByExample(@Param("record") Category record, @Param("example") CategoryExample example);

    int updateByPrimaryKeySelective(Category record);

    int updateByPrimaryKey(Category record);

	List<Category> selectByArticleId(long articleId);

	List<CategoryVo> selectWithArticleCountByUid(Long userId);

	void deleteArticleRelationByCid(Long categoryId);
}