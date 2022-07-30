package com.atguigu.staservice.service.impl;

import com.atguigu.staservice.client.UcenterClient;
import com.atguigu.staservice.entity.StatisticsDaily;
import com.atguigu.staservice.mapper.StatisticsDailyMapper;
import com.atguigu.staservice.service.StatisticsDailyService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2022-03-27
 */
@Service
public class StatisticsDailyServiceImpl extends ServiceImpl<StatisticsDailyMapper, StatisticsDaily> implements StatisticsDailyService {

    @Autowired
    private UcenterClient ucenterClient;


    //1、服务调用，统计每天注册人数
    @Override
    public void createStatisticsByDay(String day) {
        //1删除已存在的统计对象
        QueryWrapper<StatisticsDaily> dayQueryWrapper = new QueryWrapper<>();
        dayQueryWrapper.eq("date_calculated", day);
        baseMapper.delete(dayQueryWrapper);

        //2获取统计信息
        Integer registerNum = (Integer) ucenterClient.registerCount(day).getData().get("countRegister"); //注册人数
        Integer loginNum = RandomUtils.nextInt(100, 200);//TODO 登录人数 => 用户token
        Integer videoViewNum = RandomUtils.nextInt(100, 200);//TODO 每日播放视频数 => 视频凭证playAuth
        Integer courseNum = RandomUtils.nextInt(100, 200);//TODO 每日新增课程数 => 返回一个cid代表课程+1

        //创建统计对象
        StatisticsDaily daily = new StatisticsDaily();
        daily.setRegisterNum(registerNum);
        daily.setLoginNum(loginNum);
        daily.setVideoViewNum(videoViewNum);
        daily.setCourseNum(courseNum);
        daily.setDateCalculated(day);

        //存入数据库
        baseMapper.insert(daily);
    }



    //2、显示图表
    //前端表格数据分为两部分：日期 和 数量 ，且格式为 数组格式，后端对应为list
    @Override
    public Map<String, Object> getChartData(String begin, String end, String type) {

        //1根据查询条件取得的全部数据
        QueryWrapper<StatisticsDaily> dayQueryWrapper = new QueryWrapper<>();
        dayQueryWrapper.select(type, "date_calculated");
        dayQueryWrapper.between("date_calculated", begin, end);

        List<StatisticsDaily> dayList = baseMapper.selectList(dayQueryWrapper);

        List<String> dateList = new ArrayList<String>(); //日期
        List<Integer> numDataList = new ArrayList<Integer>(); //数量

        //2给对应条件的数组赋值
        for (int i = 0; i < dayList.size(); i++) {
            StatisticsDaily daily = dayList.get(i);
            dateList.add(daily.getDateCalculated()); //给日期list赋值
            switch (type) { //给数量list赋值
                case "register_num":
                    numDataList.add(daily.getRegisterNum());
                    break;
                case "login_num":
                    numDataList.add(daily.getLoginNum());
                    break;
                case "video_view_num":
                    numDataList.add(daily.getVideoViewNum());
                    break;
                case "course_num":
                    numDataList.add(daily.getCourseNum());
                    break;
                default:
                    break;
            }
        }

        //3将两组list数据，存到map集合中
        Map<String, Object> map = new HashMap<>();
        map.put("numDataList", numDataList);
        map.put("dateList", dateList);

        return map;
    }
}
