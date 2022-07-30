package com.atguigu.educms.controller;


import com.atguigu.commonutils.R;
import com.atguigu.educms.entity.CrmBanner;
import com.atguigu.educms.service.CrmBannerService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *前台banner显示接口
 * @author xwz
 * @since 2022-02-21
 */
@RestController
@Api(description = "网站首页Banner列表")
@RequestMapping("/educms/banner")
//@CrossOrigin //跨域
public class BannerFrontController {

    @Autowired
    private CrmBannerService crmbannerService;

    @ApiOperation(value = "获取首页所有banner")
    @GetMapping("getAllBanner")
    public R getAllBanner() {
        List<CrmBanner> list = crmbannerService.selectAllBanner();
        return R.ok().data("bannerList", list);
    }

}

