package com.atguigu.staservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.staservice.client.UcenterClient;
import com.atguigu.staservice.service.StatisticsDailyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 */
@RestController
@RequestMapping("/staservice/statistics")
@Api(description = "统计分析管理")
//@CrossOrigin
public class StatisticsDailyController {

    @Autowired
    private StatisticsDailyService statisticsDailyService;

    //1、服务调用，统计每天注册人数
    @ApiOperation(value = "统计注册人数")
    @PostMapping("createStatisticsByDate/{day}")
    public R createStatisticsByDate(@PathVariable String day) {
        statisticsDailyService.createStatisticsByDay(day);
        return R.ok();
    }


    //2、显示图表
    @ApiOperation(value = "显示图表")
    @GetMapping("showChart/{begin}/{end}/{type}")
    public R showChart(@ApiParam(name = "begin",value = "开始时间",required = true)@PathVariable String begin,
                       @ApiParam(name = "end",value = "结束时间",required = true)@PathVariable String end,
                       @ApiParam(name = "type",value = "查询类型",required = true) @PathVariable String type){
        System.out.println("*****************************"+begin+"*****"+end+"**********"+type);
        Map<String, Object> map = statisticsDailyService.getChartData(begin, end, type);
        return R.ok().data(map);
    }

}

