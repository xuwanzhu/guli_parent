package com.atguigu.eduorder.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


//配置原因：MyBatis-plus中的Page出现返回total总为0的问题
//上面问题出现原因：原来使用分页要配置分页插件，也就是下面的这个

@Configuration
public class MybatisPlusConfig {
    /**
     * mybatis-plus分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor(){
        PaginationInterceptor page = new PaginationInterceptor();
        return page;
    }
}
