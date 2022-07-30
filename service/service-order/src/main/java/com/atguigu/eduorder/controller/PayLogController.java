package com.atguigu.eduorder.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduorder.entity.Order;
import com.atguigu.eduorder.entity.PayLog;
import com.atguigu.eduorder.entity.vo.OrderQuery;
import com.atguigu.eduorder.entity.vo.PayQuery;
import com.atguigu.eduorder.service.PayLogService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 支付日志表 前端控制器
 * </p>
 */

@RestController
@RequestMapping("/eduorder/paylog")
@Api(description = "支付管理")
//@CrossOrigin
public class PayLogController {

    @Autowired
    private PayLogService payService;

    //1、生成微信支付二维码
    @ApiOperation(value = "生成微信支付二维码接口")
    @GetMapping("/createNative/{orderNo}")
    public R createNative(@ApiParam(name = "orderNo",value = "订单号",required = true)
                              @PathVariable String orderNo){
        //返回信息：包含有二维码地址和其他需要的信息
        Map map = payService.createNative(orderNo);
        return R.ok().data(map);
    }

    //2、查询订单支付状态
    //参数：订单号，根据订单号查询支付状态
    @ApiOperation(value = "查询订单支付状态")
    @GetMapping("/queryPayStatus/{orderNo}")
    public R queryPayStatus(@ApiParam(name = "orderNo",value = "订单号",required = true)
                                @PathVariable String orderNo) {
        //调用查询接口，查询支付状态
        Map<String, String> map = payService.queryPayStatus(orderNo);

        if (map == null) {//出错
            return R.error().message("支付出错");
        }
        if (map.get("trade_state").equals("SUCCESS")) {
            //如果成功,添加记录到支付表，更新订单表订单状态
            payService.updateOrderStatus(map);
            return R.ok().message("支付成功");
        }
        return R.ok().code(25000).message("支付中"); //25000状态码，表示请求一直等待
    }


    //3、支付分页列表
    @ApiOperation(value = "获取支付分页列表")
    @PostMapping("pagePayLogList/{page}/{limit}")
    public R pagePayLogList(@ApiParam(name = "page",value = "当前页码",required = true)
                           @PathVariable Long page,
                           @ApiParam(name = "limit", value = "每页记录数", required = true)
                           @PathVariable Long limit,
                           @RequestBody(required = false) PayQuery payQuery){
        //1构建分页对象
        Page<PayLog> pageParam = new Page<>(page, limit);


        //2构建条件
        QueryWrapper<PayLog> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(payQuery.getOrderNo())) { //订单号
            wrapper.eq("order_no",payQuery.getOrderNo());
        }
        if (!StringUtils.isEmpty(payQuery.getTransactionNo())) {//交易流水号
            wrapper.eq("transaction_id",payQuery.getTransactionNo());
        }
        if (!StringUtils.isEmpty(payQuery.getBegin())) {  //开始时间
            wrapper.ge("gmt_create",payQuery.getBegin());
        }
        if (!StringUtils.isEmpty(payQuery.getEnd())) {  //结束时间
            wrapper.le("gmt_create",payQuery.getEnd());
        }

        payService.page(pageParam,wrapper);

        long total = pageParam.getTotal();//总记录数
        List<PayLog> records = pageParam.getRecords();//数据list集合
        return R.ok().data("items",records).data("total",total);
    }


    //4、删除支付数据
    @ApiOperation(value = "根据支付Id删除支付数据")
    @DeleteMapping("deletePayLog/{paylogId}")
    public R deletePayLog(@PathVariable String paylogId){
        boolean remove = payService.removeById(paylogId);//订单号
        if (remove){
            return R.ok();
        }else {
            return R.error();
        }
    }



}

