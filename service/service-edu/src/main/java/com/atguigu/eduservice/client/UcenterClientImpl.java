package com.atguigu.eduservice.client;

import com.atguigu.commonutils.vo.UcenterMemberVo;
import org.springframework.stereotype.Component;

/*
 * 当服务调用(即执行UcenterClient接口方法)出错时，即执行了熔断器，就会调用其实现类方法，报告错误
 * */
@Component
public class UcenterClientImpl implements UcenterClient{
    @Override
    public UcenterMemberVo getInfoUc(String id) {
        return null;
    }
}
