package com.atguigu.question.service;

import com.atguigu.question.entity.Question;
import com.atguigu.question.entity.QuestionVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 问答 服务类
 * </p>
 *
 * @author atguigu
 * @since 2022-04-18
 */
public interface QuestionService extends IService<Question> {

    //1、分页问答列表
    Map<String, Object> getPageQuestionList(Page<Question> pageParam, QuestionVo questionVo);

    //2、问答管理的用户分页问答列表
    Map<String, Object> getPageUserQuestionList(Page<Question> pageParam, String memberId);

    //3、定时更新问答的回复数量
    void updateAnswerForQuestion();
}
