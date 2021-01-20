package com.example.demo.controller;


import com.example.demo.entity.UserInf;
import com.example.demo.service.CollectService;
import com.example.demo.service.CollectionDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@RequestMapping("/collect")
@Controller
public class CollectController {
    @Autowired
    CollectService collectService;

    //显示:我的共享
    @ResponseBody
    @RequestMapping("/all")
    public void all(HttpSession session){
        UserInf userInf = (UserInf) session.getAttribute("USER_SESSION"); //当前用户

        //查询我发起的共享，并返回

    }

    //搜索
    @ResponseBody
    @RequestMapping("/search")
    public void search(String shareName){

        //根据共享关键字进行模糊查询


    }

    //添加共享
    @ResponseBody
    @RequestMapping("/add")
    public void add(HttpSession session){
        //添加共享
    }

    //删除共享
    @ResponseBody
    @RequestMapping("/delete")
    public void delete(){
        //
    }
}
