package com.example.demo.controller;

import com.example.demo.entity.UserInf;
import com.example.demo.service.DirInfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class DirController {

    @Autowired
    DirInfService dirInfService;

    @RequestMapping("/newDir")
    @ResponseBody
    public String newDir(String dirName, Integer dirId, HttpSession session){
        //获取当前用户
        UserInf user = (UserInf) session.getAttribute("USER_SESSION");
        //调用业务层
        int result = dirInfService.insertSelective(dirName,dirId,user);
        if(result != 0) {
            return "OK";
        }
        return "FALSE";
    }

    @RequestMapping("/deleteDir")
    @ResponseBody
    public String deleteDir(Integer dirId){
        //调用业务层
        int result = dirInfService.deleteByPrimaryKey(dirId);
        if(result != 0) {
            return "OK";
        }
        return "FALSE";
    }


}
