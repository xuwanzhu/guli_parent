package com.atguigu.eduorder.service;

import com.atguigu.eduorder.entity.PayLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 支付日志表 服务类
 * </p>
 *
 * @author atguigu
 * @since 2022-03-20
 */
public interface PayLogService extends IService<PayLog> {

    //1、生成微信支付二维码
    Map createNative(String orderNo);

    //2、根据订单号查询订单支付状态
    Map<String, String> queryPayStatus(String orderNo);

    //3、向支付表添加记录，和更新订单状态
    void updateOrderStatus(Map<String, String> map);
}
