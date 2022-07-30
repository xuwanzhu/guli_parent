package com.atguigu.eduorder.mapper;

import com.atguigu.eduorder.entity.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 订单 Mapper 接口
 * </p>
 *
 * @author atguigu
 * @since 2022-03-20
 */
public interface OrderMapper extends BaseMapper<Order> {

    //更新course表的购买量数据
    void updateCourseBuy(String courseId);
}
