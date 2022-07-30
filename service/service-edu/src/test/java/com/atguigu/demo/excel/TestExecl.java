package com.atguigu.demo.excel;

import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.List;

public class TestExecl {
    public static void main(String[] args) {
        //设置写入文件夹和名称
        String filename = "D://write.xlsx";

        //调用easyexecl方法实现写操作
        EasyExcel.write(filename,DemoData.class).sheet("学生列表").doWrite(getData());
    }


    //生成execl数据
    public static List<DemoData> getData(){
        List<DemoData> list = new ArrayList<>();
        for (int i=0;i<10;i++){
            DemoData demoData = new DemoData();
            demoData.setSname("Mark"+i);
            demoData.setSno(i);
            list.add(demoData);
        }
        return list;
    }
}
