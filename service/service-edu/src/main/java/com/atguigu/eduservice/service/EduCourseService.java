package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.frontvo.CourseQueryVo;
import com.atguigu.eduservice.entity.frontvo.CourseWebVo;
import com.atguigu.eduservice.entity.vo.CourseInfoVo;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.atguigu.eduservice.entity.vo.CourseQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author atguigu
 * @since 2022-01-26
 */
public interface EduCourseService extends IService<EduCourse> {

    //新增课程
    String saveCourseInfo(CourseInfoVo courseInfoVo);

    //根据课程id回显数据(返回上一步)
    CourseInfoVo getCourseInfoById(String courseId);

    //修改课程
    void updateCourseInfo(CourseInfoVo courseInfo);

    //根据ID获取课程最终发布信息
    CoursePublishVo getCoursePublishVoById(String courseId);

    //多条件查询分页方法
    void pageQuery(Page<EduCourse> pageParam, CourseQuery courseQuery);

    //根据ID删除课程
    boolean removeCourseById(String courseId);

    //1、分页课程列表
    Map<String, Object> pageListWeb(Page<EduCourse> pageParam, CourseQueryVo courseQuery);

    //1查询课程信息和讲师信息,涉及到多表联查，故需要自己写SQL语句
    CourseWebVo selectInfoWebById(String courseId);

    //2查询课程信息和讲师信息,涉及到多表联查，故需要自己写SQL语句
    CourseWebVo getCourseWebVoWebById(String courseId);

    //定时任务
    //1、每30分钟定时更新表的购买人数
    void updateBuyCountForEducourse();

    //课程浏览人数+1
    void updateViewCount(String courseId);
}
