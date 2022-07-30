package com.atguigu.educenter.service;

import com.atguigu.educenter.entity.UcenterMember;
import com.atguigu.educenter.entity.vo.RegisterVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author atguigu
 * @since 2022-03-13
 */
public interface UcenterMemberService extends IService<UcenterMember> {

    //1、登录
    String login(UcenterMember ucenterMember);

    //2、注册
    void register(RegisterVo registerVo);

    //3、判断微信扫描人 是否已注册
    UcenterMember getByOpenid(String openid);

    //5、根据日期，统计某一天的注册人数
    Integer countRegisterByDay(String day);
}
