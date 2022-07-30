package com.atguigu.eduservice.client;

import org.springframework.stereotype.Component;

@Component
public class OrderClientImpl implements OrderClient{
    @Override
    public boolean isBuyCourse(String memberid, String courseId) {
        System.out.println("服务调用，判断订单是否购买出错了");
        return false;
    }
}
