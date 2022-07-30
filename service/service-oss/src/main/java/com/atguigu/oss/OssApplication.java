package com.atguigu.oss;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class) //不加载数据库配置
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.atguigu"})   //因为你要用common中的swagger,所以要将 service_base中的依赖，扫描进来。这里的com.atguigu，匹配的是整个项目的相同名字的包
public class OssApplication {
    public static void main(String[] args) {
        SpringApplication.run(OssApplication.class, args);
    }
}
