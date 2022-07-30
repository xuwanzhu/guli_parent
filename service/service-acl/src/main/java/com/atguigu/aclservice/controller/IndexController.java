package com.atguigu.aclservice.controller;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.aclservice.entity.Permission;
import com.atguigu.aclservice.service.IndexService;
import com.atguigu.aclservice.service.PermissionService;
import com.atguigu.commonutils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/acl/index")
//@CrossOrigin
@Api(description = "")
public class IndexController {

    @Autowired
    private IndexService indexService;

    /**
     * 根据token获取用户信息，包含权限信息
     */
    @ApiOperation(value = "根据token获取用户信息")
    @GetMapping("info")
    public R info(){
        //获取当前登录用户用户名
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Map<String, Object> userInfo = indexService.getUserInfo(username);
        return R.ok().data(userInfo);
    }

    /**
     * 获取当前用户的菜单(路由)
     * @return
     */
    @GetMapping("menu")
    @ApiOperation(value = "获取菜单")
    public R getMenu(){
        //获取当前登录用户用户名
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        //列表中只有一级菜单(一个或者多个)，一级菜单中有children，children中装着二级菜单和三级按钮，二级菜单和三级按钮中没有children
        //返回的为json格式的 菜单数据
        List<JSONObject> permissionList = indexService.getMenu(username); //拼接路由

        return R.ok().data("permissionList", permissionList);
    }

    @PostMapping("logout")
    @ApiOperation(value = "退出")
    public R logout(){
        return R.ok();
    }

}
