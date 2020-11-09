package com.example.demo.controller;

import com.example.demo.entity.UserInf;
import com.example.demo.service.DeptInfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;


@Controller
public class DeptInfController {
    @Autowired
    DeptInfService deptInfService;

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
    删除部门
    */
    @ResponseBody
    @RequestMapping("/getDept/{deptId}")
    public String selectByPrimaryKey(@PathVariable Integer deptId){
        String deptName = deptInfService.selectByPrimaryKey(1).getDeptName();
        System.out.println(deptName);
        return deptName;
    }



}
