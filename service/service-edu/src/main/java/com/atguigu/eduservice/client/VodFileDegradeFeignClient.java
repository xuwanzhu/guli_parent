package com.atguigu.eduservice.client;

import com.atguigu.commonutils.R;
import org.springframework.stereotype.Component;

import java.util.List;

/*
* 当服务调用(即执行VodClient接口方法)出错时，即执行了熔断器，就会调用其实现类方法，报告错误
* */
@Component
public class VodFileDegradeFeignClient implements VodClient{

    @Override
    public R removeVideoById(String videoId) {
        return R.error().message("删除视频失败!");
    }

    @Override
    public R deleteBatch(List<String> videoIdList) {
        return R.error().message("删除多个视频失败!");
    }
}
