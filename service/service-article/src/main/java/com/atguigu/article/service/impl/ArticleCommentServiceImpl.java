package com.atguigu.article.service.impl;

import com.atguigu.article.entity.ArticleComment;
import com.atguigu.article.mapper.ArticleCommentMapper;
import com.atguigu.article.service.ArticleCommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 文章评论 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2022-04-13
 */
@Service
public class ArticleCommentServiceImpl extends ServiceImpl<ArticleCommentMapper, ArticleComment> implements ArticleCommentService {

}
