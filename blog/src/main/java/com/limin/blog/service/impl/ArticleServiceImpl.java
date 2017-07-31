package com.limin.blog.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import com.limin.blog.dto.ValueObject;
import com.limin.blog.entity.CommentExample;
import com.limin.blog.util.ArticleLucene;
import com.limin.blog.util.JedisAdapter;
import com.limin.blog.util.RedisKeyUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.limin.blog.entity.Article;
import com.limin.blog.entity.ArticleExample;
import com.limin.blog.mapper.ArticleMapper;
import com.limin.blog.mapper.CommentMapper;
import com.limin.blog.service.ArticleService;

@Service("articleService")
public class ArticleServiceImpl implements ArticleService {

	@Resource
	private ArticleMapper articleMapper;
	
	@Resource
	private CommentMapper commentMapper;

	@Resource
	private ArticleLucene articleLucene;

	@Resource
	private JedisAdapter jedisAdapter;


	/*@Override
	public void add(Article article) {
		article.setTitle(HtmlUtils.htmlEscape(article.getTitle()));
		articleMapper.insert(article);
	}*/

	@Override
	public void add(Article article, List<String> categoryIds) {
		article.setReleaseDate(new Date());
		article.setTitle(excepeHtml(article.getTitle()));
		articleMapper.insertSelective(article);
		String articleKey = RedisKeyUtil.getArticle(article.getId());
		jedisAdapter.sadd(articleKey, categoryIds.toArray(new String[0]));
		String categoryKey = null;
		for (int i = 0; i < categoryIds.size(); i++) {
			categoryKey = RedisKeyUtil.getCategory(Integer.parseInt(categoryIds.get(i)));
			jedisAdapter.sadd(categoryKey, String.valueOf(article.getId()));
		}
		article.setContent(excepeHtml(article.getContent()));
		articleLucene.addIndex(article);
	}

    @Override
    public void update(Article article, List<String> categoryIds) {
	    article.setReleaseDate(new Date());
	    article.setTitle(HtmlUtils.htmlEscape(article.getTitle()));
        articleMapper.updateByPrimaryKeySelective(article);

        String articlekey = RedisKeyUtil.getArticle(article.getId());
        for (String categoryId :jedisAdapter.smembers(articlekey)) {
            String categorykey = RedisKeyUtil.getCategory(Long.parseLong(categoryId));
            jedisAdapter.srem(categorykey, String.valueOf(article.getId()));
        }
        jedisAdapter.del(articlekey);

        jedisAdapter.sadd(articlekey, categoryIds.toArray(new String[0]));
        for (String categoryId : categoryIds) {
            String categorykey = RedisKeyUtil.getCategory(Long.parseLong(categoryId));
            jedisAdapter.sadd(categorykey, String.valueOf(article.getId()));
        }
		article.setContent(excepeHtml(article.getContent()));
        articleLucene.updateIndex(article);
    }

	@Override
	public List<Article> findByExample(ArticleExample articleExample) {
		return articleMapper.selectByExampleWithBLOBs(articleExample);
	}

	@Override
	public int getHeavy(Article article) {
		int viewCount = Integer.parseInt(jedisAdapter.get(RedisKeyUtil.getViewCount(article.getId())));
		int commentCount = Math.toIntExact(getCommentCount(article.getId()));
		int support = Integer.parseInt(jedisAdapter.get(RedisKeyUtil.getSupportKey(article.getId())));
		int against = Integer.parseInt(jedisAdapter.get(RedisKeyUtil.getAgainstKey(article.getId())));
		long time = article.getReleaseDate().getTime();

		return (int) ((viewCount * 100 + commentCount * 300 + support * 100 - against*100 ) / time);
	}


	@Override
	public Article findById(Long id) {
		Article article = articleMapper.selectByPrimaryKey(id);
		return article;
	}

	@Override
	public Long incrViewCount(long articleId) {
		return jedisAdapter.incr(RedisKeyUtil.getViewCount(articleId));
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
	public Long getViewCount(Long articleId) {
		String viewCount = RedisKeyUtil.getViewCount(articleId);
		return Long.valueOf(jedisAdapter.get(viewCount));
	}
	@Override
    public Long getCommentCount(Long articleId) {
		CommentExample example = new CommentExample();
		example.createCriteria().andArticleIdEqualTo(articleId);
		return commentMapper.countByExample(example);
    }
	@Override
	public Long getSupport(Long articleId) {
		String support = jedisAdapter.get(RedisKeyUtil.getSupportKey(articleId));
		return support == null ? 0L : Long.parseLong(support);
	}
	@Override
	public Long getAgainst(Long articleId) {
		String against = jedisAdapter.get(RedisKeyUtil.getAgainstKey(articleId));
		return against == null ? 0L : Long.parseLong(against);
	}

	@Override
	public Long support(long articleId) {
		String supportKey = RedisKeyUtil.getSupportKey(articleId);
		return jedisAdapter.incr(supportKey);
	}
	@Override
	public Long unsupport(long articleId) {
		String supportKey = RedisKeyUtil.getSupportKey(articleId);
		return jedisAdapter.decr(supportKey);
	}

	@Override
	public Long against(long articleId) {
		String againstKey = RedisKeyUtil.getAgainstKey(articleId);
		return jedisAdapter.incr(againstKey);
	}
	@Override
	public Long unagainst(long articleId) {
		String againstKey = RedisKeyUtil.getAgainstKey(articleId);
		return jedisAdapter.decr(againstKey);
	}

	@Override
	public Set<String> getCategoryIds(Long articleId) {
        String articlekey = RedisKeyUtil.getArticle(articleId);
        return jedisAdapter.smembers(articlekey);
    }

	@Override
	public void deleteById(Long id) {
		//删除关联的评论
		CommentExample example = new CommentExample();
		example.createCriteria().andArticleIdEqualTo(id);
		commentMapper.deleteByExample(example);

		String articleKey = RedisKeyUtil.getArticle(id);
		Set<String> categoryIds = jedisAdapter.smembers(articleKey);
		String categoryKey = null;
		for (String categoryId : categoryIds) {
			categoryKey = RedisKeyUtil.getCategory(Integer.parseInt(categoryId));
			jedisAdapter.srem(categoryKey, String.valueOf(id));
		}
		jedisAdapter.del(articleKey);
		jedisAdapter.del(RedisKeyUtil.getViewCount(id));
		jedisAdapter.del(RedisKeyUtil.getSupportKey(id));
		jedisAdapter.del(RedisKeyUtil.getAgainstKey(id));

		articleMapper.deleteByPrimaryKey(id);
		articleLucene.deleteIndex(id);
	}


	@Override
	public String excepeHtml(String inputString) {
		if (inputString == null)
			return null;
		String htmlStr = inputString; // 含html标签的字符串
		String textStr = "";
		java.util.regex.Pattern p_script;
		java.util.regex.Matcher m_script;
		java.util.regex.Pattern p_style;
		java.util.regex.Matcher m_style;
		java.util.regex.Pattern p_html;
		java.util.regex.Matcher m_html;
		try {
			//定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
			String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
			//定义style的正则表达式{或]*?>[\\s\\S]*?<\\/style>
			String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";
			String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
			p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
			m_script = p_script.matcher(htmlStr);
			htmlStr = m_script.replaceAll(""); // 过滤script标签
			p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
			m_style = p_style.matcher(htmlStr);
			htmlStr = m_style.replaceAll(""); // 过滤style标签
			p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
			m_html = p_html.matcher(htmlStr);
			htmlStr = m_html.replaceAll(""); // 过滤html标签
			textStr = htmlStr;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return textStr;// 返回文本字符串
	}



}
