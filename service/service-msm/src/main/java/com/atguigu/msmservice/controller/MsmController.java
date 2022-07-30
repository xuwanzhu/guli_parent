package com.atguigu.msmservice.controller;

import com.atguigu.commonutils.R;
import com.atguigu.msmservice.service.MsmService;
import com.atguigu.msmservice.utils.RandomUtil;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/edumsm/msm")
//@CrossOrigin //跨域
@Api(description = "验证码的发送")
public class MsmController {

    @Autowired
    private MsmService msmService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    //发送验证码的方法
    @GetMapping("/send/{phone}")
    public R send(@PathVariable String phone){
        //1、从redis中获取验证码，如果获取到直接返回  (原本已经发过一次验证码)
        String code = redisTemplate.opsForValue().get(phone);
        if(!StringUtils.isEmpty(code)) return R.ok();

        //生成随机验证码值，传递给阿里云进行发送给用户
        code = RandomUtil.getFourBitRandom(); //获取四位数的验证码
        Map<String,Object> param = new HashMap<>();
        param.put("code", code);

        //调用service发送短信的方法
        boolean isSend = msmService.send(phone, param); //阿里云短信服务

        if(isSend) {
            //发送成功，把发送的验证码放redis中，并设置有效时间为1分钟
            redisTemplate.opsForValue().set(phone, code,1,TimeUnit.MINUTES);
            return R.ok();
        } else {
            return R.error().message("发送短信失败");
        }
    }
}
