package com.atguigu.security.filter;

import com.atguigu.commonutils.R;
import com.atguigu.commonutils.ResponseUtil;
import com.atguigu.security.entity.SecurityUser;
import com.atguigu.security.entity.User;
import com.atguigu.security.security.TokenManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * 登录过滤器（拦截器），继承UsernamePasswordAuthenticationFilter，对用户名密码进行登录校验
 *
 * 认证过滤器
 *
 */
public class TokenLoginFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager; //该接口的作用是对用户的未授信凭据进行认证，认证通过则返回授信状态的凭据，否则将抛出认证异常AuthenticationException
    private TokenManager tokenManager; //token生成工具类
    private RedisTemplate redisTemplate; //进行redis操作

    public TokenLoginFilter(AuthenticationManager authenticationManager, TokenManager tokenManager, RedisTemplate redisTemplate) {
        this.authenticationManager = authenticationManager;
        this.tokenManager = tokenManager;
        this.redisTemplate = redisTemplate;
        this.setPostOnly(false);
        //设置当前登录的请求地址，根据我们的项目写就可以，如果我们从前端想这个地址发送请求，那就会被当前登录拦截器拦截
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/admin/acl/login","POST"));
    }


    //根据用户名和密码进行    用户身份验证
    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
            throws AuthenticationException {
        try {
            //前端传过来的是username和password，见api下面的login.js中的login()方法，我们在后端使用user.class去接收数据
            User user = new ObjectMapper().readValue(req.getInputStream(), User.class);
            ///验证username和password，之后会进入service_acl模块下面的UserDetailsServiceImpl.java中
            //认证成功则执行下面的successfulAuthentication方法
            //System.out.println("00");
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), new ArrayList<>()));
            //执行完return 再执行service,,,,authenticationManager该接口的作用是对用户的未授信凭据进行认证，认证通过则返回授信状态的凭据，否则将抛出认证异常AuthenticationException。
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 认证成功调用的方法，
     * 登录成功
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {

        SecurityUser user = (SecurityUser) auth.getPrincipal(); //auth为认证返回的用户信息
        //1根据用户名生成token
        String token = tokenManager.createToken(user.getCurrentUserInfo().getUsername());

        //2登录成功就把key:用户名称和value:权限值列表放在redis中
        redisTemplate.opsForValue().set(user.getCurrentUserInfo().getUsername(), user.getPermissionValueList());

        //把token返回给前端
        ResponseUtil.out(res, R.ok().data("token", token));
    }

    /**
     * 验证用户身份信息失败，即登录失败
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException e) throws IOException, ServletException {
        ResponseUtil.out(response, R.error().message("账号或密码填写错误"));
    }
}
