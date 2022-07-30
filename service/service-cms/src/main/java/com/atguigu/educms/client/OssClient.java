package com.atguigu.educms.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "service-oss",fallback = OssClientImpl.class)
@Component
public interface OssClient {
    //判断课程是否购买过
    @PostMapping("/eduoss/fileoss/delete") //全路径
    public boolean delete(@RequestBody Map<String,String> map);
}
