package com.atguigu.article.controller;


import com.atguigu.article.client.UcenterClient;
import com.atguigu.article.entity.Article;
import com.atguigu.article.service.ArticleService;
import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.commonutils.vo.UcenterMemberVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 文章 前端控制器
 * </p>
 *
 */
@RestController
@RequestMapping("/article/articleService")
@Api(description = "文章管理")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UcenterClient ucenterClient;

    @ApiOperation(value = "分页文章列表")
    @PostMapping("pageArticleList/{current}/{limit}")
    public R pageArticleList(@ApiParam(name = "current",value = "当前页码",required = true)
                                  @PathVariable Long current,
                                  @ApiParam(name = "limit", value = "每页记录数",required = true)
                                  @PathVariable Long limit ,
                                  @RequestBody(required = false) Article article){
        Page<Article> pageParam = new Page<>(current, limit); //Page对象

      Map<String,Object> map = articleService.getPageArticleList(pageParam);
        return R.ok().data(map);//链式编程，可以连续调用自身的方法
    }


    @ApiOperation("新增文章")
    @PostMapping("addArticle")
    public R addArticle(@RequestBody Article article, HttpServletRequest request){
        String memberId = JwtUtils.getMemberIdByJwtToken(request); //1根据token字符串获取会员id，也可获取用户的其他信息
        if(StringUtils.isEmpty(memberId)) { //StringUtils：是用来操作String类的
            return R.error().code(28004).message("请登录"); // 返回错误代码28004，清除ticket信息并跳转到登录页面
        }
        //2补充评论数据，包括用户信息、课程信息，然后加到数据库中
        article.setMemberId(memberId);//会员id
        //3通过服务调用，获取到登录用户信息
        UcenterMemberVo ucenterInfo = ucenterClient.getInfoUc(memberId);
        //4继续赋值
        article.setNickname(ucenterInfo.getNickname());//昵称
        //article.setAvatar(ucenterInfo.getAvatar());//头像

        //5、保存到数据库
        boolean save = articleService.save(article);
        if (save) {
            return R.ok().message("新增文章成功!");
        }else {
            return R.error().message("新增文章失败!");
        }
    }

    @ApiOperation("根据文章id回显/查询数据")
    @GetMapping("getArticleInfo/{articleId}")
    public R getArticleInfo(@PathVariable String articleId){
        Article article = articleService.getById(articleId);
        return R.ok().data("articleInfo",article);
    }

    @ApiOperation("修改文章")
    @PostMapping("updateArticleInfo")
    public R updateArticleInfo(@RequestBody Article article){
        System.out.println("+++++++++"+article.toString());
        articleService.updateById(article);
        return R.ok();
    }


    //TODO 删除文章同时删除oss中的资源,同时还要去评论表中删除评论
    @ApiOperation(value = "根据ID删除文章")
    @DeleteMapping("deleteArticleById/{articleId}")
    public R deleteArticleById(@ApiParam(name = "articleId", value = "文章ID", required = true)
                              @PathVariable String articleId){
        boolean result = articleService.removeById(articleId);
        if (result) {
            return R.ok().message("删除文章成功!");
        }else{
            return R.error().message("删除文章失败!");
        }
    }

    @ApiOperation(value = "查询指定用户分页文章列表")
    @GetMapping("pageUserArticleList/{current}/{limit}")
    public R pageUserArticleList(@ApiParam(name = "current",value = "当前页码",required = true)
                             @PathVariable Long current,
                             @ApiParam(name = "limit", value = "每页记录数",required = true)
                             @PathVariable Long limit ,
                             HttpServletRequest request){
        String memberId = JwtUtils.getMemberIdByJwtToken(request); //1根据token字符串获取会员id，也可获取用户的其他信息
        System.out.println("+++++++++++++++++++++"+memberId);
        if(StringUtils.isEmpty(memberId)) { //StringUtils：是用来操作String类的
            return R.error().code(28004).message("请登录"); // 返回错误代码28004，清除ticket信息并跳转到登录页面
        }

        Page<Article> pageParam = new Page<>(current, limit); //Page对象

        Map<String,Object> map = articleService.getPageUserArticleList(pageParam,memberId);
        return R.ok().data(map);//链式编程，可以连续调用自身的方法
    }

}

