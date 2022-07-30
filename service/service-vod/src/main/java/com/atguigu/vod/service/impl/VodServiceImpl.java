package com.atguigu.vod.service.impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.atguigu.servicebase.config.exceptionhandler.GuliException;
import com.atguigu.vod.Utils.ConstantPropertiesUtil;
import com.atguigu.vod.Utils.InitVideoClient;
import com.atguigu.vod.service.VodService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@Service
public class VodServiceImpl implements VodService {


    @Override
    //1、上传视频到阿里云
    public String uploadVideo(MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename(); //上传文件的原始名称

            //从fileName中截取名称给title命名
            String title = fileName.substring(0, fileName.lastIndexOf(".")); //上传阿里云之后视频显示的名称

            InputStream inputStream = file.getInputStream();  //上传文件输入流

            UploadStreamRequest request = new UploadStreamRequest(ConstantPropertiesUtil.ACCESS_KEY_ID, ConstantPropertiesUtil.ACCESS_KEY_SECRET, title, fileName, inputStream);

            System.out.println("id"+ConstantPropertiesUtil.ACCESS_KEY_ID);
            System.out.println("scret"+ConstantPropertiesUtil.ACCESS_KEY_SECRET);

            UploadVideoImpl uploader = new UploadVideoImpl();
            UploadStreamResponse response = uploader.uploadStream(request);

            String videoId = null;
            if (response.isSuccess()) {
                videoId = response.getVideoId();
                System.out.print("====>>>>>>VideoId=>>>" +videoId+ "\n");
            } else {
                System.out.print("上传视频出错!");
            }
            return videoId;
        }catch (Exception e){
            e.printStackTrace();
        throw new GuliException(20001, "服务上传视频失败");
    }
    }


    //2、删除阿里云端视频
    @Override
    public void removeVideo(String videoId) {
        try {
            //初始化对象
            DefaultAcsClient client = InitVideoClient.initVodClient(ConstantPropertiesUtil.ACCESS_KEY_ID, ConstantPropertiesUtil.ACCESS_KEY_SECRET);
            //创建删除视频requesr对象
            DeleteVideoRequest request = new DeleteVideoRequest();
            //向删除对象设置id
            request.setVideoIds(videoId);

            //调用初始化对象的方法 实现删除
           client.getAcsResponse(request);

        }catch (Exception e){
            e.printStackTrace();
            throw new GuliException(20001, "删除视频失败!");
        }
    }


    //3、批量删除视频   videoIdList为视频id集合
    @Override
    public void removeVideoList(List<String>  videoIdList) {
        try {
            //初始化对象
            DefaultAcsClient client = InitVideoClient.initVodClient(ConstantPropertiesUtil.ACCESS_KEY_ID, ConstantPropertiesUtil.ACCESS_KEY_SECRET);
            //创建删除视频requesr对象
            DeleteVideoRequest request = new DeleteVideoRequest();
            //向删除对象设置id
            //一次只能批量删20个
            //将list集合中的id设置成用 1,2,3,4,5用逗号分割的
            String str = org.apache.commons.lang.StringUtils.join(videoIdList.toArray(), ",");
            request.setVideoIds(str); //多个id用 逗号 分割

            //调用初始化对象的方法 实现删除
            client.getAcsResponse(request);

        }catch (Exception e){
            e.printStackTrace();
            throw new GuliException(20001, "批量删除视频失败!");
        }
    }


}
