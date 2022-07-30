package com.atguigu.demo.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data //生成set、get方法
public class DemoData {

    //设置 一级名称
    @ExcelProperty("学生学号")
    private Integer sno;

    //设置 一级名称
    @ExcelProperty("学生名字")
    private String sname;
}
