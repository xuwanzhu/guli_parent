package com.atguigu.eduservice.controller.front;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.commonutils.ordervo.CourseWebVoOrder;
import com.atguigu.eduservice.client.OrderClient;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.entity.chapter.ChapterVo;
import com.atguigu.eduservice.entity.chapter.VideoVo;
import com.atguigu.eduservice.entity.frontvo.CourseQueryVo;
import com.atguigu.eduservice.entity.frontvo.CourseWebVo;
import com.atguigu.eduservice.entity.subject.OneSubject;
import com.atguigu.eduservice.entity.vo.CourseInfoVo;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.service.EduSubjectService;
import com.atguigu.eduservice.service.EduVideoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
//@CrossOrigin
@RequestMapping("/eduservice/coursefront")
@Api(description = "前台页面课程管理")
public class CourseFrontController {

    @Autowired
    private EduCourseService eduCourseService;

    @Autowired
    private EduSubjectService eduSubjectService;

    //章节
    @Autowired
    private EduChapterService eduChapterService;

    @Autowired
    private EduVideoService eduVideoService;

    @Autowired
    private OrderClient orderClient;

    //1、分页课程列表
    @ApiOperation(value = "条件查询带分页课程列表")
    @PostMapping("getPageCourseList/{page}/{limit}")
    public R getPageCourseList(@ApiParam(name = "page", value = "当前页码", required = true)
                                   @PathVariable Long page,
                               @ApiParam(name = "limit", value = "每页记录数", required = true)
                                   @PathVariable Long limit,
                               @ApiParam(name = "courseQuery", value = "查询对象", required = false)
                                   @RequestBody(required = false) CourseQueryVo courseQuery){

        Page<EduCourse> pageParam = new Page<>(page, limit); //创建分页查询对象
        Map<String,Object> map = eduCourseService.pageListWeb(pageParam,courseQuery);//封装成map并返回
        return R.ok().data(map);
    }


    //2、前台单科课程的详情信息
    @ApiOperation(value = "根据课程ID查询课程")
    @GetMapping("/getCourseFrontInfo/{courseId}")
    private R getCourseFrontInfo(@ApiParam(name = "courseId", value = "课程ID", required = true)
    @PathVariable String courseId, HttpServletRequest request){
        //1查询课程信息和讲师信息,涉及到多表联查，故需要自己写SQL语句
        CourseWebVo courseWebVo = eduCourseService.selectInfoWebById(courseId);
        //2查询当前课程的章节信息
        List<ChapterVo> chapterVoList = eduChapterService.getChapterVideoBycourseId(courseId);
        //3、远程调用，判断课程是否被购买.根据课程id和用户id，查询课程是否已经支付过？
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        boolean buyCourse = orderClient.isBuyCourse(memberId, courseId);
        return R.ok().data("course",courseWebVo).data("chapterVoList",chapterVoList).data("isbuyCourse",buyCourse);
    }

    //3、课程支付订单-前台单科课程的详情信息
    @ApiOperation(value = "课程支付订单-根据课程ID查询课程")
    @GetMapping("/getCourseInfoForOrder/{courseId}")
    private CourseWebVoOrder getCourseInfoForOrder(@ApiParam(name = "courseId", value = "课程ID", required = true)
                                 @PathVariable String courseId){
        //1查询课程信息和讲师信息,涉及到多表联查，故需要自己写SQL语句
        CourseWebVo courseWebVo = eduCourseService.selectInfoWebById(courseId); //这里并不是pdf上面的使用的get

        CourseWebVoOrder courseWebVoOrder = new CourseWebVoOrder();

        BeanUtils.copyProperties(courseWebVo,courseWebVoOrder);

        return courseWebVoOrder;
    }

    //4、封装章节信息，返回给播放页面的选择器
    @ApiOperation(value = "播放页面的选择器-根据课程id获取章节信息")
    @GetMapping("/getChapterVideoFrontInfo/{courseId}")
    private R getChapterVideoFrontInfo(@ApiParam(name = "courseId", value = "课程ID", required = true)
                                 @PathVariable String courseId){
        //2查询当前课程的章节信息
        List<ChapterVo> chapterVoList = eduChapterService.getChapterVideoBycourseId(courseId);
        return R.ok().data("chapterVoList",chapterVoList);
    }




    //4、封装章节信息，返回给播放页面的选择器
    @ApiOperation(value = "播放页面的选择器-根据课程id获取章节信息")
    @GetMapping("/getChapterVoListByVideoId/{videoId}")
    private R getChapterVoListByVideoId(@ApiParam(name = "videoId", value = "视频Id", required = true)
                                       @PathVariable String videoId){
        //1、根据视频地址 获取课程id
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("id",videoId);
        EduVideo eduVideo = eduVideoService.getOne(wrapper);
        String courseId = eduVideo.getCourseId();

        //2查询当前课程的章节信息
        List<ChapterVo> chapterVoList = eduChapterService.getChapterVideoBycourseId(courseId);
        return R.ok().data("chapterVoList",chapterVoList);
    }


    @ApiOperation(value = "根据小节id查询小节视频资源id")
    @GetMapping("getVideoSourceIdByVideoId/{videoId}")
    public R getVideoSourceIdByVideoId(@ApiParam(name = "videoId",value = "小节id", required = true)
                                             @PathVariable String videoId){
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("id",videoId);
        EduVideo video = eduVideoService.getOne(wrapper);
        String videoSourceId = video.getVideoSourceId();
        String videoID = video.getId();
        String chapterId = video.getChapterId();

        return R.ok().data("videoId",videoID).data("chapterId",chapterId).data("videoSourceId",videoSourceId);
    }

    @ApiOperation(value = "根据小节id查询课程id")
    @GetMapping("getCourseIdByVideoId/{videoId}")
    public R getCourseIdByVideoId(@ApiParam(name = "videoId",value = "小节id", required = true)
                                       @PathVariable String videoId){
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("id",videoId);
        EduVideo video = eduVideoService.getOne(wrapper);
        String courseId = video.getCourseId();

        return R.ok().data("courseId",courseId);
    }



    @ApiOperation(value = "根据课程分类id查询课程分类的序号-用来样式显示")
    @GetMapping("getIndexBySubjectId/{subjectId}")
    public R getIndexBySubjectId(@ApiParam(name = "subjectId",value = "课程一级分类id", required = true)
                                  @PathVariable String subjectId){
        int index = 0;
        List<OneSubject> subjectList = eduSubjectService.getAllOneTwoSubject();
        for (int i = 0; i <subjectList.size(); i++) {
            if (subjectId.equals(subjectList.get(i).getId())) { //如果前台的分类id 和 后台相等
                index = i;
                break;
            }
        }

        return R.ok().data("index",index);
    }


    @PostMapping("watchPeopleNums/{courseId}")
    public R watchPeopleNums(@ApiParam(name = "courseId",value = "课程id", required = true)
                                 @PathVariable String courseId){
        eduCourseService.updateViewCount(courseId);//课程浏览人数+1
        //必须这样独自写一个方法用来监控浏览人数，用上面的有返回值链.data 会影响浏览数值。
        return R.ok();
    }
}
