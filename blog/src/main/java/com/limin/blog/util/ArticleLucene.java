package com.limin.blog.util;

import com.limin.blog.dto.ArticleVo;
import com.limin.blog.dto.ValueObject;
import com.limin.blog.entity.Article;
import com.limin.blog.service.UserService;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by devlimin on 2017/7/2.
 */
@Service
public class ArticleLucene {
    @Resource
    private JedisAdapter jedisAdapter;

    @Resource
    private UserService userService;

    private static final String indexPath = "E://workspace//idea//practice//blog-ssm//blog//src//main//resources//index";
    private static Directory indexDir = null;
    private static Analyzer cnAnalyzer = null;
    static {
        try {
            indexDir = FSDirectory.open(Paths.get(indexPath));
            cnAnalyzer = new SmartChineseAnalyzer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addIndex(Article article) {
        IndexWriter writer = null;
        try {
            IndexWriterConfig iwc = new IndexWriterConfig(cnAnalyzer);
            iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
            writer = new IndexWriter(indexDir, iwc);

            Document doc = new Document();
            doc.add(new StringField("id", String.valueOf(article.getId()), Field.Store.YES));
            doc.add(new StringField("username", userService.findById(article.getUserId()).getUsername(), Field.Store.YES));
            doc.add(new TextField("title", article.getTitle(), Field.Store.YES));
            doc.add(new TextField("content", article.getContent(), Field.Store.YES));
            Date releaseDate = article.getReleaseDate();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String format = sdf.format(new Date());//
            doc.add(new StringField("releaseDate", format, Field.Store.YES));

            writer.addDocument(doc);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if(writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void updateIndex(Article article) {
        IndexWriter writer = null;
        try {
            IndexWriterConfig iwc = new IndexWriterConfig(cnAnalyzer);
            writer = new IndexWriter(indexDir, iwc);

            Document doc = new Document();
            doc.add(new TextField("title", article.getTitle(), Field.Store.YES));
            doc.add(new TextField("content", article.getContent(), Field.Store.YES));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String format = sdf.format(article.getReleaseDate());
            doc.add(new StringField("releaseDate", format, Field.Store.YES));

            writer.updateDocument(new Term("id", String.valueOf(article.getId())), doc);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void deleteIndex(Long articleId) {
        IndexWriter writer = null;
        try {
            IndexWriterConfig iwc = new IndexWriterConfig(cnAnalyzer);
            writer = new IndexWriter(indexDir, iwc);

            writer.deleteDocuments(new Term("id", String.valueOf(articleId)));
            writer.forceMergeDeletes();
            writer.commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public ValueObject searchIndex(String queryString, int page, int size) {
        List<ArticleVo> articles = new ArrayList<>();
        int totalHits = 0;
        ValueObject vo = new ValueObject();
        IndexReader reader = null;
        try {
            reader = DirectoryReader.open(indexDir);
            IndexSearcher searcher = new IndexSearcher(reader);

            QueryParser contentParser = new QueryParser("content", cnAnalyzer);
            Query contentQuery = contentParser.parse(queryString);

            int hitsPerPage = 8;
            TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage);
            searcher.search(contentQuery, collector);

            ScoreDoc[] scoreDocs = collector.topDocs().scoreDocs;
            totalHits = scoreDocs.length;

            QueryScorer scorer = new QueryScorer(contentQuery);
            Formatter formatter = new SimpleHTMLFormatter("<span style='color:red;'>", "</span>");
            Highlighter highlighter = new Highlighter(formatter,scorer);

            int start = (page - 1) * size;
            int end = start + size;
            if (start > totalHits) {
                return vo;
            }
            if (end > totalHits) {
                end = totalHits;
            }
            ArticleVo article = null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            for (int i = start; i < end; i++) {
                int docId = scoreDocs[i].doc;
                Document doc = searcher.doc(docId);
                Long id = Long.valueOf((doc.get("id")));
                String username = doc.get("username");
                String title = doc.get("title");
                String content = doc.get("content");
                String format = doc.get("releaseDate");
                Date releaseDate = sdf.parse(format);

                article = new ArticleVo();

                TokenStream tokenStream = cnAnalyzer.tokenStream("content", new StringReader(content));
                content = highlighter.getBestFragment(tokenStream, content);
                if (content != null && !"".equals(content.trim())) {
                    if (content.length() <= 300) {
                        article.setContent(content);
                    } else {
                        article.setContent(content.substring(0, 300));
                    }
                }
                article.setId(id);
                article.setTitle(title);
                article.setUsername(username);
                article.setReleaseDate(releaseDate);
                articles.add(article);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        vo.set("articles", articles);
        vo.set("totalHits", totalHits);
        return vo;
    }

}
