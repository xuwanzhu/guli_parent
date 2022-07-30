package com.atguigu.educms.controller;


import com.atguigu.commonutils.R;
import com.atguigu.educms.entity.CrmBanner;
import com.atguigu.educms.entity.query.BannerQuery;
import com.atguigu.educms.service.CrmBannerService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 * 后台banner管理接口
 * @author xwz
 * @since 2022-02-21
 */
@RestController
@RequestMapping("/educms/banneradmin")
@Api(description="Banner后台管理")
//@CrossOrigin //跨域
public class BannerAdminController {

    @Autowired
    private CrmBannerService crmBannerService;

    @ApiOperation(value = "获取Banner分页列表")
    @PostMapping("pageBanner/{page}/{limit}")
    public R pageBanner(@ApiParam(name = "page",value = "当前页码",required = true)
                        @PathVariable Long page,
                        @ApiParam(name = "limit", value = "每页记录数", required = true)
                        @PathVariable Long limit,
                        @RequestBody(required = false) BannerQuery bannerQuery){
        //构建分页对象
        Page<CrmBanner> pageParam = new Page<>(page, limit);
        //构建条件
        QueryWrapper<CrmBanner> wrapper = new QueryWrapper<>();

        //多条件组合查询
        String title = bannerQuery.getTitle();
        String begin = bannerQuery.getBegin();
        String end = bannerQuery.getEnd();

        if (!StringUtils.isEmpty(title)){
            wrapper.like("title",title);
        }
        if (!StringUtils.isEmpty(begin)) {
            wrapper.ge("gmt_create", begin);  //gmt_create为数据库表中的列
        }
        if (!StringUtils.isEmpty(end)) {
            wrapper.le("gmt_create", end);
        }
        //对列表进行排序
        wrapper.orderByDesc("gmt_create");

        crmBannerService.page(pageParam,wrapper);

        long total = pageParam.getTotal();//总记录数
        List<CrmBanner> records = pageParam.getRecords();//数据list集合
        return R.ok().data("items",records).data("total",total);
    }

    @ApiOperation(value = "添加Banner")
    @PostMapping("save")
    public R save(@RequestBody CrmBanner banner) {
        crmBannerService.save(banner);
        return R.ok();
    }


    @ApiOperation(value = "修改Banner")
    @PostMapping("update")
    public R updateById(@RequestBody CrmBanner crmBanner) {

        crmBannerService.updateById(crmBanner);
        return R.ok();
    }

    @ApiOperation(value = "删除Banner") //同时删除阿里云的存储资源
    @DeleteMapping("remove/{id}")
    public R remove(@PathVariable String id) {
        boolean delete = crmBannerService.removeBannerById(id);
        if (delete) {
            return R.ok();
        }else return R.error();
    }


    @ApiOperation(value = "根据id查询Banner对象数据")
    @GetMapping("selectBannerById/{id}")
    public R selectBannerById(@ApiParam(name = "轮播图id",value = "id",required = true)@PathVariable String id) {
        CrmBanner banner = crmBannerService.getById(id);
        return R.ok().data("banner",banner);
    }

}

