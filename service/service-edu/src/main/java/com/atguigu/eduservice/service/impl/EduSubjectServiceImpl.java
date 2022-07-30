package com.atguigu.eduservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.excel.SubjectData;
import com.atguigu.eduservice.entity.subject.OneSubject;
import com.atguigu.eduservice.entity.subject.TwoSubject;
import com.atguigu.eduservice.listener.SubjectExcelListener;
import com.atguigu.eduservice.mapper.EduSubjectMapper;
import com.atguigu.eduservice.service.EduSubjectService;
import com.atguigu.servicebase.config.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2022-01-22
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {

    //添加课程分类
    @Override
    public void saveSubject(MultipartFile file,EduSubjectService eduSubjectService) {
        try {
            //获得文件输入流
            InputStream in = file.getInputStream();

            //调用方法进行读取excel,主要是用监听器实现
            EasyExcel.read(in, SubjectData.class, new SubjectExcelListener(eduSubjectService)).sheet().doRead();
        }catch (Exception e){
            e.printStackTrace();
            throw new GuliException(20002,"添加课程分类失败");
        }
    }

    //课程分类列表
    @Override
    public List<OneSubject> getAllOneTwoSubject() {

        //1、查询出所以一级分类
        QueryWrapper<EduSubject> wrapperOne = new QueryWrapper<>(); //创建一个查询对象
        wrapperOne.eq("parent_id","0"); //给查询对象添加条件
        List<EduSubject> oneSubjectList = baseMapper.selectList(wrapperOne);

        //2、查询出所有二级分类
        QueryWrapper<EduSubject> wrapperTwo = new QueryWrapper<>(); //创建一个查询对象
        wrapperTwo.ne("parent_id","0"); //不等于0
        List<EduSubject> twoSubjectList = baseMapper.selectList(wrapperTwo);


        //创建list集合，用于存储最终的数据
        List<OneSubject> finalSubjectList = new ArrayList<>();

        //3、封装一级分类
        //查询出来所有的一级分类List集合，遍历得到每个一级分类对象，获取每个一级分类对象值
        //封装到要求的list集合finalSubjectList里面
        for (int i = 0; i < oneSubjectList.size(); i++) {
            EduSubject eduSubject = oneSubjectList.get(i); //得到oneSubjectList中的每个对象

            //把eduSubject里面值获取出来，放到OneSubject对象里面

            OneSubject oneSubject = new OneSubject();
            //oneSubject.setChildren(twoSubjectList.get());
            //oneSubject.setId(eduSubject.getId());
            //oneSubject.setTitle(eduSubject.getTitle());
            //多个OneSubject放到finalSubjectList里面
            BeanUtils.copyProperties(eduSubject,oneSubject);//相当于上两句              List返回值用[]表示,对象用{}表示

            finalSubjectList.add(oneSubject);//添加到finalSubjectList中
        }


        //4、封装二级分类
        //将twoSubjectList中的值添加到finalSubjectList中的对象中
        for (int i = 0; i < finalSubjectList.size(); i++) {
            //创建一个list集合用来接受 相同一级分类下的 二级分类
            List<TwoSubject> OneTwoSubjectlist = new ArrayList<>();

            for (int j = 0; j < twoSubjectList.size(); j++) {
                if ((finalSubjectList.get(i).getId()).equals(twoSubjectList.get(j).getParentId())){
                    TwoSubject twoSubject = new TwoSubject();
                    twoSubject.setId(twoSubjectList.get(j).getId());
                    twoSubject.setTitle(twoSubjectList.get(j).getTitle());

                    OneTwoSubjectlist.add(twoSubject);
                }
            }

            finalSubjectList.get(i).setChildren(OneTwoSubjectlist);
            //OneTwoSubjectlist.clear(); 用list.clear()方法清空list；用此方法，其它引用该list的值也会变成空。所以这里不能用clear,同时等于null也不行
        }
        return finalSubjectList;
    }
}
