package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.chapter.ChapterVo;
import com.atguigu.eduservice.service.EduChapterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2022-01-26
 */
@Api(description = "编辑课程大纲")
@RestController
//@CrossOrigin //跨域
@RequestMapping("/eduservice/chapter")
public class EduChapterController {

    @Autowired
    private EduChapterService eduChapterService;

    //查询章节列表，课程列表大纲
    @GetMapping("getChapterVideo/{courseId}")
    public R getChapterVideo(@PathVariable String courseId){

         List<ChapterVo> list = eduChapterService.getChapterVideoBycourseId(courseId);
        System.out.println("");
        return R.ok().data("list",list);
    }


    @ApiOperation(value = "新增章节")
    @PostMapping("addChapter")
    public R addChapter(@ApiParam(name = "chapter", value = "章节对象", required = true)
            @RequestBody EduChapter chapter){ //接收前台返回的 表单对象
        eduChapterService.save(chapter);//调用系统的save方法
        return R.ok();
    }

    @ApiOperation(value = "修改章节")
    @PostMapping("updateChaper")
    public R updateChaper(
            @ApiParam(name = "chapter", value = "章节对象", required = true)
                    @RequestBody EduChapter chapter){
        eduChapterService.updateById(chapter);//调用系统的updateById方法
        return R.ok();
    }

    @ApiOperation(value = "根据ID删除章节")
    @DeleteMapping("/deleteChapterById/{id}")
    public R deleteChapterById(
            @ApiParam(name = "id", value = "章节ID", required = true)
            @PathVariable String id){
        boolean result = eduChapterService.removeChapterById(id);
        if(result){
            return R.ok().message("删除该章节成功!");
        }else{
            return R.error().message("该章节下存在小节，请先删除课程小节!");
        }
    }

    //根据章节id查询单个章节数据
    @ApiOperation(value = "根据ID查询单个章节")
    @GetMapping("/getOneChapter/{chapterId}")
    public R getOneChapter(@PathVariable String chapterId){

        EduChapter chapter = eduChapterService.getById(chapterId);

        return R.ok().data("items",chapter);
    }


}

