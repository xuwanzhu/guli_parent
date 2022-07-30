package com.atguigu.educenter.controller;


import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.MD5;
import com.atguigu.commonutils.R;
import com.atguigu.commonutils.ordervo.UcenterMemberOrder;
import com.atguigu.commonutils.vo.UcenterMemberVo;
import com.atguigu.educenter.entity.UcenterMember;
import com.atguigu.educenter.entity.vo.RegisterVo;
import com.atguigu.educenter.service.UcenterMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2022-03-13
 */
@RestController
@RequestMapping("/educenter/member")
//@CrossOrigin
@Api(description = "登录和注册")
public class UcenterMemberController {

    @Autowired
    private UcenterMemberService memberService;

    //1、登录
    @ApiOperation(value = "登录")
    @PostMapping("login")
    public R loginUser(@RequestBody UcenterMember ucenterMember){

        //调用service方法，实现登录
        //使用jwt, 返回 token, 实现单点登录
        String token = memberService.login(ucenterMember);
        return R.ok().data("token",token);
    }


    //2、注册
    @ApiOperation(value = "注册")
    @PostMapping("register")
    public R register(@RequestBody RegisterVo registerVo){
        memberService.register(registerVo);
        return R.ok();
    }


    //4 修改用户信息
    @ApiOperation(value = "修改用户信息")
    @PostMapping("update")
    public R updateUser(@RequestBody UcenterMember ucenterMember){
        ucenterMember.setPassword(MD5.encrypt(ucenterMember.getPassword()));//将密码进行加密后存储
        boolean update = memberService.updateById(ucenterMember);
        if (update) {
            return R.ok();
        }else{
            return R.error();
        }
    }

    //3、根据token获取登录信息
    @ApiOperation(value = "根据token获取登录信息")
    @GetMapping("getLoginUserInfo")
    public R getLoginUserInfo(HttpServletRequest request){
        //1、调用jwt工具类的方法，根据request对象获取头信息，返回用户id
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        //2、查询数据库，根据用户id获取用户信息
        UcenterMember loginInfo = memberService.getById(memberId);
        return R.ok().data("userInfo", loginInfo);
    }


    //4、课程评论-根据用户id   获取用户信息,根据token字符串获取用户信息
    @ApiOperation(value = "课程评论-根据token字符串获取用户信息")
    @PostMapping("getInfoUc/{id}")
    public UcenterMemberVo getInfoUc(@PathVariable String id) {
        //根据用户id获取用户信息
        UcenterMember memeber = memberService.getById(id);
        UcenterMemberVo memeberVo = new UcenterMemberVo();  //采用new Educomment，需导入service-edu模块才行，耦合度高，不推荐
        BeanUtils.copyProperties(memeber,memeberVo);//复制值
        return memeberVo;
    }

    //4、课程支付订单-根据用户id获取用户信息,根据token字符串获取用户信息
    @ApiOperation(value = "课程支付订单-根据token字符串获取用户信息")
    @PostMapping("getUcenterInfoForOrder/{id}")
    public UcenterMemberOrder getUcenterInfoForOrder(@PathVariable String id) {
        //根据用户id获取用户信息
        UcenterMember memeber = memberService.getById(id);

        UcenterMemberOrder memeberOrder = new UcenterMemberOrder();  //采用new Educomment，需导入service-edu模块才行，耦合度高，不推荐

        BeanUtils.copyProperties(memeber,memeberOrder);//复制值
        return memeberOrder;
    }

    //5、根据日期，统计某一天的注册人数
    @GetMapping(value = "registerCount/{day}")
    public R registerCount(@PathVariable String day){
        Integer count = memberService.countRegisterByDay(day);
        return R.ok().data("countRegister", count);
    }

}

