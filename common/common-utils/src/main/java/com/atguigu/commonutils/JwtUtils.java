package com.atguigu.commonutils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * JWT 工具类
 */
public class JwtUtils {

    //两个常量
    public static final long EXPIRE = 1000 * 60 * 60 * 24;  //设置token 过期时间
    public static final String APP_SECRET = "ukc5BgbRigdDaY6jZFfWud2jZVLGHK";  //密钥，可自己随便定义

    //生成token字符串的方法
    public static String getJwtToken(String id, String nickname){ //用户id 和名字，可自己设置多个属性

        String JwtToken = Jwts.builder()
                //头信息部分
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")

                .setSubject("guli-user")
                .setIssuedAt(new Date()) //设置过期时间
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))

                //设置token主体部分，存储用户信息，可设置多个用户信息
                .claim("id", id)
                .claim("nickname", nickname)

                //签名哈希
                .signWith(SignatureAlgorithm.HS256, APP_SECRET)
                .compact();

        return JwtToken;
    }

    /**
     * 判断token是否存在与有效
     * @param jwtToken
     * @return
     */
    public static boolean checkToken(String jwtToken) {
        if(StringUtils.isEmpty(jwtToken)) return false;
        try {
            Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 判断token是否存在与有效
     * @param request
     * @return
     */
    public static boolean checkToken(HttpServletRequest request) {
        try {
            String jwtToken = request.getHeader("token");
            if(StringUtils.isEmpty(jwtToken)) return false;
            Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 根据token字符串获取会员id，也可获取用户的其他信息
     * @param request
     * @return
     */
    public static String getMemberIdByJwtToken(HttpServletRequest request) {
        String jwtToken = request.getHeader("token"); //获取头信息
        if(StringUtils.isEmpty(jwtToken))
            return "";
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken); //解析
        Claims claims = claimsJws.getBody();
        return (String)claims.get("id");//也可获取用户的其他信息
    }
}
