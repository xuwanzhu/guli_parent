package com.atguigu.eduorder.controller;


import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.eduorder.entity.Order;
import com.atguigu.eduorder.entity.vo.OrderQuery;
import com.atguigu.eduorder.service.OrderService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 订单页面 前端控制器
 *
 * @author atguigu
 * @since 2022-03-20
 */
@RestController
@RequestMapping("/eduorder/order")
//@CrossOrigin
@Api(description = "订单管理")
public class OrderController {

    @Autowired
    private OrderService orderService;

    //1、创建订单
    // 根据课程id和用户id创建订单，返回订单id
    @ApiOperation(value = "创建订单")
    @PostMapping("createOrder/{courseId}")
    public R createOrder(@PathVariable String courseId, HttpServletRequest request){

        String memberId= JwtUtils.getMemberIdByJwtToken(request); //获取用户id
        String orderId = orderService.saveOrder(courseId,memberId); //订单号
        return R.ok().data("orderId",orderId);
    }


    //2、根据订单号，查询订单
    @ApiOperation(value = "根据订单号查询某个订单")
    @GetMapping("getOrderByOrderId/{orderId}")
    public R getOrderByOrderId(@ApiParam(name = "orderId",value = "订单号",required = true)
                                   @PathVariable String orderId){
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no",orderId); //订单号
        Order order = orderService.getOne(wrapper);
        System.out.println("*************"+order.toString());
        return R.ok().data("item",order);
    }

    //3、判断课程是否已购买
    @ApiOperation(value = "根据用户id和课程id查询课程是否已购买")
    @GetMapping("/isBuyCourse/{memberid}/{courseId}")
    public boolean isBuyCourse(@ApiParam(name = "memberid",value = "会员id",required = true)
                               @PathVariable String memberid,
                                @ApiParam(name = "courseId",value = "课程id",required = true)
                                @PathVariable String courseId){
       QueryWrapper<Order> wrapper = new QueryWrapper<>();
       wrapper.eq("member_id",memberid);
       wrapper.eq("course_id",courseId);
       wrapper.eq("status",1); //1表示已支付，0表示未支付
        int count = orderService.count(wrapper);
        if (count > 0) {
            return true;
        }else {
            return false;
        }
    }


    //4、后台订单分页列表
    @ApiOperation(value = "获取订单分页列表")
    @PostMapping("pageOrderList/{page}/{limit}")
    public R pageOrderList(@ApiParam(name = "page",value = "当前页码",required = true)
                        @PathVariable Long page,
                        @ApiParam(name = "limit", value = "每页记录数", required = true)
                        @PathVariable Long limit,
                        @RequestBody(required = false) OrderQuery order){
        //1构建分页对象
        Page<Order> pageParam = new Page<>(page, limit);


        //2构建条件
        QueryWrapper<Order> wrapper = new QueryWrapper<>();

        if (order != null){
            if (!StringUtils.isEmpty(order.getTitle())) { //按课程搜索
                wrapper.like("course_title",order.getTitle());
            }
            if (!StringUtils.isEmpty(order.getOrderNo())) {//按订单号
                wrapper.eq("order_no",order.getOrderNo());
            }
            if (!StringUtils.isEmpty(order.getBegin())) {  //开始时间
                wrapper.ge("gmt_create",order.getBegin());
            }
            if (!StringUtils.isEmpty(order.getEnd())) {  //结束时间
                wrapper.le("gmt_create",order.getEnd());
            }
        }



        orderService.page(pageParam,wrapper);

        long total = pageParam.getTotal();//总记录数
        List<Order> records = pageParam.getRecords();//数据list集合
        return R.ok().data("items",records).data("total",total);
    }


    //5、删除订单
    @ApiOperation(value = "根据订单Id删除订单")
    @DeleteMapping("deleteOrder/{orderId}")
    public R deleteOrder(@PathVariable String orderId){
        boolean remove = orderService.removeById(orderId);//订单号
        if (remove){
            return R.ok();
        }else {
            return R.error();
        }
    }

    //4、前台订单分页列表
    @ApiOperation(value = "获取前台订单分页列表")
    @GetMapping("pageFrontOrderList/{page}/{limit}")
    public R pageFrontOrderList(@ApiParam(name = "page",value = "当前页码",required = true)
                           @PathVariable Long page,
                           @ApiParam(name = "limit", value = "每页记录数", required = true)
                           @PathVariable Long limit,
                           HttpServletRequest request){
        //1构建分页对象
        Page<Order> pageParam = new Page<>(page, limit);

        //1、调用jwt工具类的方法，根据request对象获取头信息，返回用户id
        String memberId = JwtUtils.getMemberIdByJwtToken(request);

        //2构建条件
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("member_id",memberId); //当前登录用户

        orderService.page(pageParam,wrapper);

        long total = pageParam.getTotal();//总记录数
        List<Order> records = pageParam.getRecords();//数据list集合
        return R.ok().data("items",records).data("total",total);
    }

}

