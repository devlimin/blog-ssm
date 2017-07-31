package com.limin.blog.util;

import org.springframework.data.redis.core.index.PathBasedRedisIndexDefinition;

/**
 * Created by devlimin on 2017/7/28.
 */
public class RedisKeyUtil {
    private static String SPLIT = ":";
    private static String BIZ_SUPPORT = "SUPPORT";
    private static String BIZ_ANAINST = "AGAINST";
    private static String BIZ_VIEW_COUNT = "VIEW_COUNT";
    private static String BIZ_COMMENT_COUNT ="COMMENT_COUNT";
    private static String BIZ_ARTICLE = "ARTICLE";
    private static String BIZ_CATEGORY = "CATEGORY";

    // 文章的赞数 key
    public static String getSupportKey(long articleId) {
        return BIZ_ARTICLE + SPLIT + BIZ_SUPPORT + SPLIT + String.valueOf(articleId);
    }

    // 文章的踩数 key
    public static String getAgainstKey(long articleId) {
        return BIZ_ARTICLE + SPLIT + BIZ_ANAINST + SPLIT + String.valueOf(articleId);
    }

    // 文章的阅读数 key
    public static String getViewCount(long articleId) {
        return BIZ_ARTICLE + SPLIT + BIZ_VIEW_COUNT + SPLIT + String.valueOf(articleId);
    }

    // 文章的评论数量 key
    /*public static String getCommentCount(long articleId) {
        return BIZ_ARTICLE + SPLIT + BIZ_COMMENT_COUNT + String.valueOf(articleId);
    }*/

    // 文章的所有分类id key
    public static String getArticle(long articleId) {
        return BIZ_ARTICLE + SPLIT + BIZ_CATEGORY + SPLIT +  String.valueOf(articleId);
    }

    // 分类的所有文章id key
    public static String getCategory(long categoryId) {
        return BIZ_CATEGORY + SPLIT + BIZ_ARTICLE + SPLIT + String.valueOf(categoryId);
    }

}
