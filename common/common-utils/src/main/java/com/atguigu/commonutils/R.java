package com.atguigu.commonutils;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
/*
*    统一结果返回 格式
*
* 项目中我们会将响应封装成json返回，一般我们会将所有接口的数据格式统一， 使前端(iOS Android,Web)对数据的操作更一致、轻松。
* */
@Data
public class R {
    @ApiModelProperty(value = "是否成功")
    private Boolean success;

    @ApiModelProperty(value = "返回码")
    private Integer code;

    @ApiModelProperty(value = "返回消息")
    private String message;

    @ApiModelProperty(value = "返回数据")
    private Map<String, Object> data = new HashMap<String, Object>();


    private R(){}  //将该类设置成只能 自己用，防止别人用来new对象

    public static R ok(){
        R r = new R();
        r.setSuccess(true);
        r.setCode(ResultCode.SUCCESS);
        r.setMessage("成功");
        return r;
    }


    public static R error(){
        R r = new R();
        r.setSuccess(false);
        r.setCode(ResultCode.ERROR);
        r.setMessage("失败");
        return r;
    }


    public R success(Boolean success){
        this.setSuccess(success);
        return this;
    }


    public R message(String message){
        this.setMessage(message);
        return this;
    }


    public R code(Integer code){
        this.setCode(code);
        return this;
    }


    public R data(String key, Object value){
        this.data.put(key, value);  //值是一个ArrayList数组，键是key
        return this;
    }


    public R data(Map<String, Object> map){
        this.setData(map);
        return this;
    }
}

