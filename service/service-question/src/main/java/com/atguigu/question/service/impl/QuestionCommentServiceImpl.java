package com.atguigu.question.service.impl;

import com.atguigu.question.entity.QuestionComment;
import com.atguigu.question.mapper.QuestionCommentMapper;
import com.atguigu.question.service.QuestionCommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 问答评论 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2022-04-19
 */
@Service
public class QuestionCommentServiceImpl extends ServiceImpl<QuestionCommentMapper, QuestionComment> implements QuestionCommentService {

}
