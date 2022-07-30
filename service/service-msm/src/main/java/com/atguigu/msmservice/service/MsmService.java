package com.atguigu.msmservice.service;

import java.util.Map;

public interface MsmService {
    //发送验证码的方法
    boolean send(String phone, Map<String, Object> param);
}
