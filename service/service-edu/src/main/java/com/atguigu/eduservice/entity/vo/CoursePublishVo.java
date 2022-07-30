package com.atguigu.eduservice.entity.vo;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import java.io.Serializable;

/*
* 课程信息最终发布实体类
* */
@ApiModel(value = "课程发布信息")
@Data
public class CoursePublishVo implements Serializable {
    private  String id;
    private String title;
    private String cover;
    private Integer lessonNum;
    private String subjectLevelOne;
    private String subjectLevelTwo;
    private String teacherName;
    private String price;//只用于显示
}
