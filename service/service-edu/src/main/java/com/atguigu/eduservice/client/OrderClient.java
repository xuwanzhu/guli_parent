package com.atguigu.eduservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "service-order",fallback = OrderClientImpl.class)
@Component
public interface OrderClient {
    //判断课程是否购买过
    @GetMapping("/eduorder/order/isBuyCourse/{memberid}/{courseId}") //全路径
    public boolean isBuyCourse(@PathVariable("memberid") String memberid, @PathVariable("courseId") String courseId);
}
