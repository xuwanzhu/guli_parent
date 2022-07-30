package com.atguigu.question.controller;


import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.commonutils.vo.UcenterMemberVo;
import com.atguigu.question.client.UcenterClient;
import com.atguigu.question.entity.Question;
import com.atguigu.question.entity.QuestionVo;
import com.atguigu.question.service.QuestionService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 *
 * 问答 前端控制器
 *
 */
@RestController
@RequestMapping("/question/service")
@Api(description = "问答管理")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UcenterClient ucenterClient;


    @ApiOperation(value = "分页问答列表")
    @PostMapping("pageQuestionList/{current}/{limit}")
    public R pageQuestionList(@ApiParam(name = "current",value = "当前页码",required = true)
                              @PathVariable Long current,
                              @ApiParam(name = "limit", value = "每页记录数",required = true)
                              @PathVariable Long limit ,
                              @RequestBody(required = false) QuestionVo questionVo){
        Page<Question> pageParam = new Page<>(current, limit); //Page对象

        Map<String,Object> map = questionService.getPageQuestionList(pageParam,questionVo);
        return R.ok().data(map);//链式编程，可以连续调用自身的方法
    }


    @ApiOperation("新增问答")
    @PostMapping("addQuestion")
    public R addQuestion(@RequestBody Question question, HttpServletRequest request){
        String memberId = JwtUtils.getMemberIdByJwtToken(request); //1根据token字符串获取会员id，也可获取用户的其他信息
        if(StringUtils.isEmpty(memberId)) { //StringUtils：是用来操作String类的
            return R.error().code(28004).message("请登录"); // 返回错误代码28004，清除ticket信息并跳转到登录页面
        }
        //2补充评论数据，包括用户信息、课程信息，然后加到数据库中
        question.setMemberId(memberId);//会员id
        //3通过服务调用，获取到登录用户信息
        UcenterMemberVo ucenterInfo = ucenterClient.getInfoUc(memberId);
        //4继续赋值
        question.setNickname(ucenterInfo.getNickname());//昵称
        question.setAvatar(ucenterInfo.getAvatar());//头像

        //5、保存到数据库
        boolean save = questionService.save(question);
        if (save) {
            return R.ok().message("新增问答成功!");
        }else {
            return R.error().message("新增问答失败!");
        }
    }

    @ApiOperation("根据问答id回显/查询数据")
    @GetMapping("getQuestionInfo/{questionId}")
    public R getQuestionInfo(@PathVariable String questionId){
        Question question = questionService.getById(questionId);
        return R.ok().data("questionInfo",question);
    }

    @ApiOperation("修改问答")
    @PostMapping("updateQuestionInfo")
    public R updateQuestionInfo(@RequestBody Question question){
        questionService.updateById(question);
        return R.ok();
    }


    //TODO 删除问答同时删除oss中的资源,同时还要去评论表中删除评论
    @ApiOperation(value = "根据ID删除问答")
    @DeleteMapping("deleteQuestionById/{questionId}")
    public R deleteQuestionById(@ApiParam(name = "questionId", value = "文章ID", required = true)
                               @PathVariable String questionId){
        boolean result = questionService.removeById(questionId);
        if (result) {
            return R.ok().message("删除问答成功!");
        }else{
            return R.error().message("删除问答失败!");
        }
    }

    @ApiOperation(value = "查询指定用户分页问答列表")
    @GetMapping("pageUserQuestionList/{current}/{limit}")
    public R pageUserQuestionList(@ApiParam(name = "current",value = "当前页码",required = true)
                                 @PathVariable Long current,
                                 @ApiParam(name = "limit", value = "每页记录数",required = true)
                                 @PathVariable Long limit ,
                                 HttpServletRequest request){
        String memberId = JwtUtils.getMemberIdByJwtToken(request); //1根据token字符串获取会员id，也可获取用户的其他信息
        if(StringUtils.isEmpty(memberId)) { //StringUtils：是用来操作String类的
            return R.error().code(28004).message("请登录"); // 返回错误代码28004，清除ticket信息并跳转到登录页面
        }
        Page<Question> pageParam = new Page<>(current, limit); //Page对象

        Map<String,Object> map = questionService.getPageUserQuestionList(pageParam,memberId);
        return R.ok().data(map);//链式编程，可以连续调用自身的方法
    }

}

