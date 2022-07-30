package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduCourseDescription;
import com.atguigu.eduservice.entity.frontvo.CourseQueryVo;
import com.atguigu.eduservice.entity.frontvo.CourseWebVo;
import com.atguigu.eduservice.entity.vo.CourseInfoVo;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.atguigu.eduservice.entity.vo.CourseQuery;
import com.atguigu.eduservice.mapper.EduCourseMapper;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduCourseDescriptionService;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.service.EduVideoService;
import com.atguigu.servicebase.config.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2022-01-26
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

    //课程小节
    @Autowired
    private EduVideoService eduVideoService;

    //章节
    @Autowired
    private EduChapterService eduChapterService;


    //课程简介
    @Autowired
    private EduCourseDescriptionService eduCourseDescriptionService;

    //新增课程的基本信息
    @Override
    public String saveCourseInfo(CourseInfoVo courseInfoVo) {
        EduCourse eduCourse = new EduCourse();

        BeanUtils.copyProperties(courseInfoVo,eduCourse);//将courseInfoVo中的数据 set到eduCourse中
        int insert = baseMapper.insert(eduCourse);//调用dao层将前台获取的表单数据传到数据库
        if (insert == 0){ //添加失败
            throw new GuliException(20001, "课程详情信息保存失败");
        }

        //课程cid
        String cid = eduCourse.getId();

        //添加课程 简介信息
        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        eduCourseDescription.setId(cid);
        eduCourseDescription.setDescription(courseInfoVo.getDescription());
        eduCourseDescriptionService.save(eduCourseDescription);

        // TODO 每返回一个cid代表新增了一门课程
        return cid;
    }


    //根据课程id回显数据(返回上一步)
    @Override
    public CourseInfoVo getCourseInfoById(String courseId) {
        //查询课程表
        CourseInfoVo courseInfoVo = new CourseInfoVo();

        EduCourse eduCourse = baseMapper.selectById(courseId);

        BeanUtils.copyProperties(eduCourse,courseInfoVo);

        //查询简介表
        courseInfoVo.setDescription(eduCourseDescriptionService.getById(courseId).getDescription());
        return courseInfoVo;
    }


    //修改课程
    @Override
    public void updateCourseInfo(CourseInfoVo courseInfo) {
        //1、修改课程表
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfo,eduCourse);
        int i = baseMapper.updateById(eduCourse);
        if (i == 0) {
            throw new GuliException(20001,"修改课程信息失败!");
        }

        //2、修改简介表
        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        eduCourseDescription.setId(courseInfo.getId());
        eduCourseDescription.setDescription(courseInfo.getDescription());
        boolean update = eduCourseDescriptionService.updateById(eduCourseDescription);
        System.out.println("9999");
        if (!update) {
            throw new GuliException(20001,"保存课程信息失败!");
        }
    }


    //根据ID获取课程最终发布信息
    @Override
    public CoursePublishVo getCoursePublishVoById(String courseId) {
        //调用mapper,自己创建的dao层查询方法
        CoursePublishVo coursePublishVoById = baseMapper.getCoursePublishVoById(courseId);
        return coursePublishVoById;
    }

    //多条件分页列表查询方法
    @Override
    public void pageQuery(Page<EduCourse> pageParam, CourseQuery courseQuery) {
        QueryWrapper<EduCourse> queryWrapper = new QueryWrapper<>(); //创建一个查询对象
        queryWrapper.orderByDesc("gmt_create"); //排序

        if(courseQuery == null){ //如果查询条件为空
            baseMapper.selectPage(pageParam, queryWrapper);
            return;
        }

        //判断查询条件
        String title = courseQuery.getTitle();
        String teacherId = courseQuery.getTeacherId();
        String subjectParentId = courseQuery.getSubjectParentId();
        String subjectId = courseQuery.getSubjectId();
        if (!StringUtils.isEmpty(title)) {
            queryWrapper.like("title", title);
        }
        if (!StringUtils.isEmpty(teacherId) ) {
            queryWrapper.eq("teacher_id", teacherId);
        }
        if (!StringUtils.isEmpty(subjectParentId)) {
            queryWrapper.eq("subject_parent_id", subjectParentId);
        }
        if (!StringUtils.isEmpty(subjectId)) {
            queryWrapper.eq("subject_id", subjectId);
        }

        //调用selectPage方法查询
        baseMapper.selectPage(pageParam, queryWrapper);
    }


    //根据ID删除课程
    //TODO 删除视频
    @Override
    public boolean removeCourseById(String courseId) {
        //1、根据id删除所有视频和小节
        eduVideoService.removeByCourseId(courseId);

        //2、根据id删除所有章节
        eduChapterService.removeByCourseId(courseId);

        //根据id删除所有课程详情
        //courseDescriptionService.removeById(id);
        //删除封面 TODO 独立完成

        //3、最终删除课程
        Integer result = baseMapper.deleteById(courseId);
        return null != result && result > 0;
    }

    //1、前台首页-分页条件课程
    @Override
    public Map<String, Object> pageListWeb(Page<EduCourse> pageParam, CourseQueryVo courseQuery) {

        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();//这段代码的意思就是，首先新建一个QueryWrapper对象，类型为EduCourse对象，也就是你需要查询的实体数据

        //1、判断 查询条件
        if (!StringUtils.isEmpty(courseQuery.getSubjectParentId())) { //一级分类
            wrapper.eq("subject_parent_id",courseQuery.getSubjectParentId());
        }
        if (!StringUtils.isEmpty(courseQuery.getSubjectId())) { //二级分类
            wrapper.eq("subject_id", courseQuery.getSubjectId());
        }
        if (!StringUtils.isEmpty(courseQuery.getBuyCountSort())) { //购买量
            wrapper.orderByDesc("buy_count");
        }
        if (!StringUtils.isEmpty(courseQuery.getGmtCreateSort())) {//最新
            wrapper.orderByDesc("gmt_create");
        }
        if (!StringUtils.isEmpty(courseQuery.getPriceSort())) {  //价格
            wrapper.orderByDesc("price");
        }

        String title = courseQuery.getTitle();
        if (!StringUtils.isEmpty(title)) { //课程搜索
            wrapper.like("title",title);
        }
        System.out.println("===========》》》》》"+wrapper.toString());

        //2、进行查询
        baseMapper.selectPage(pageParam,wrapper); //这段代码会将查询到的所有数据封装到pageParam中

        //3、获取数据
        List<EduCourse> records = pageParam.getRecords();
        long current = pageParam.getCurrent();
        long pages = pageParam.getPages();
        long size = pageParam.getSize();
        long total = pageParam.getTotal();
        boolean hasNext = pageParam.hasNext();
        boolean hasPrevious = pageParam.hasPrevious();

        //4、将上面获得的数据封装到map中
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("items", records);
        map.put("current", current);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);

        //5、返回根据条件查询所得的数据
        return map;
    }


    //前台：查询课程信息和讲师信息,涉及到多表联查，故需要自己写SQL语句
    @Override
    public CourseWebVo selectInfoWebById(String courseId) {
        return baseMapper.selectInfoWebById(courseId);
    }

    @Override
    public CourseWebVo getCourseWebVoWebById(String courseId) {
        return baseMapper.getCourseWebVoWebById(courseId);
    }


    //定时任务
    //1、每30分钟执行一次
    @Override
    public void updateBuyCountForEducourse() {
        List<EduCourse> eduCourseList = baseMapper.selectList(null);
        for(EduCourse course:eduCourseList){
            String courseId = course.getId();
            baseMapper.updateBuyCount(courseId);
            System.out.println("执行定时任务=>"+courseId);
        }

    }

    @Override
    public void updateViewCount(String courseId) {
        baseMapper.updateViewCount(courseId);
        System.out.println("浏览+1");
    }

}
