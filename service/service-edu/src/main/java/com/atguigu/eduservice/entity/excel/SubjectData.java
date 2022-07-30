package com.atguigu.eduservice.entity.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/*
* excel表格的 实体类
* EduSubject为数据库中的类
*
* 我们通过excel的实体类 将excel表格的数据  读取到  数据库中（EduSubject为数据库中的excel数据保存形式），所以在SubjectExcelListener监听器中我们传入
* 的实体类为excel表格的 实体类
* excel
* */
@Data
public class SubjectData {

    @ExcelProperty(index = 0) //excel表格 第一列的列名
    private String oneSubjectName;

    @ExcelProperty(index = 1)
    private String twoSubjectName;
}
