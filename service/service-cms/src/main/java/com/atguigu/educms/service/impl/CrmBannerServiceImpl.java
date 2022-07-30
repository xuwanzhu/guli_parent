package com.atguigu.educms.service.impl;

import com.atguigu.educms.client.OssClient;
import com.atguigu.educms.entity.CrmBanner;
import com.atguigu.educms.mapper.CrmBannerMapper;
import com.atguigu.educms.service.CrmBannerService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 首页banner表 服务实现类
 * </p>
 *
 * @author xwz
 * @since 2022-02-21
 */
@Service
public class CrmBannerServiceImpl extends ServiceImpl<CrmBannerMapper, CrmBanner> implements CrmBannerService {

    @Autowired
    private OssClient ossClient;

    //获取所有banner
    @Cacheable(value = "banner", key = "'selectIndexList'")  //开启轮播图 redis缓存 ，需要有配置类 配置在commt模块中
    @Override
    public List<CrmBanner> selectAllBanner() {
        QueryWrapper<CrmBanner> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("sort"); //显示 根据sort进行排序
        //last方法，拼接sql语句
        wrapper.last("limit 4");

        List<CrmBanner> list = baseMapper.selectList(wrapper);

        return list;
    }

    //删除Banner,同时删除阿里云oss中的文件
    @Override
    public boolean removeBannerById(String id) {
        CrmBanner crmBanner = baseMapper.selectById(id);

        String url = crmBanner.getImageUrl();
        //截取
        url = url.replace("http://edu-guli-project-oss.oss-cn-shenzhen.aliyuncs.com/", "");
        try {
            //对URL地址进行编码，解决一些字符失效的问题,比如/
            url = URLEncoder.encode(url, "utf-8");
        }catch (Exception e){
            e.printStackTrace();
        }

        //封装进map
        Map<String, String> map = new HashMap<>();
        map.put("url",url);

        //1、服务调用，删除oss中的图片,先删除阿里云oss文件
        boolean delete = ossClient.delete(map);

        //2、删除数据库中的指定banner对象
        if (delete) {
            baseMapper.deleteById(id);
            return true;
        }
        else {
            return false;
        }

    }


    //TODO 轮播图的增删查改
}
