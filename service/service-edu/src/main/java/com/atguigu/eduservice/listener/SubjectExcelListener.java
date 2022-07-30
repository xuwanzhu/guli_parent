package com.atguigu.eduservice.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.excel.SubjectData;
import com.atguigu.eduservice.service.EduSubjectService;
import com.atguigu.servicebase.config.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.Map;

/*
*
* 功能：将读取到的数据 添加到数据库
*
* */

public class SubjectExcelListener extends AnalysisEventListener<SubjectData> {

    private EduSubjectService eduSubjectService;

    public SubjectExcelListener() {}

    public SubjectExcelListener(EduSubjectService eduSubjectService) {//创建有参数构造，传递subjectService用于操作数据库
        this.eduSubjectService = eduSubjectService;
    }

    //一行一行读取数据
    @Override
    public void invoke(SubjectData subjectData, AnalysisContext analysisContext) {
        if (subjectData == null){
            throw new GuliException(20001,"excel文件数据为空！");
        }
        //一行一行读取，每次读取有两个值，第一个值为一级分类，第二个值为二级分类
        //判断一级分类是否重复
        EduSubject existOneSubject = this.existOneSubject(eduSubjectService,subjectData.getOneSubjectName());
        if (existOneSubject == null){    //没有相同一级分类，进行添加
            existOneSubject = new EduSubject();
            existOneSubject.setParentId("0");
            existOneSubject.setTitle(subjectData.getOneSubjectName());
            eduSubjectService.save(existOneSubject);//添加功能，即添加到数据库
        }

        //判断二级分类是否重复
        String pid = existOneSubject.getId();//获取一级分类id值
        EduSubject existTwoSubject = this.existTwoSubject(eduSubjectService, subjectData.getOneSubjectName(), pid);
        if (existTwoSubject == null){    //没有相同一级分类，进行添加
            existTwoSubject = new EduSubject();
            existTwoSubject.setParentId(pid);
            existTwoSubject.setTitle(subjectData.getTwoSubjectName());
            eduSubjectService.save(existTwoSubject);
        }

    }





    //读取excel表头信息
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        System.out.println("表头信息："+headMap);
    }

    //读取完后执行
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {}


    //判断一级分类不能重复添加
    /*
    * Wrapper的常用方法：
    eq方法，名称是equals的缩写，两个参数，一个是数据库表字段的名称，一个是表字段值
    userQueryWrapper.eq("user_id", 9); // WHERE user_id = ?
    * */
    private EduSubject existOneSubject(EduSubjectService subjectService,String name){

        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>(); //构造一个条件对象，然后下面为它添加条件
        wrapper.eq("title",name);
        wrapper.eq("parent_id","0");// wrapper.eq("title",name).eq("parent_id","0");


        EduSubject oneSubject = subjectService.getOne(wrapper); //getOne为对数据库中满足wrapper条件的对象进行查询并返回
        return oneSubject;
    }


    //判断二级分类不能重复添加
    private EduSubject existTwoSubject(EduSubjectService subjectService,String name,String pid){
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title",name);
        wrapper.eq("parent_id",pid);

        EduSubject twoSubject = subjectService.getOne(wrapper);//查询功能
        return twoSubject;
    }




}
