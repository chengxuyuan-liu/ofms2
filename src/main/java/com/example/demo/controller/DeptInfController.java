package com.example.demo.controller;

import com.example.demo.entity.UserInf;
import com.example.demo.service.DeptInfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;


@Controller
public class DeptInfController {
    @Autowired
    DeptInfService deptInfService;

    @Autowired
    private JavaMailSender javaMailSender;

    /*
    新建部门
    */
    @ResponseBody
    @RequestMapping("/newDept")
    public String newDept(String deptName,HttpSession session){

        UserInf userInf = (UserInf) session.getAttribute("USER_SESSION");
        //调用业务
        Boolean result = deptInfService.newDept(deptName,userInf);
        if(result) return "OK";
        return "FALSE";
    }

    /*
    解散部门
    */
    @ResponseBody
    @RequestMapping("/deleteDept")
    public String deleteDept(Integer deptId){
        //调用业务
       boolean result = deptInfService.deleteDept(deptId);
       if(result) return "OK";
        return "FALSE";
    }


    /*
    编辑部门
    */
    @ResponseBody
    @RequestMapping("/editDept")
    public String editDept(String deptName,Integer maxSpace,Integer deptId){
        //调用业务
        if(deptInfService.updateByPrimaryKeySelective(deptName,maxSpace,deptId)) return "OK";
        return "FALSE";
    }







}
