package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.entity.chapter.ChapterVo;
import com.atguigu.eduservice.entity.chapter.VideoVo;
import com.atguigu.eduservice.mapper.EduChapterMapper;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduVideoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2022-01-26
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {

    @Autowired
    private EduVideoService eduVideoService;

    //课程列表大纲，根据课程id查询
    @Override
    public List<ChapterVo> getChapterVideoBycourseId(String courseId) {
        //1、获得课程章节
        QueryWrapper<EduChapter> wrapperChapter = new QueryWrapper<>();
        wrapperChapter.eq("course_id",courseId);
        wrapperChapter.orderByAsc("sort", "id");
        List<EduChapter> eduChapterList = baseMapper.selectList(wrapperChapter); //获得章节的所有数据


        //2、获得课程小节，而不是章节的小节
        QueryWrapper<EduVideo> wrapperVideo = new QueryWrapper<>();
        wrapperVideo.eq("course_id",courseId);
        wrapperVideo.orderByAsc("sort", "id");
        List<EduVideo> eduVideoList = eduVideoService.list(wrapperVideo); //获得小节的所有数据
        //System.out.println("================="+eduVideoList.toString());

        //最终返回的数据list
        List<ChapterVo> ListChapterVideo = new ArrayList<>();

        //3、封装课程章节
        for (int i = 0; i < eduChapterList.size(); i++) {
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(eduChapterList.get(i),chapterVo); //将eduChapterList.get(i)对象set到chapterVo中
            ListChapterVideo.add(chapterVo);
        }


        //4、封装章节的小节
        for (int i = 0; i < ListChapterVideo.size(); i++) {
            List<VideoVo> videoList= new ArrayList<>(); //存储每次chapter中的children列表

            for (int j = 0; j < eduVideoList.size(); j++) {
                if (ListChapterVideo.get(i).getId().equals(eduVideoList.get(j).getChapterId())){
                    VideoVo videoVo = new VideoVo();
                    //videoVo.setId(eduVideoList.get(j).getId());
                    //videoVo.setTitle(eduVideoList.get(j).getTitle());
                    BeanUtils.copyProperties(eduVideoList.get(j),videoVo);//重点：相当于上面两句，但是只可以将videoVo中属性名称和EduVideo中属性名称一样的 值 进行复制。
                    //System.out.println("############################"+videoVo.toString());
                    videoList.add(videoVo);
                }
            }
            ListChapterVideo.get(i).setChildren(videoList);
        }
        return ListChapterVideo;
    }


    //根据ID删除章节
    @Override
    public boolean removeChapterById(String id) { //章节id
        //1、查询该章节下面是否还有 小节
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>(); //创建查询对象
        wrapper.eq("chapter_id",id);
        List<EduVideo> list = eduVideoService.list(wrapper);
        if (list.isEmpty()){ //不能用==null判断，语法有错
            int result = baseMapper.deleteById(id); //返回的为 执行的语句条数
            if (result != 0){
                return true;
            }else {
                return false;
            }
        }else {
            return false;
        }
    }


    //根据ID删除章节
    @Override
    public boolean removeByCourseId(String courseId) {
        QueryWrapper<EduChapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseId);
        Integer count = baseMapper.delete(queryWrapper);
        return null != count && count > 0;
    }
}
