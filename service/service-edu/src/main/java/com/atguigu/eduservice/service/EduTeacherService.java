package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.EduTeacher;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author atguigu
 * @since 2022-01-07
 */
public interface EduTeacherService extends IService<EduTeacher> {

    //1、普通写法获取讲师列表
    Map<String, Object> getPageTeacherList(Page<EduTeacher> pageTeacher);
}
