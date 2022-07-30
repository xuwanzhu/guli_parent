package com.atguigu.eduorder.service;

import com.atguigu.eduorder.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author atguigu
 * @since 2022-03-20
 */
public interface OrderService extends IService<Order> {

    //1、根据课程id和用户id创建订单，返回订单id
    String saveOrder(String courseId, String memberId);

    //2、//跟新course表的购买量数据
    void updateCourseBuy(String courseId);
}
