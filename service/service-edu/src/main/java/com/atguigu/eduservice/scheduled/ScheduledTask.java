package com.atguigu.eduservice.scheduled;

/*
* 定时任务 : 每天定时统计前一天的注册人数等
*
* */

import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.utils.DateUtil;
//import com.atguigu.staservice.service.StatisticsDailyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ScheduledTask {
    @Autowired
    private EduCourseService eduCourseService;

    //定时 每30分钟执行一次
    @Scheduled(cron = "0 0/30 * * * ?")
    public void task3() {
        System.out.println("=======开始：定时任务====");
        //获取上一分钟的日期
        //String day = DateUtil.formatDate(DateUtil.addMinutes(new Date(), -1));
        eduCourseService.updateBuyCountForEducourse();
        System.out.println("=======结束：定时任务=======");
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
