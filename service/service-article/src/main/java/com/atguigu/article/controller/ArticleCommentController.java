package com.atguigu.article.controller;


import com.atguigu.article.client.UcenterClient;
import com.atguigu.article.entity.ArticleComment;
import com.atguigu.article.service.ArticleCommentService;
import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.commonutils.vo.UcenterMemberVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 文章评论 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2022-04-13
 */
@RestController
@RequestMapping("/article/comment")
@Api(description = "文章评论")
public class ArticleCommentController {

    @Autowired
    private ArticleCommentService articleCommentService;

    //服务调用
    @Autowired
    private UcenterClient ucenterClient;


    //1、添加评论

    @ApiOperation(value = "添加评论")
    @PostMapping("addArticleComment")
    public R addArticleComment(HttpServletRequest request, @RequestBody ArticleComment articleComment){

        String memberId = JwtUtils.getMemberIdByJwtToken(request); //1根据token字符串获取会员id，也可获取用户的其他信息
        if(StringUtils.isEmpty(memberId)) { //StringUtils：是用来操作String类的
            return R.error().code(28004).message("请登录"); // 返回错误代码28004，清除ticket信息并跳转到登录页面
        }
        //2补充评论数据，包括用户信息、课程信息，然后加到数据库中
        articleComment.setMemberId(memberId);//会员id
        //3通过服务调用，获取到登录用户信息
        UcenterMemberVo ucenterInfo = ucenterClient.getInfoUc(memberId);
        //4继续赋值
        articleComment.setNickname(ucenterInfo.getNickname());//昵称
        articleComment.setAvatar(ucenterInfo.getAvatar());//头像

        //4添加数据到数据库
        boolean save = articleCommentService.save(articleComment);
        if (save) {
            return R.ok();
        }else {
            return R.error();
        }

    }



    //2、分页查询评论列表
    @ApiOperation(value = "评论分页列表")
    @GetMapping("getCommentList/{page}/{limit}")
    public R getCommentList(@ApiParam(name = "page",value = "当前页码",required = true)
                            @PathVariable Long page,
                            @ApiParam(name = "limit",value = "每页记录数",required = true)
                            @PathVariable Long limit,
                            @ApiParam(name = "articleId",value = "文章id",required = false)
                                    String articleId){
        Page<ArticleComment> pageParam = new Page<>(page, limit);
        QueryWrapper<ArticleComment> wrapper = new QueryWrapper<>();
        wrapper.eq("article_id",articleId);
        wrapper.orderByDesc("gmt_create");//按最新排序

        articleCommentService.page(pageParam,wrapper); //page：调用方法的时候，底层封装，把分页的所有数据封装到pageParam对象里面

        //将数据先提取，然后再封装到map
        List<ArticleComment> commentList = pageParam.getRecords();
        Map<String, Object> map = new HashMap<>();

        map.put("items", commentList); //评论列表
        map.put("current", pageParam.getCurrent());  //当前页，和上面的参数page 一个意思
        map.put("pages", pageParam.getPages());
        map.put("size", pageParam.getSize());
        map.put("total", pageParam.getTotal());
        map.put("hasNext", pageParam.hasNext());
        map.put("hasPrevious", pageParam.hasPrevious());

        return R.ok().data(map);
    }

}

