package com.atguigu.oss.service.impl;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.CannedAccessControlList;
import com.atguigu.oss.service.OssService;
import com.atguigu.oss.utils.ConstantPropertiesUtil; //自定义工具类
import com.atguigu.servicebase.config.exceptionhandler.GuliException;
import io.swagger.annotations.Api;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@Api(description = "上传文件")
public class OssServiceImpl implements OssService {

    @Override
    public String uploadFileAvatar(MultipartFile file) {
        //MultipartFile 这个类一般是用来接受前台传过来的文件
        //获取上传文件 MultipartFile file

        //获取阿里云存储相关常量
        String endPoint = ConstantPropertiesUtil.END_POINT;
        String accessKeyId = ConstantPropertiesUtil.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtil.ACCESS_KEY_SECRET;
        String bucketName = ConstantPropertiesUtil.BUCKET_NAME;

        String url = null;

        try {
            //判断oss实例是否存在：如果不存在则创建，如果存在则获取
            OSSClient ossClient = new OSSClient(endPoint, accessKeyId, accessKeySecret);
            if (!ossClient.doesBucketExist(bucketName)) {
                //创建bucket
                ossClient.createBucket(bucketName);
                //设置oss实例的访问权限：公共读
                ossClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
            }

            //获取上传文件流
            InputStream inputStream = file.getInputStream();

            //文件名：uuid.扩展名
            //获取文件名
            String fileName = file.getOriginalFilename();

            //UUID生成唯一的文件名称
            //hu32-ffs3-fsf3
            String uuid = UUID.randomUUID().toString().replace("-","");

            //文件名
            fileName = uuid+fileName;

            //2、把文件按日期进行分类
            //  2022/1/18/01.jpg
            String datePath = new DateTime().toString("yyyy/MM/dd");

            //最终文件名
            fileName = datePath+"/"+fileName;

            //文件上传至阿里云
            ossClient.putObject(bucketName, fileName, inputStream);
            // 关闭OSSClient。
            ossClient.shutdown();

            //获取url地址
            url = "http://" + bucketName + "." + endPoint + "/" + fileName;
            return url;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
