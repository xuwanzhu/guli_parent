package com.atguigu.eduservice.entity.chapter;

import io.swagger.annotations.ApiModel;
import io.swagger.models.auth.In;
import lombok.Data;

import java.io.Serializable;

//小节
@ApiModel(value = "课时基本信息", description = "编辑课时小节基本信息的表单对象")
@Data
public class VideoVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String title;
    // TODO 原来类型：private Boolean free; 更改为以下:
    private Integer isFree;  //判断小节是否免费

    private String videoSourceId; //视频id
}
