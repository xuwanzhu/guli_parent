package com.atguigu.article.service;

import com.atguigu.article.entity.Article;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 文章 服务类
 * </p>
 *
 * @author atguigu
 * @since 2022-04-10
 */
public interface ArticleService extends IService<Article> {

    //1、分页文章列表
    Map<String, Object> getPageArticleList(Page<Article> pageParam);

    //2、查询当前用户分页文章列表
    Map<String, Object> getPageUserArticleList(Page<Article> pageParam,String memberId);
}
