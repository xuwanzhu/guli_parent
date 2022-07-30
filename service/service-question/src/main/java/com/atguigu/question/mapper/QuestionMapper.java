package com.atguigu.question.mapper;

import com.atguigu.question.entity.Question;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 问答 Mapper 接口
 * </p>
 *
 * @author atguigu
 * @since 2022-04-18
 */
public interface QuestionMapper extends BaseMapper<Question> {

    //1、定时更新问答的回复数量
    void updateAnswerForQuestion(String questionId);
}
