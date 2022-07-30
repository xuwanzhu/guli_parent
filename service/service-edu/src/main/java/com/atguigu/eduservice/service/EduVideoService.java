package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.EduVideo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author atguigu
 * @since 2022-01-26
 */
public interface EduVideoService extends IService<EduVideo> {

    //根据id删除小节
    boolean removeByCourseId(String courseId);
}
