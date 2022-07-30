package com.atguigu.educenter.controller;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.educenter.entity.UcenterMember;
import com.atguigu.educenter.service.UcenterMemberService;
import com.atguigu.educenter.utils.ConstantPropertiesUtil;
import com.atguigu.educenter.utils.HttpClientUtils;
import com.atguigu.servicebase.config.exceptionhandler.GuliException;
import com.google.gson.Gson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;

import java.net.URLEncoder;
import java.util.HashMap;

//@CrossOrigin
@Controller//只是请求地址，不需要返回数据，故不用@RestController
@RequestMapping("/api/ucenter/wx")
public class WxApiController {

    @Autowired
    private UcenterMemberService memberService;


    //点击微信注册：生成二维码和设置二维码的一些配置信息，比如扫码后跳转到哪里、授权的作用域是哪里
    @GetMapping("login")
    public String genQrConnect() {
        // 1、微信开放平台授权baseUrl，%s相当于占位符
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" + //扫码后要去的路径地址
                "&response_type=code" +
                "&scope=snsapi_login" + //应用授权作用域，网页应用目前仅填写snsapi_login即可
                "&state=%s" +  //用于保持请求和回调的状态，授权请求后原样带回给第三方。该参数可用于防止csrf攻击
                "#wechat_redirect";
        // 2、对redirectUrl 进行URLEncoder编码
        String redirectUrl = ConstantPropertiesUtil.WX_OPEN_REDIRECT_URL;
        try {
            redirectUrl = URLEncoder.encode(redirectUrl, "UTF-8"); //url编码
        } catch (UnsupportedEncodingException e) {
            throw new GuliException(20001, e.getMessage());
        }

        // 防止csrf攻击（跨站请求伪造攻击）
        //String state = UUID.randomUUID().toString().replaceAll("-", "");//一般情况下会使用一个随机数
        String state = "imhelen";//为了让大家能够使用我搭建的外网的微信回调跳转服务器，这里填写你在ngrok的前置域名
        System.out.println("state = " + state);
        // 采用redis等进行缓存state 使用sessionId为key 30分钟后过期，可配置
        //键："wechar-open-state-" + httpServletRequest.getSession().getId()
        //值：satte
        //过期时间：30分钟

        //生成二维码的url，即设置 %s里面的值
        String url = String.format(
                baseUrl,
                ConstantPropertiesUtil.WX_OPEN_APP_ID,
                redirectUrl,
                "atguigu" //state,非必须有
        );

        //二维码的路径地址
        return "redirect:" + url;
    }






    //微信注册扫码后 调用的方法
    //微信登录，获取用户信息
    @GetMapping("callback")
    public String callback(String code, String state){
        //1、得到授权临时票据code，相当于验证码
        System.out.println(code);
        System.out.println(state);
        //从redis中将state获取出来，和当前传入的state作比较
        //如果一致则放行，如果不一致则抛出异常：非法访问

        //2、拿着code请求微信固定的地址，得到两个值access_token和openid
        //向认证服务器发送请求换取access_token
        String baseAccessTokenUrl =
                "https://api.weixin.qq.com/sns/oauth2/access_token" +
                "?appid=%s" +
                "&secret=%s" +
                "&code=%s" +
                "&grant_type=authorization_code";

        //拼接三个参数：id、密钥、和code值
        String accessTokenUrl = String.format(baseAccessTokenUrl,
                ConstantPropertiesUtil.WX_OPEN_APP_ID,
                ConstantPropertiesUtil.WX_OPEN_APP_SECRET,
                code );

        String result = null;
        try {
            //4、请求这个拼接好的地址，得到返回的两个值access_token和openid
            //使用httpclient发送请求，得到返回结果
            result = HttpClientUtils.get(accessTokenUrl);
            System.out.println("accessToken=============" + result);
        } catch (Exception e) {
            throw new GuliException(20001, "获取access_token失败");
        }
        //5、从result返回值字符串中取出来两个值access_token和openid
        //把result字符串转换成map集合，方便取值，根据map里面的key获取对应值。转换map采用json转换工具：gson
        //解析json字符串
        Gson gson = new Gson();
        HashMap map = gson.fromJson(result, HashMap.class);
        String accessToken = (String)map.get("access_token");
        String openid = (String)map.get("openid");

        //6、查询数据库当前用用户是否曾经使用过微信登录
        UcenterMember member = memberService.getByOpenid(openid);
        if(member == null){ //member为空表示我们需要将数据添加到数据库，不为null则不需要添加
            System.out.println("新用户注册");
        //7、拿着得到的access_token和openid，再次访问微信提供的固定地址，从而获取到扫描人信息
            String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                    "?access_token=%s" +
                    "&openid=%s";
            //拼接两个参数
            String userInfoUrl = String.format(baseUserInfoUrl, accessToken, openid);

            //发送请求
            String resultUserInfo = null;
            try {
                resultUserInfo = HttpClientUtils.get(userInfoUrl); //得到返回的用户信息
                System.out.println("resultUserInfo==========" + resultUserInfo);
            } catch (Exception e) {
                throw new GuliException(20001, "获取用户信息失败");
            }

        //8、解析json
            HashMap<String, Object> mapUserInfo = gson.fromJson(resultUserInfo,
                    HashMap.class);
            String nickname = (String)mapUserInfo.get("nickname");
            String headimgurl = (String)mapUserInfo.get("headimgurl");

        //9、向数据库中插入一条记录
            member = new UcenterMember();
            member.setNickname(nickname);
            member.setOpenid(openid);
            member.setAvatar(headimgurl);
            memberService.save(member);
        }


        //10、扫描之后，需要在首页面显示微信信息，比如昵称或者头像
            //使用jwt根据member对象生成token字符串
        String jwtToken = JwtUtils.getJwtToken(member.getId(), member.getNickname());
            //最后：返回首页面，通过路径传递token字符串
        return "redirect:http://localhost:3000?token="+jwtToken;
    }


}