package com.atguigu.eduservice.controller.front;

import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
//@CrossOrigin
@RequestMapping("/eduservice/teacherfront")
@Api(description = "前台页面讲师管理")
public class TeacherFrontController {

    @Autowired
    private EduTeacherService eduTeacherService;

    @Autowired
    private EduCourseService eduCourseService;

    //1、普通写法:获取讲师列表
    @ApiOperation(value = "分页讲师列表")
    @GetMapping("getTeacherFrontList/{page}/{limit}")
    public R getTeacherFrontList(@ApiParam(name = "page", value = "当前页码", required = true)
                                     @PathVariable Long page,
                                 @ApiParam(name = "limit", value = "每页记录数", required = true)
                                     @PathVariable Long limit){
        Page<EduTeacher> pageTeacher = new Page<>(page,limit); //创建一个page对象
        Map<String,Object> map = eduTeacherService.getPageTeacherList(pageTeacher);
        return R.ok().data(map);
    }


    //2、讲师详情
    @ApiOperation(value = "前台讲师详情")
    @GetMapping("getTeacherDetails/{teacherId}")
    public R getTeacherDetails(@PathVariable String teacherId){
        //1根据讲师id查询讲师基本信息
        EduTeacher eduTeacher = eduTeacherService.getById(teacherId);

        //2根据讲师id查询所讲课程
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        wrapper.eq("teacher_id",teacherId);
        List<EduCourse> eduCourseList = eduCourseService.list(wrapper);

        return R.ok().data("teacher",eduTeacher).data("courseList",eduCourseList);
    }
}
