package com.atguigu.question.scheduled;

/*
* 定时任务 : 定时更新每条问答的回复数
*
* */

import com.atguigu.question.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTask {
    @Autowired
    private QuestionService questionService;

    //定时 每30分钟执行一次
    @Scheduled(cron = "0 0/2 * * * ?")
    public void task3() {
        System.out.println("=======问答开始：定时任务====");

        questionService.updateAnswerForQuestion(); //定时更新问答的回复数量
        System.out.println("=======问答结束：定时任务=======");
    }












    /**
     * 测试
     * 每五秒执行一次
     @Scheduled(cron = "0/5 * * * * ?")
     public void task1() {
     //System.out.println("*********++++++++++++*****执行了");
     }*/


    /**
     * 每天凌晨1点执行定时
    @Scheduled(cron = "0 0 1 * * ?")
    public void task2() {
        //获取上一天的日期
        //String day = DateUtil.formatDate(DateUtil.addDays(new Date(), -1));
       // dailyService.createStatisticsByDay(day);
    }*/
}
