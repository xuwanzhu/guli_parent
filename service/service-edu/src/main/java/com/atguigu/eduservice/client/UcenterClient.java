package com.atguigu.eduservice.client;

import com.atguigu.commonutils.vo.UcenterMemberVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "service-ucenter",fallback = UcenterClientImpl.class)
@Component
public interface UcenterClient {
    //根据用户id获取用户信息
    @PostMapping("/educenter/member/getInfoUc/{id}") //全路径
    public UcenterMemberVo getInfoUc(@PathVariable("id") String id); //方法名等必须和生产者端(service-ucenter中controller层) 一样

    //public UcenterMemberVo getInfoUc(@PathVariable String id); //方法名等必须和生产者端(service-ucenter中controller层) 一样

}
