package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.client.VodClient;
import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.entity.subject.OneSubject;
import com.atguigu.eduservice.service.EduVideoService;
import com.atguigu.servicebase.config.exceptionhandler.GuliException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(description = "课程小节管理")
@RestController
@RequestMapping("/eduservice/video")
//@CrossOrigin //跨域
public class EduVideoController {

    @Autowired
    private EduVideoService eduVideoService;

    @Autowired
    private VodClient vodClient;

    //1、添加小节
    @ApiOperation(value = "添加小节")
    @PostMapping("addVideoInfo")
    public R addVideoInfo(@RequestBody EduVideo eduVideo){
        boolean save = eduVideoService.save(eduVideo);
        if(save){
            return R.ok().message("添加成功!");
        }else {
        return R.error().message("添加失败!");
        }
    }

    //2、修改小节
    @ApiOperation(value = "修改小节")
    @PostMapping("updateVideo")
    public R updateVideo(@RequestBody EduVideo eduVideo){
        boolean result = eduVideoService.updateById(eduVideo);
        if(result){
            return R.ok().message("修改成功!");
        }else {
            return R.error().message("修改失败!");
        }
    }

    //3、删除小节
    @ApiOperation(value = "删除小节")
    @DeleteMapping("/deleteVideo/{videoId}")
    public R deleteVideo(@ApiParam(name = "videoId", value = "小节ID", required = true)
                             @PathVariable String videoId){
        //1、删除视频
        //根据小节id获取到 视频id
        EduVideo eduVideo = eduVideoService.getById(videoId); //通过小节id获取到该对象
        String videoSourceId = eduVideo.getVideoSourceId();
        //判断videoSourceId是否为空
        if (!StringUtils.isEmpty(videoSourceId)){
            //根据id,实现调用远程服务 对视频进行删除
            R result = vodClient.removeVideoById(videoSourceId);
            if (result.getCode() == 20001) {
                throw new GuliException(20001,"删除视频失败!,熔断器！！！");
            }

        }

        //2、删除小节
        boolean result = eduVideoService.removeById(videoId);
        if(result){
            return R.ok().message("删除成功!");
        }else {
            return R.error().message("删除失败!");
        }
    }

    //4、根据章节id查询单个章节数据
    @ApiOperation(value = "根据ID查询单个小节")
    @GetMapping("/getOneVideo/{videoId}")
    public R getOneChapter(@PathVariable String videoId){
        EduVideo video = eduVideoService.getById(videoId);
        return R.ok().data("items",video);
    }

}

