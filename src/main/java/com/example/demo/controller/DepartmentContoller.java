package com.example.demo.controller;

import com.example.demo.entity.*;
import com.example.demo.service.DepartmentService;
import com.example.demo.service.DirInfService;
import com.example.demo.service.FileCabinetService;
import com.example.demo.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class DepartmentContoller {


    @Autowired
    FileCabinetService fileCabinetService;
    @Autowired
    DepartmentService departmentService;
    @Autowired
    DirInfService dirInfService;
    @Autowired
    TeamService teamService;


    /*
    * 新建部门
    * */
    @ResponseBody
    @RequestMapping("/newDept")
    public String newDept(Department dept, HttpSession session){
        //当前用户
        UserInf userInf = (UserInf) session.getAttribute("USER_SESSION");

        //调用创建文件夹service
        DirInf dirInf = dirInfService.selectRootDirByUserId(userInf.getUserId()); //获得根目录
        DirInf newDeptDir = dirInfService.insertSelective(dept.getDeptName(),dirInf.getDirId(),userInf); //创建文件夹

        //调用创建文件柜service
        FileCabinet fileCabinet = fileCabinetService.newFileCabinet(dept,userInf,newDeptDir);

        //调用创建部门service
        Team team = teamService.selectByUserId(userInf);
        departmentService.insertSelective(fileCabinet,team);

        return "OK";
    }


    /*
    * 解散部门
    * */
    @ResponseBody
    @RequestMapping("/deleteDept")
    public String deleteDept(Integer deptId){
        return null;
    }


    /*
    * 编辑部门
    * */
    @ResponseBody
    @RequestMapping("/editDept")
    public String editDept(String deptName,Integer maxSpace,Integer deptId){
        //调用业务
        if(false) return "OK";
        return "FALSE";
    }
}
