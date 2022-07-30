package com.atguigu.msmservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.atguigu.msmservice.service.MsmService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MsmServiceImpl implements MsmService {

    //发送验证码的方法
    @Override
    public boolean send(String phone, Map<String, Object> param) {
        DefaultProfile profile =
                DefaultProfile.getProfile("default", "你的阿里云id", "你的阿里云密钥");
        IAcsClient client = new DefaultAcsClient(profile);

        //设置一些固定参数
        CommonRequest request = new CommonRequest();
        //request.setProtocol(ProtocolType.HTTPS);
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");

        //设置发送相关的参数
        request.putQueryParameter("PhoneNumbers", phone); //要发送的手机号
        //短信服务签名名称
        request.putQueryParameter("SignName", "爱学习的汽水"); //value为短信费页面的签名名称
        //短信服务模板名称
        request.putQueryParameter("TemplateCode", "SMS_236560877");  //短信服务 模板code
        request.putQueryParameter("TemplateParam", JSONObject.toJSONString(param));//验证码  需要是json格式
        try {
            //最终发送
            CommonResponse response = client.getCommonResponse(request);
            //System.out.println(response.getData());
            return response.getHttpResponse().isSuccess(); //返回true或false,true表示验证码发送成功
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
