package com.atguigu.educenter.service.impl;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.MD5;
import com.atguigu.educenter.entity.UcenterMember;
import com.atguigu.educenter.entity.vo.RegisterVo;
import com.atguigu.educenter.mapper.UcenterMemberMapper;
import com.atguigu.educenter.service.UcenterMemberService;
import com.atguigu.servicebase.config.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2022-03-13
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    //1、登录
    @Override
    public String login(UcenterMember ucenterMember) {
        //1、获取传过来的 手机号和密码
        String mobile = ucenterMember.getMobile();
        String password = ucenterMember.getPassword();

        //3、校验手机号或者密码是否有为空的
        if(StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)) {
            throw new GuliException(20001,"手机号或者密码为空!");
        }

        //4、判断手机号是否正确
        // member为会员对象
        UcenterMember mobileMember = baseMapper.selectOne(new QueryWrapper<UcenterMember>().eq("mobile", mobile));
        //判断根据手机号查询的对象是否为空
        if(null == mobileMember) {
            throw new GuliException(20001,"手机号输入有错!");
        }

        //5、校验密码
        //因为存储到数据库的密码是加密的
        //所以需要把前端输入的密码加密 再和数据库的密码进行比较
        if(!MD5.encrypt(password).equals(mobileMember.getPassword())) {
            throw new GuliException(20001,"密码输入有错!");
        }
        //6、校验是否被禁用
        if(mobileMember.getIsDisabled()) {
            throw new GuliException(20001,"账号已被禁用!");
        }
        //7、使用JWT生成token字符串
        String jwtToken = JwtUtils.getJwtToken(mobileMember.getId(), mobileMember.getNickname()); //将用户id 和昵称 存到token中

        //8、TODO 生成一个token代表有一个用户登录了
        return jwtToken;
    }


    //2、注册
    @Override
    public void register(RegisterVo registerVo) {
        //1、获取注册信息，进行校验
        String nickname = registerVo.getNickname();
        String mobile = registerVo.getMobile();
        String password = registerVo.getPassword();
        String code = registerVo.getCode(); //验证码

        //2、判断参数是否为空
        if(StringUtils.isEmpty(nickname) ||
                StringUtils.isEmpty(mobile) ||
                StringUtils.isEmpty(password) ||
                StringUtils.isEmpty(code)) {
            throw new GuliException(20001,"输入参数为空!");
        }
        //3、校验校验验证码
        //从redis获取发送的验证码
        String mobleCode = redisTemplate.opsForValue().get(mobile);
        if (mobleCode.isEmpty()){
            System.out.println("redis验证码已过期。。。。。");
        }
        if(!code.equals(mobleCode)) { //判断前台输入的验证码 是否和 redis中的一致
            throw new GuliException(20001,"验证码输入错误!");
        }

        //4、查询数据库中是否存在相同的手机号码
        Integer count = baseMapper.selectCount(new QueryWrapper<UcenterMember>().eq("mobile", mobile));
        if(count.intValue() > 0) {
            throw new GuliException(20001,"手机号已注册!");
        }

        //5、添加注册信息到数据库
        UcenterMember member = new UcenterMember();
        member.setNickname(nickname);
        member.setMobile(registerVo.getMobile());
        member.setPassword(MD5.encrypt(password));//将密码进行加密后存储
        member.setIsDisabled(false);
        //设置默认头像
        member.setAvatar("https://online-teach-file.oss-cn-beijing.aliyuncs.com/teacher/2019/10/30/b8aa36a2-db50-4eca-a6e3-cc6e608355e0.png");
        baseMapper.insert(member);
    }

    //3、根据openid,判断微信扫描人 是否已注册
    @Override
    public UcenterMember getByOpenid(String openid) {
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("openid",openid);
        UcenterMember member = baseMapper.selectOne(wrapper);
        return member;
    }

    //5、根据日期，统计某一天的注册人数
    @Override
    public Integer countRegisterByDay(String day) {
        return baseMapper.countRegisterByDay(day);
    }
}
