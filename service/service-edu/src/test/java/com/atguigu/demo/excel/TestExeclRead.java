package com.atguigu.demo.excel;

import com.alibaba.excel.EasyExcel;

public class TestExeclRead {
    public static void main(String[] args) {
        //1、指定读取文件
        String filename = "D://write.xlsx";


        //2、执行execl读操作
        EasyExcel.read(filename,DemoData.class,new ExeclListener()).sheet().doRead();
    }
}
