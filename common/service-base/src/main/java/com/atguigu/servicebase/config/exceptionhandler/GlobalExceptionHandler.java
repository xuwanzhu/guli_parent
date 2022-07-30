package com.atguigu.servicebase.config.exceptionhandler;


import com.atguigu.commonutils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一异常处理类
 */
@Slf4j  //将日志输出到文件
@ControllerAdvice
public class GlobalExceptionHandler {

    /*
    * 全局异常处理
    * */
    @ExceptionHandler(Exception.class)  //不管出现什么异常 都会执行
    @ResponseBody  //为了返回数据
    public R error(Exception e){
        e.printStackTrace(); //将异常信息 打印输出
        return R.error().message("执行了全局异常处理");
    }


    /*
     * 特定异常处理
     * */
    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody  //为了返回数据
    public R error(ArithmeticException e){
        e.printStackTrace(); //将异常信息 打印输出
        return R.error().message("执行了特定异常处理");
    }


    /*
     * 自定义异常处理
     * 注：该异常不会自动抛出，需手动抛出异常，通过try catch
     * */
    @ExceptionHandler(GuliException.class)
    @ResponseBody
    public R error(GuliException e){
        e.printStackTrace();
        log.error(e.getMessage()); //将 错误日志输出到文件中
        //log.warn(e.getMessage());//将 警告日志输出到文件中
        return R.error().message(e.getMsg()).code(e.getCode()); //生成异常信息
    }

}




















