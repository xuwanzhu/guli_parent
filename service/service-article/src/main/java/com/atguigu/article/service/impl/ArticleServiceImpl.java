package com.atguigu.article.service.impl;

import com.atguigu.article.entity.Article;
import com.atguigu.article.mapper.ArticleMapper;
import com.atguigu.article.service.ArticleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 文章 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2022-04-10
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    //1、分页文章列表
   //@Cacheable(value = "article", key = "'articlelist'")  //开启轮播图 redis缓存
    @Override
    public Map<String, Object> getPageArticleList(Page<Article> pageParam) {

        QueryWrapper<Article> wrapper = new QueryWrapper<>();
        wrapper.eq("is_publish",1);
        wrapper.orderByDesc("gmt_create");//按最新排序

        baseMapper.selectPage(pageParam,wrapper); //获取到的数据都封装到了pageTeacher里面

        List<Article> records = pageParam.getRecords();

        long current = pageParam.getCurrent(); //当前页
        long pages = pageParam.getPages();
        long size = pageParam.getSize();
        long total = pageParam.getTotal();
        boolean hasNext = pageParam.hasNext(); //下一页
        boolean hasPrevious = pageParam.hasPrevious();  //上一页

        HashMap<String, Object> map = new HashMap<>(); //将上面的数据封装到map中返回给前端
        map.put("items", records); //每个讲师数据 list
        map.put("current", current); //当前页
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);

        return map;
    }


    //2、查询指定用户的分页文章列表
    @Override
    public Map<String, Object> getPageUserArticleList(Page<Article> pageParam, String memberId) {
        QueryWrapper<Article> wrapper = new QueryWrapper<>();
        //wrapper.eq("is_publish",1);
        wrapper.eq("member_id",memberId); //查询指定用户的文章
        wrapper.orderByDesc("gmt_create");//按最新排序

        baseMapper.selectPage(pageParam,wrapper); //获取到的数据都封装到了pageTeacher里面

        List<Article> records = pageParam.getRecords();

        long current = pageParam.getCurrent(); //当前页
        long pages = pageParam.getPages();
        long size = pageParam.getSize();
        long total = pageParam.getTotal();
        boolean hasNext = pageParam.hasNext(); //下一页
        boolean hasPrevious = pageParam.hasPrevious();  //上一页

        HashMap<String, Object> map = new HashMap<>(); //将上面的数据封装到map中返回给前端
        map.put("items", records); //每个讲师数据 list
        map.put("current", current); //当前页
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);

        return map;
    }
}
