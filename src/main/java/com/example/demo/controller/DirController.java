package com.example.demo.controller;

import com.example.demo.entity.DirInf;
import com.example.demo.entity.UserInf;
import com.example.demo.service.DirInfService;
import com.example.demo.service.UserInfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class DirController {

    @Autowired
    DirInfService dirInfService;
    @Autowired
    UserInfService userInfService;

    /*
    新建文件夹
    */
    @RequestMapping("/newDir")
    @ResponseBody
    public String newDir(String dirName, Integer dirId, HttpSession session){
        //获取当前用户
        UserInf user = (UserInf) session.getAttribute("USER_SESSION");
        //判断当前文件夹是否是私人文件夹
        //获得文件夹对象，获得文件夹的所属用户Id, 与当前用户的id对比，根据对比结果规定实参user的值
        DirInf dirInf = dirInfService.selectByPrimaryKey(dirId);
        if(dirInf.getUserId() != user.getUserId()){
            user = userInfService.selectByPrimaryKey(dirInf.getUserId());
        }
        //调用业务层
        /*int result = */dirInfService.insertSelective(dirName,dirId,user);
//        if(result != 0) {
//            return "OK";
//        }
         return "OK";
    }


    /*
    删除文件夹
    */
    @RequestMapping("/deleteDir")
    @ResponseBody
    public String deleteDir(Integer dirId){
        //调用业务层
        Boolean result = dirInfService.deleteByPrimaryKey(dirId); //删除文件和文件夹
        if(result) return "OK";
        return "FALSE";
    }


}
