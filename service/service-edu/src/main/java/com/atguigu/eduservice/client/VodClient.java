package com.atguigu.eduservice.client;

import com.atguigu.commonutils.R;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "service-vod",fallback = VodFileDegradeFeignClient.class) //application配置文件中的 服务名;  fallback为执行熔断器时调用
@Component
public interface VodClient {
    /*
    *   @FeignClient注解用于指定从哪个服务中调用功能 ，名称与被调用的服务名保持一致。
        @GetMapping注解用于对被调用的微服务进行地址映射。
        @PathVariable注解一定要指定参数名称，否则出错
        @Component注解防止，在其他位置注入CodClient时idea报错
    * */
    //1、删除小节视频
    @DeleteMapping(value = "/eduvod/video/removeVideo/{videoId}") //全路径
    public R removeVideoById(@PathVariable("videoId") String videoId); //方法名等必须和生产者端(service-vod中controller层) 一样

    //2、删除课程时删除视频，批量删除视频
    @DeleteMapping("/eduvod/video/deleteBatch")
    public R deleteBatch(@RequestParam("videoIdList") List<String>  videoIdList);

}
