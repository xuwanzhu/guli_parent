package com.atguigu.eduorder.client;

import com.atguigu.commonutils.ordervo.UcenterMemberOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Component
@FeignClient("service-ucenter")
public interface UcenterClient {
    //2根据用户id获取用户信息
    @PostMapping("/educenter/member/getUcenterInfoForOrder/{id}") //controller层全路径
    public UcenterMemberOrder getUcenterInfoForOrder(@PathVariable("id") String id);
}
