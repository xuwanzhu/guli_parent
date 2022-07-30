package com.atguigu.question.service.impl;

import com.atguigu.question.entity.Question;
import com.atguigu.question.entity.QuestionVo;
import com.atguigu.question.mapper.QuestionMapper;
import com.atguigu.question.service.QuestionService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 问答 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2022-04-18
 */
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements QuestionService {

    //1、分页问答列表
    @Override
    public Map<String, Object> getPageQuestionList(Page<Question> pageParam, QuestionVo questionVo) {
        QueryWrapper<Question> wrapper = new QueryWrapper<>();
        //wrapper.eq("is_delete",1); 直接物理删
        if (!StringUtils.isEmpty(questionVo.getHotSort())) { //按热门 即最多回复排序
            wrapper.orderByDesc("more_answer");
        }
        if (!StringUtils.isEmpty(questionVo.getTimeSort())) {//按最新排序
            wrapper.orderByDesc("gmt_create");
        }
        if (!StringUtils.isEmpty(questionVo.getWaitSort())) {  //按等待回答 即0回复排序
            wrapper.eq("more_answer",0); //回复数量等于0
        }

        baseMapper.selectPage(pageParam,wrapper); //获取到的数据都封装到了pageTeacher里面

        List<Question> records = pageParam.getRecords();

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


    //2、问答管理的用户分页问答列表
    @Override
    public Map<String, Object> getPageUserQuestionList(Page<Question> pageParam, String memberId) {
        QueryWrapper<Question> wrapper = new QueryWrapper<>();
        //wrapper.eq("is_delete",1);
        wrapper.eq("member_id",memberId); //查询指定用户的文章
        //wrapper.orderByDesc("gmt_create");//按最新排序

        baseMapper.selectPage(pageParam,wrapper); //获取到的数据都封装到了pageTeacher里面

        List<Question> records = pageParam.getRecords();

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

    //3、定时更新问答的回复数量
    @Override
    public void updateAnswerForQuestion() {
        List<Question> questionList = baseMapper.selectList(null);//问答数据列表
        for(Question question:questionList){
            String questionId = question.getId();
            System.out.println("定时更新问答=>"+questionId);
            baseMapper.updateAnswerForQuestion(questionId);
        }

    }
}
