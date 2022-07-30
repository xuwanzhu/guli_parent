package com.atguigu.eduservice.mapper;

import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.frontvo.CourseWebVo;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author atguigu
 * @since 2022-01-26
 */
public interface EduCourseMapper extends BaseMapper<EduCourse> {
    //1、
    public CoursePublishVo getCoursePublishVoById(String courseId);

    //2、前台查询课程信息和讲师信息,涉及到多表联查，故需要自己写SQL语句
    CourseWebVo selectInfoWebById(String courseId);

    //3、前台查询课程信息和讲师信息,涉及到多表联查，故需要自己写SQL语句
    CourseWebVo getCourseWebVoWebById(String courseId);

    //4、课程浏览人数+1
    void updateViewCount(String courseId);


    //定时任务
    //1、每一分钟更新一次edu_course表的购买人数数据
    void updateBuyCount(String courseId);


}
