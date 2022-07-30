package com.atguigu.question.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="Question前端查询条件对象", description="问答查询条件对象")
public class QuestionVo {
    @ApiModelProperty(value = "热门排序")
    private String hotSort;

    @ApiModelProperty(value = "最新时间排序")
    private String timeSort;

    @ApiModelProperty(value = "等待回答排序")
    private String waitSort;
}
