package com.atguigu.staservice.service;

import com.atguigu.staservice.entity.StatisticsDaily;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务类
 * </p>
 *
 * @author atguigu
 * @since 2022-03-27
 */
public interface StatisticsDailyService extends IService<StatisticsDaily> {

    //1、服务调用，统计每天注册人数
    void createStatisticsByDay(String day);

    //2、//2、显示图表
    Map<String, Object> getChartData(String begin, String end, String type);
}
