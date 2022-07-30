package com.atguigu.educms.service;

import com.atguigu.educms.entity.CrmBanner;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务类
 * </p>
 *
 * @author xwz
 * @since 2022-02-21
 */
public interface CrmBannerService extends IService<CrmBanner> {

    //获取所有banner
    List<CrmBanner> selectAllBanner();

    //删除Banner
    boolean removeBannerById(String id);
}
