package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.entity.vo.TeacherQuery;
import com.atguigu.eduservice.service.EduTeacherService;
import com.atguigu.servicebase.config.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2022-01-07
 */

@Api(description="讲师管理")
@RestController
@RequestMapping("/eduservice/teacher") //edu-teacher
//@CrossOrigin //解决跨越问题   9528访问8001会产生跨越问题
public class EduTeacherController {

    //注入service
    @Autowired
    private EduTeacherService eduTeacherService;

   /* @ApiOperation(value = "所有讲师列表")
    @GetMapping("findAll")
    public List<EduTeacher> findAll(){
        List<EduTeacher> list = eduTeacherService.list(null);  //获取表中所有的数据
        return list;
    }*/
   @ApiOperation(value = "所有讲师列表")
   @GetMapping("findAll")
   public R list(){
       List<EduTeacher> list = eduTeacherService.list(null);
       return R.ok().data("items", list); // 值是一个ArrayList数组，键是items
   }



    /*@ApiOperation(value = "逻辑删除讲师")
    @DeleteMapping("{id}")  // id值 需要通过路径进行传递
    public boolean remove(@ApiParam(name = "id", value = "讲师ID", required = true) @PathVariable String id){  //注解表示获取到 上一行中传来的那个 id 值

        boolean result = eduTeacherService.removeById(id);
        return result;
    }*/
    @ApiOperation(value = "根据ID删除讲师")
    @DeleteMapping("{id}")
    public R removeById(
        @ApiParam(name = "id", value = "讲师ID", required = true) @PathVariable String id){
        boolean b = eduTeacherService.removeById(id);
        if (b){
            return R.ok();
        }else {
            return R.error();
        }
    }


    @ApiOperation(value = "分页讲师列表")
    @GetMapping("pageTeacher/{current}/{limit}")
    public R pageTeacher(@ApiParam(name = "current",value = "当前页码",required = true) @PathVariable Long current,
                      @ApiParam(name = "limit", value = "每页记录数",required = true) @PathVariable Long limit){
        Page<EduTeacher> pageParam = new Page<>(current, limit); //Page对象

        //调用方法实现分页
        //参数：当前对象 和 条件
        eduTeacherService.page(pageParam,null); //调用方法的时候，底层封装，把分页的所有数据封装到pageParam对象里面

        long total = pageParam.getTotal();//总记录数

        List<EduTeacher> records = pageParam.getRecords();//数据list集合
        //返回的data结果格式:有total和records集合
        return R.ok().data("total",total).data("records",records); //链式编程，可以连续调用自身的方法
    }


    @ApiOperation(value = "多条件分页讲师列表查询")
    @PostMapping("pageTeacherCondition/{current}/{limit}")
    public R pageTeacherCondition(@ApiParam(name = "current",value = "当前页码",required = true) @PathVariable Long current,
                                  @ApiParam(name = "limit", value = "每页记录数",required = true)
                                  @PathVariable Long limit ,@RequestBody(required = false) TeacherQuery teacherQuery){ //@RequestBody(required = false) 表示teacherQuery可以为空，因为是RequestBody，所以要用PostMapping提交方式
        Page<EduTeacher> pageParam = new Page<>(current, limit); //Page对象

        //构建条件
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        //多条件组合查询
        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();

        if (!StringUtils.isEmpty(name)){
            wrapper.like("name",name);
        }
        if (!StringUtils.isEmpty(level) ) {
            wrapper.eq("level", level);
        }
        if (!StringUtils.isEmpty(begin)) {
            wrapper.ge("gmt_create", begin);  //gmt_create为数据库表中的列
        }
        if (!StringUtils.isEmpty(end)) {
            wrapper.le("gmt_create", end);
        }

        //对列表进行排序
        wrapper.orderByDesc("gmt_create");


        //调用方法实现分页
        //参数：当前对象 和 条件
        eduTeacherService.page(pageParam,wrapper); //调用方法的时候，底层封装，把分页的所有数据封装到pageParam对象里面
        long total = pageParam.getTotal();//总记录数
        List<EduTeacher> records = pageParam.getRecords();//数据list集合
        //返回的data结果格式:有total和records集合
        return R.ok().data("total",total).data("rows",records); //链式编程，可以连续调用自身的方法
    }


    @ApiOperation(value = "添加讲师")
    @PostMapping("addTeacher")
    public R addTeacher(@RequestBody EduTeacher eduTeacher){
        boolean save = eduTeacherService.save(eduTeacher);
        if (save){
            return R.ok();
        }else{
            return R.error();
        }
    }


    @ApiOperation(value = "根据ID查询讲师")
    @GetMapping("getTeacher/{id}")
    public R getTeacher(@PathVariable String id){
        EduTeacher eduTeacher = eduTeacherService.getById(id);


        /*try {
            int i = 10/0;
        }catch (Exception e){
            throw new GuliException(2001,"执行了自定义异常处理...");
        }*/

        if (!StringUtils.isEmpty(eduTeacher))
            return R.ok().data("teacher",eduTeacher);
        else return R.error();
    }

    @ApiOperation(value = "讲师修改功能")
    @PostMapping("updateTeacher")
    public R updateTeacher(@RequestBody EduTeacher eduTeacher){
        boolean flag = eduTeacherService.updateById(eduTeacher);
        if(flag){
            return R.ok();
        }else {
            return R.error();
        }
    }

}

