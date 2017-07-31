package com.limin.blog;

import com.limin.blog.dto.ArticleVo;
import com.limin.blog.dto.ValueObject;
import com.limin.blog.util.ArticleLucene;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;

/**
 * Created by devlimin on 2017/6/29.
 */
public class Test {
    @org.junit.Test
    public void testDate() {
        LocalDate now = LocalDate.now();
        System.out.println(now);
        System.out.println(now.minusWeeks(1));
        Date from = Date.from(Instant.now().minusSeconds(3600 * 24 * 7));
        System.out.println(from);
    }

    @org.junit.Test
    public void testPath(){
        String indexPath = "src/main/resources/index";
        Path path = Paths.get(indexPath);
        System.out.println(path);
        System.out.println(path.getFileName());
        path = path.toAbsolutePath();
        System.out.println(path);
    }

    @org.junit.Test
    public void testSearch() {
        ArticleLucene articleIndex = new ArticleLucene();
        ValueObject vo = articleIndex.searchIndex("lucnen", 1, 3);
        System.out.println(vo.get("articles"));
    }

    @org.junit.Test
    public void testAdd() {
        ArticleLucene articleIndex = new ArticleLucene();
        articleIndex.addIndex(new ArticleVo());
    }
 }
