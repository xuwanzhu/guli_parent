package com.atguigu.educenter.mapper;

import com.atguigu.educenter.entity.UcenterMember;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 会员表 Mapper 接口
 * </p>
 *
 * @author atguigu
 * @since 2022-03-13
 */
public interface UcenterMemberMapper extends BaseMapper<UcenterMember> {

    //根据日期，统计某一天的注册人数
    Integer countRegisterByDay(String day);
}
