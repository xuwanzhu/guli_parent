package com.atguigu.vod.controller;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.atguigu.commonutils.R;
import com.atguigu.servicebase.config.exceptionhandler.GuliException;
import com.atguigu.vod.Utils.ConstantPropertiesUtil;
import com.atguigu.vod.Utils.InitVideoClient;
import com.atguigu.vod.service.VodService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Api(description="阿里云视频点播微服务")
//@CrossOrigin //跨域
@RestController
@RequestMapping("/eduvod/video")
public class VodController {

    @Autowired
    private VodService vodService;


    @ApiOperation(value = "上传视频")
    @PostMapping("uploadVideo")
    public R uploadVideo(MultipartFile file){
        String videoId = vodService.uploadVideo(file);
        return R.ok().data("videoId",videoId);
    }

    @ApiOperation(value = "删除单个云端视频")
    @DeleteMapping("/removeVideo/{videoId}")
    public R removeVideoById(@PathVariable String videoId){
        vodService.removeVideo(videoId);
        return R.ok().message("视频删除成功!");
    }

    @ApiOperation(value = "批量删除视频")
    @DeleteMapping("deleteBatch")
    public R deleteBatch(@ApiParam(name = "videoIdList", value = "云端视频id集合", required = true)
                             @RequestParam("videoIdList") List<String>  videoIdList) // RequestParam注解对传入参数指定为videoIdList，如果前端不传videoIdList参数名，会报错
    {
        vodService.removeVideoList(videoIdList);
        return R.ok().message("视频删除成功!");
    }


    //1、视频播放：通过视频id和凭证播放视频
    @ApiOperation(value = "阿里云视频播放之 根据视频id获取凭证")
    @GetMapping("getVideoPlayAuth/{videoId}")
    public R getVideoPlayAuth (@PathVariable String videoId){
        try {
            //1创建初始化对象
            DefaultAcsClient client = InitVideoClient.initVodClient(ConstantPropertiesUtil.ACCESS_KEY_ID,ConstantPropertiesUtil.ACCESS_KEY_SECRET);

            //2创建获取凭证request和response对象
            GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();

            //3向request设置视频id
            request.setVideoId(videoId);

            //4调用方法得到凭证
            GetVideoPlayAuthResponse response = client.getAcsResponse(request);
            String playAuth = response.getPlayAuth();

            //5、TODO 每获取一次视频播放凭证代表视频播放数+1
            return R.ok().data("playAuth",playAuth);
        }catch (Exception e){
            throw new GuliException(20001,"获取视频凭证失败!");
        }
    }


}
