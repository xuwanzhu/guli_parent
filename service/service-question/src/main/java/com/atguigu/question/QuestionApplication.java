package com.atguigu.question;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling //执行定时任务
@EnableDiscoveryClient //nacos注册
@EnableFeignClients //开启feign服务调用
@MapperScan("com.atguigu.question.mapper") //不加，会报错，说扫描不到articleServiceImpl
@ComponentScan(basePackages = {"com.atguigu"})   //因为你要用common中的swagger,所以要将 service_base中的依赖，扫描进来。这里的com.atguigu，匹配的是整个项目的相同名字的包
public class QuestionApplication {
    public static void main(String[] args) {
        SpringApplication.run(QuestionApplication.class,args);
    }
}
