package com.atguigu.educenter;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@EnableDiscoveryClient //nacos注册
@MapperScan("com.atguigu.educenter.mapper") //扫描mapper
@ComponentScan(basePackages = {"com.atguigu"})   //因为你要用common中的swagger,所以要将 service_base中的依赖，扫描进来。这里的com.atguigu，匹配的是整个项目的相同名字的包
@SpringBootApplication
public class UcenterApplication {
    public static void main(String[] args) {
        SpringApplication.run(UcenterApplication.class,args);
    }
}
