package com.example.demo.controller;

import com.example.demo.entity.UserInf;
import com.example.demo.service.FileCabinetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;


@Controller
public class FileCabinetController {
    @Autowired
    FileCabinetService fileCabinetService;

    @Autowired
    private JavaMailSender javaMailSender;

    /*
    新建文件柜
    */
    @ResponseBody
    @RequestMapping("/newFileCabinet")
    public String newFileCabinet(String deptName,HttpSession session){

        UserInf userInf = (UserInf) session.getAttribute("USER_SESSION");
        //调用业务
//        Boolean result = fileCabinetService.newFileCabinet(deptName,userInf);
//        if(result) return "OK";
//        return "FALSE";
        return null;
    }

    /*
    解散文件柜
    */
    @ResponseBody
    @RequestMapping("/deleteFileCabinet")
    public String deleteDept(Integer deptId){
        //调用业务
       boolean result = fileCabinetService.deleteDept(deptId);
       if(result) return "OK";
        return "FALSE";
    }


    /*
    编辑文件柜
    */
    @ResponseBody
    @RequestMapping("/editFileCabinet")
    public String editDept(String deptName,String maxSpace,Integer deptId){
        //调用业务
        if(fileCabinetService.updateByPrimaryKeySelective(deptId,deptName,maxSpace,null,null)) return "OK";
        return "FALSE";
    }







}
