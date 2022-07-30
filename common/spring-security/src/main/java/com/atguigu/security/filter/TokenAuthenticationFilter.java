package com.atguigu.security.filter;

import com.atguigu.commonutils.R;
import com.atguigu.commonutils.ResponseUtil;
import com.atguigu.security.security.TokenManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * 访问过滤器 授权过滤器
 *
 * 访问对应的service-acl模块中的controller相关方法之前，会先经过SpringSecurity中定义的访问过滤器。
 *
 */
public class TokenAuthenticationFilter extends BasicAuthenticationFilter {
    private TokenManager tokenManager; // token生成工具类
    private RedisTemplate redisTemplate; //进行redis操作

    public TokenAuthenticationFilter(AuthenticationManager authManager, TokenManager tokenManager,RedisTemplate redisTemplate) {
        super(authManager);
        this.tokenManager = tokenManager;
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        // spring . security加载service_acl模块上面，那就只会接收匹配断言/* /acl/**的请求，
        // 并且service_acl模块上面都是/admin/开头的，所以访间service_acl的每一个请求都会来到这里，不过if拦不住它，毕竟indexof ( "admin")大于o
        logger.info("================="+req.getRequestURI());
        // 判断是不是admin，超级管理员用户，如是，直接通过。
        if(req.getRequestURI().indexOf("admin") == -1) {
            chain.doFilter(req, res);
            return;
        }

        //不是admin超级管理员用户
        UsernamePasswordAuthenticationToken authentication = null;
        try {
            // 根据请求获取用户权限
            ///获取当前认证成功用户权限信息
            authentication = getAuthentication(req);
        } catch (Exception e) {
            ResponseUtil.out(res, R.error());
        }

        // 权限不为空
        if (authentication != null) {
            // 设置权限
            ////判断如果有权限信息，放到权限上下文中    授权？
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            ResponseUtil.out(res, R.error());
        }

        chain.doFilter(req, res); //验证通过放行
    }



    //授权？？？权限：即角色权限
    //通过token获取redis存储的菜单列表，判断当前用户是否有操作所点击菜单的权限，有权限就进行操作，否则提示没有权限
    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        // token置于header里
        String token = request.getHeader("token");

        if (token != null && !"".equals(token.trim())) {
            //获取用户名称
            String userName = tokenManager.getUserFromToken(token);

            //从redis中取出权限值列表之后在使用simpleGrantedAuthority进行封装
            List<String> permissionValueList = (List<String>) redisTemplate.opsForValue().get(userName);

            Collection<GrantedAuthority> authorities = new ArrayList<>();
            // 权限封装
            for(String permissionValue : permissionValueList) {
                if(StringUtils.isEmpty(permissionValue)) continue; //如果权限为空
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(permissionValue);
                authorities.add(authority);
            }

            //如果名称不是null，那就把UsernamePasswordAuthenticationToken返回
            // 用户存在权限，给用户添加权限并返回。
            if (!StringUtils.isEmpty(userName)) {
                return new UsernamePasswordAuthenticationToken(userName, token, authorities);
            }
            return null;
        }
        return null;
    }
}