package com.example.demo.controller;


import com.example.demo.service.ShareDetailService;
import com.example.demo.service.ShareingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/shareing")
@Controller
public class ShareingController {

    @Autowired
    ShareingService shareingService;


    //显示
    @ResponseBody
    @RequestMapping("/all")
    public void all(){

    }

    //搜索
    @ResponseBody
    @RequestMapping("/search")
    public void search(){

    }

    //添加共享
    @ResponseBody
    @RequestMapping("/add")
    public void add(){

    }

    //删除共享
    @ResponseBody
    @RequestMapping("/delete")
    public void delete(){

    }
}
