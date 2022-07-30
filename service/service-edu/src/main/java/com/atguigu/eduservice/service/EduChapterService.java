package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.chapter.ChapterVo;
import com.atguigu.eduservice.entity.vo.CourseInfoVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author atguigu
 * @since 2022-01-26
 */
public interface EduChapterService extends IService<EduChapter> {
    //课程列表大纲，根据课程id查询
    List<ChapterVo> getChapterVideoBycourseId(String courseId);

    //根据ID删除章节
    boolean removeChapterById(String id);

    //根据ID删除章节
    boolean removeByCourseId(String courseId);
}
