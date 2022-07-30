package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.vo.CourseInfoVo;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.atguigu.eduservice.entity.vo.CourseQuery;
import com.atguigu.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
 */
@Api(description = "课程管理")
@RestController
@RequestMapping("/eduservice/course")
//@CrossOrigin //跨域
public class EduCourseController {

    @Autowired
    private EduCourseService eduCourseService;

    @ApiOperation(value = "多条件分页课程列表查询")
    @PostMapping("pageCourseList/{current}/{limit}")
    public R pageTeacherCondition(@ApiParam(name = "current",value = "当前页码",required = true) @PathVariable Long current,
                                  @ApiParam(name = "limit", value = "每页记录数",required = true)
                                  @PathVariable Long limit ,@RequestBody(required = false) CourseQuery courseQuery){ //@RequestBody(required = false) 表示teacherQuery可以为空，因为是RequestBody，所以要用PostMapping提交方式
        Page<EduCourse> pageParam = new Page<>(current, limit); //Page对象

        eduCourseService.pageQuery(pageParam, courseQuery); //调用方法的时候，底层封装，把impl里分页查询到的所有数据封装到pageParam对象里面

        List<EduCourse> records = pageParam.getRecords();//records:前端需要显示的数据的list集合
        //System.out.println("*****"+records.toString());
        long total = pageParam.getTotal();//总记录数
        return R.ok().data("total", total).data("rows", records);//链式编程，可以连续调用自身的方法
    }


    @ApiOperation("新增课程")
    @PostMapping("addCourse")
    public R addCourse(@RequestBody CourseInfoVo courseInfoVo){

        String id = eduCourseService.saveCourseInfo(courseInfoVo);
        return R.ok().data("courseId",id);
    }

    @ApiOperation("根据课程id回显数据(返回上一步)")
    @GetMapping("getCourseInfo/{courseId}")
    public R getCourseInfo(@PathVariable String courseId){
        CourseInfoVo courseInfo = eduCourseService.getCourseInfoById(courseId);
        return R.ok().data("courseInfo",courseInfo);
    }

    @ApiOperation("修改课程")
    @PostMapping("updateCourseInfo")
    public R updateCourseInfo(@RequestBody CourseInfoVo courseInfo){
        eduCourseService.updateCourseInfo(courseInfo);
        return R.ok();
    }


    @ApiOperation(value = "根据ID获取课程最终发布信息")
    @GetMapping("getCoursePublishVoById/{courseId}")
    public R getCoursePublishVoById(@PathVariable String courseId){
        CoursePublishVo coursePublishInfo = eduCourseService.getCoursePublishVoById(courseId);
        return R.ok().data("publicCourseInfo", coursePublishInfo);
    }

    @ApiOperation(value = "课程最终发布") //修改课程状态
    @PostMapping("publishCourse/{courseId}")
    public R publishCourse(@PathVariable String courseId){
        EduCourse eduCourse = new EduCourse();
        eduCourse.setId(courseId);
        eduCourse.setStatus("Normal");//设置课程发布状态  已发布 未发布
        eduCourseService.updateById(eduCourse);
        return R.ok();
    }

    @ApiOperation(value = "根据ID删除课程")
    @DeleteMapping("deleteCourseById/{courseId}")
    public R deleteCourseById(@ApiParam(name = "courseId", value = "课程ID", required = true)
            @PathVariable String courseId){
        boolean result = eduCourseService.removeCourseById(courseId);
        if (result) {
            return R.ok().message("删除成功!");
        }else{
            return R.error().message("删除失败!");
        }
    }

}

