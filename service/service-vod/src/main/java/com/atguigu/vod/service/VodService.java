package com.atguigu.vod.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VodService {
    //上传视频
    String uploadVideo(MultipartFile file);

    //通过videoId删除云端视频
    void removeVideo(String videoId);

    //根据id集合 批量删除视频
    void removeVideoList(List<String> videoIdList);
}
