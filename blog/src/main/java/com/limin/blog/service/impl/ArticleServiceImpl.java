package com.limin.blog.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.limin.blog.dto.ValueObject;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.limin.blog.dto.ArticleVo;
import com.limin.blog.entity.Article;
import com.limin.blog.entity.ArticleExample;
import com.limin.blog.mapper.ArticleMapper;
import com.limin.blog.mapper.CommentMapper;
import com.limin.blog.service.ArticleService;
import com.limin.blog.util.PageResult;

@Service("articleService")
public class ArticleServiceImpl implements ArticleService {

	@Resource
	private ArticleMapper articleMapper;
	
	@Resource
	private CommentMapper commentMapper;
	
	@Override
	public void add(Article article) {
		article.setTitle(HtmlUtils.htmlEscape(article.getTitle()));
		articleMapper.insert(article);
	}

	@Override
	public void update(Article article) {
		article.setTitle(HtmlUtils.htmlEscape(article.getTitle()));
		articleMapper.updateByPrimaryKeyWithBLOBs(article);
		articleMapper.updateByPrimaryKeySelective(article);
	}
	

	@Override
	public void deleteById(Long id) {
		articleMapper.deleteCategoryRelationByArticleId(id);
		//删除关联的评论
		
		articleMapper.deleteByPrimaryKey(id);
		
	}

	@Override
	public Article findById(Long id) {
		Article article = articleMapper.selectByPrimaryKey(id);
		article.setViewCount(article.getViewCount()+1);
		articleMapper.updateByPrimaryKeySelective(article);
		return article;
	}



	@Override
	public ValueObject findPageByExample(ArticleExample example,
                                         int pageNum, int pageSize) {
		
		PageHelper.startPage(pageNum, pageSize);
		List<Article> articles = articleMapper.selectByExampleWithBLOBs(example);
		PageInfo<Article> pageInfo = new PageInfo<Article>(articles);
		long totalRow = pageInfo.getTotal();

		ValueObject objs = new ValueObject();
		objs.set("articles", articles);
		objs.set("totalRow", totalRow);

		return objs;
	}

	@Override
	public PageResult<Article> findPageWithContentByCid(long cid, int pageNum, int pageSize){
		PageHelper.startPage(pageNum, pageSize);
		List<Article> list = articleMapper.selectByCid(cid);	
		PageInfo<Article> pageInfo = new PageInfo<Article>(list);
		long totalRow = pageInfo.getTotal();
		
		PageResult<Article> pageResult = new PageResult<Article>(pageNum, pageSize, list, totalRow);
		return pageResult;
	}

	@Override
	public ValueObject findPageArticleWithCommentCountByUidAndCid(
			Long userId, Long categoryId, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<ArticleVo> articles = articleMapper.selectArticleWithCommentCountByUidAndCid(userId, categoryId);
		PageInfo<ArticleVo> pageInfo = new PageInfo<ArticleVo>(articles);
		long totalRow = pageInfo.getTotal();

		ValueObject vo = new ValueObject();
		vo.set("articles", articles);
		vo.set("totalRow", totalRow);
		return vo;
	}

	@Override
	public void addWithCategories(ArticleVo articleVo) {
		Article article = articleVo;
		articleVo.setTitle(HtmlUtils.htmlEscape(articleVo.getTitle()));
		articleMapper.insertSelective(article);

		Long articleId = article.getId();
		List<Long> categoryIds = articleVo.getCategoryIds();
		articleMapper.insertRelationCategories(articleId, categoryIds);
	}

	@Override
	public void updateWithCategories(
			ArticleVo articleVo) {
		Article article = articleVo;
		article.setReleaseDate(new Date());
		article.setTitle(HtmlUtils.htmlEscape(articleVo.getTitle()));
		articleMapper.updateByPrimaryKeySelective(article);
		
		articleMapper.deleteCategoryRelationByArticleId(article.getId());
		articleMapper.insertRelationCategories(article.getId(), articleVo.getCategoryIds());
		
	}

}
