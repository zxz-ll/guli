package com.laity.eduservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.laity.commonutils.R;
import com.laity.eduservice.entity.EduTeacher;
import com.laity.eduservice.entity.vo.TeacherQuery;
import com.laity.eduservice.service.EduTeacherService;
import com.laity.servicebase.exceptionhandler.LaityException;
import io.swagger.annotations.Api;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-04-25
 */
@Api(description = "讲师管理") //swagger的类注解
@RestController
@RequestMapping("/eduservice/edu-teacher")
public class EduTeacherController {
/**
 * 知识总结
 * @RestController 主要包含
 * @ResponseBody:   返回json数据
 * @Controller: 表示交由spring容器管理
 *
 */

/**
 * 项目的整体结构
 * springboot   /删除src目录  1
 *    maven     /删除src目录  2
 *     maven                3
 *    maven
 *
 *
 *  开发步骤
 *  1. 配置springboot启动类 自己建
 *  2.创建配置类，配置mapper扫描和其他
 *  3.写业务代码创建controller
 *  controller中注入service
 *  service中注入mapper   mp框架已经实现
 *
 *
 *  访问接口地址
 *  http://localhost:8001/eduservice/edu-teacher/findAll
 */

    /**
     *
     * {
     *  "success":true/false,         //响应是否成功
     *  "code":数字,                    //响应码
     *  "message":字符串,               //返回消息
     *  "data":HashMap                 //返回数据，放在键值对中
     * }
     *
     * 链式编程: R.ok().code().message()类似这种
     *
     */

   @Autowired
   private EduTeacherService teacherService;

   //查询讲师表的所有数据
   //rest风格

    @ApiOperation(value = "获取所有讲师列表")  //swagger的方法注解
   @GetMapping("findAll")  //表示用get的方式提交
   public R findAllTeacher(){
      List<EduTeacher> list = teacherService.list(null);
      return R.ok().data("items",list);
   }


   //2.逻辑删除讲师的方法
    @ApiOperation(value = "逻辑删除讲师的方法")
    @DeleteMapping("{id}")   //写法含义是 id值通过路径进行传递
    public R removeTeacher(@ApiParam(name = "id",value = "讲师id",required = true) @PathVariable String id){//注解含义得到路径中的id参数
        boolean flag = teacherService.removeById(id);
        if (flag){
            return R.ok();
        }else {
            return R.error();
        }
    } //浏览器提交方式是get或者post  delete方式提交的话不能直接用浏览器   借助swagger测试


    /**
     * 分页查询讲师的方法
     */
    @GetMapping("pageTeacher/{current}/{limit}") //当前页 ，每页条数
    public R pageListTeacher(@PathVariable long current,
                             @PathVariable long limit ) {
        //创建page对象
        Page<EduTeacher> teacherPage=new Page<>(current,limit);
        //调用方法实现分页
        //调用方法的时候，底层封装，把分页所有的数据封装到 teacherPage对象里面
        teacherService.page(teacherPage,null);//page对象和分页条件
        long total = teacherPage.getTotal();  //总记录数
        List<EduTeacher> records = teacherPage.getRecords(); //数据list集合

       //方法一：data(). data(),   return R.ok().data("total",total).data("rows",records);
       //方法二：使用map集合   两个方法的data传入的参数类型不同效果相同
        Map map=new HashMap();
        map.put("total",total);
        map.put("rows",records);
        return R.ok().data(map);
    }

    /**
     *
     * @param current
     * @param limit
     * @param teacherQuery
     * @return
     */
    //4 条件查询带分页的方法
    @PostMapping("pageTeacherCondition/{current}/{limit}")    //使用@RequestBody就要使用post的提交方式
    public R pageTeacherCondition(@PathVariable long current,@PathVariable long limit,
                                  @RequestBody(required = false)  TeacherQuery teacherQuery) {//使用json传数据，将json数据封装 再用对象的形式得到
        //创建page对象              //@RequestBody(required = false) 表示后面的对象可以没有值 参数值可以为空
        Page<EduTeacher> pageTeacher = new Page<>(current,limit);

        //构建条件
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        // 多条件组合查询
        // mybatis学过 动态sql
        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();
        //判断条件值是否为空，如果不为空拼接条件
        if(!StringUtils.isEmpty(name)) {
            //构建条件
            wrapper.like("name",name);
        }
        if(!StringUtils.isEmpty(level)) {
            wrapper.eq("level",level);
        }
        if(!StringUtils.isEmpty(begin)) {
            wrapper.ge("gmt_create",begin); //大于  对应的是表字段
        }
        if(!StringUtils.isEmpty(end)) {
            wrapper.le("gmt_create",end);  //小于
        }

        //排序
        wrapper.orderByDesc("gmt_create");

        //调用方法实现条件查询分页
        teacherService.page(pageTeacher,wrapper);

        long total = pageTeacher.getTotal();//总记录数
        List<EduTeacher> records = pageTeacher.getRecords(); //数据list集合
        return R.ok().data("total",total).data("rows",records);
    }

    //添加讲师接口的方法
    @PostMapping("addTeacher")
    public R addTeacher(@RequestBody EduTeacher eduTeacher) {
        boolean save = teacherService.save(eduTeacher);
        if(save) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    //根据讲师id进行查询
    @GetMapping("getTeacher/{id}")
    public R getTeacher(@PathVariable String id) {
        EduTeacher eduTeacher = teacherService.getById(id);
        try {
            int i=10/0;
        } catch (Exception e) {
            //执行自定义异常
            throw new LaityException(2001," 饿出异常了");
        }
        return R.ok().data("teacher",eduTeacher);
    }

    //讲师修改功能
    @PostMapping("updateTeacher")
    public R updateTeacher(@RequestBody EduTeacher eduTeacher) {
        boolean flag = teacherService.updateById(eduTeacher);
        if(flag) {
            return R.ok();
        } else {
            return R.error();
        }
    }







}

