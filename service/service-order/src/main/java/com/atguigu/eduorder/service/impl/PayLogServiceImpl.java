package com.atguigu.eduorder.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.eduorder.entity.Order;
import com.atguigu.eduorder.entity.PayLog;
import com.atguigu.eduorder.mapper.PayLogMapper;
import com.atguigu.eduorder.service.OrderService;
import com.atguigu.eduorder.service.PayLogService;
import com.atguigu.eduorder.utils.HttpClient;
import com.atguigu.servicebase.config.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 课程支付 服务实现类
 */
@Service
public class PayLogServiceImpl extends ServiceImpl<PayLogMapper, PayLog> implements PayLogService {

    @Autowired
    private OrderService orderService;

    //1、生成微信支付二维码
    @Override
    public Map createNative(String orderNo) {

        try {
            //1、根据订单号查询获取订单信息
            QueryWrapper<Order> wrapper = new QueryWrapper<>();
            wrapper.eq("order_no",orderNo);
            Order order = orderService.getOne(wrapper);

            Map m = new HashMap();
            //2、使用map设置生成二维码需要参数
            //都是一些固定值
            m.put("appid", "wx74862e0dfcf69954");  //关联的公众号appid
            m.put("mch_id", "1558950191");  //商户号
            m.put("nonce_str", WXPayUtil.generateNonceStr());
            m.put("body", order.getCourseTitle()); //课程标题
            m.put("out_trade_no", orderNo); //订单号
            m.put("total_fee", order.getTotalFee().multiply(new BigDecimal("100")).longValue()+""); //价格
            m.put("spbill_create_ip", "127.0.0.1"); //支付的ip地址，可以改为自己域名  TODO
            m.put("notify_url", "http://guli.shop/api/order/weixinPay/weixinNotify\n"); //回调地址
            m.put("trade_type", "NATIVE"); //支付类型


            //3、发送HTTPClient请求，传递xml格式参数，请求地址为微信提供的固定地址
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            //client设置参数
            client.setXmlParam(WXPayUtil.generateSignedXml(m, "T6m9iK73b0kn9g5v426MKfHQH7X8rKwb")); //商户key
            client.setHttps(true); //支持https访问
            client.post(); //执行请求发送

            //4、得到发送请求，返回结果
            String xml = client.getContent(); //返回的内容是xml格式
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml); //把xml格式转换map集合，用map集合返回

            //5、封装返回结果集
            Map map = new HashMap<>();
            map.put("out_trade_no", orderNo);
            map.put("course_id", order.getCourseId());
            map.put("total_fee", order.getTotalFee());
            map.put("result_code", resultMap.get("result_code")); //返回二维码操作状态码 200 ,get中的“result_code、code_url”是固定的
            map.put("code_url", resultMap.get("code_url")); //支付二维码地址

            //微信支付二维码2小时过期，可采取2小时未支付取消订单
            //redisTemplate.opsForValue().set(orderNo, map, 120,TimeUnit.MINUTES);
            return map;
        }catch (Exception e){
            throw new GuliException(20001,"生成微信支付二维码失败!");
        }


    }


    //2、根据订单号查询订单支付状态
    @Override
    public Map<String, String> queryPayStatus(String orderNo) {
        try {
            //1、封装参数
            Map m = new HashMap<>();
            m.put("appid", "wx74862e0dfcf69954");
            m.put("mch_id", "1558950191");
            m.put("out_trade_no", orderNo);
            m.put("nonce_str", WXPayUtil.generateNonceStr());

            //2、发送httpclient请求
            //HTTPClient来根据URL访问第三方接口并且传递参数
            HttpClient client = new
                    HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            client.setXmlParam(WXPayUtil.generateSignedXml(m,
                    "T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));
            client.setHttps(true);
            client.post();

            //3、返回第三方的数据
            String xml = client.getContent();
            //4、返回记过为xml格式，xml格式转成Map并返回
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);

            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    //3、向支付表添加记录，和更新订单状态
    @Override
    public void updateOrderStatus(Map<String, String> map) {

        //从map中获取订单号
        String orderNo = map.get("out_trade_no");
        //根据订单号 查询订单信息
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no",orderNo);
        Order order = orderService.getOne(wrapper);

        //更新订单支付状态 ，1为已支付
        if(order.getStatus().intValue() == 1) return;
        order.setStatus(1);
        orderService.updateById(order);


        //向支付表添加支付记录，记录支付日志
        PayLog payLog=new PayLog();

        payLog.setOrderNo(order.getOrderNo());//支付订单号
        payLog.setPayTime(new Date());
        payLog.setPayType(1);//支付类型
        payLog.setTotalFee(order.getTotalFee());//总金额(分)
        payLog.setTradeState(map.get("trade_state"));//支付状态
        payLog.setTransactionId(map.get("transaction_id"));
        payLog.setAttr(JSONObject.toJSONString(map));
        baseMapper.insert(payLog);//插入到支付日志表

    }
}
