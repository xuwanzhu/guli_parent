package com.atguigu.oss.controller;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.atguigu.commonutils.R;
import com.atguigu.oss.service.OssService;
import com.atguigu.oss.utils.ConstantPropertiesUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLDecoder;
import java.util.Map;

@Api(description = "阿里云文件管理")
//@CrossOrigin //跨域
@RestController
@RequestMapping("/eduoss/fileoss")
public class OssController {

    @Autowired
    private OssService ossService;

    /*
    * 文件上传
    * */

    @ApiOperation(value = "文件上传")
    @PostMapping("upload")
    public R upload(@ApiParam(name = "file", value = "文件", required = true)
                        @RequestParam("file") MultipartFile file){
        //MultipartFile 这个类一般是用来接受前台传过来的文件
        //获取上传文件 MultipartFile file

        //返回上传文件的网址路径
        String url = ossService.uploadFileAvatar(file);
        return R.ok().data("url", url);
    }


    @ApiOperation(value = "删除文件")
    @PostMapping("delete")
    public boolean delete(@RequestBody Map<String,String> map ){

        String url = map.get("url");


        try {
            //对获取到的url进行解码
            url = URLDecoder.decode(url,"utf-8");
        } catch (Exception e){
            e.printStackTrace();
        }
        //System.out.println("=========="+url);

        //获取阿里云存储相关常量
        String endPoint = ConstantPropertiesUtil.END_POINT;
        String accessKeyId = ConstantPropertiesUtil.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtil.ACCESS_KEY_SECRET;
        String bucketName = ConstantPropertiesUtil.BUCKET_NAME;

        //url = url.replace("https://edu-guli-project-oss.oss-cn-shenzhen.aliyuncs.com/", "");
        //int index = url.indexOf("?");
        //url = url.substring(0, index);
        //System.out.println("拆分后的路径: "+url);

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endPoint, accessKeyId, accessKeySecret);

        // 删除文件或目录。如果要删除目录，目录必须为空。
        ossClient.deleteObject(bucketName, url);
        ossClient.shutdown();

        //删除成功
        return true;
    }




}
