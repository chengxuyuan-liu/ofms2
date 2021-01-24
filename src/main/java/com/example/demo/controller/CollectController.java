package com.example.demo.controller;


import com.example.demo.entity.UserInf;
import com.example.demo.service.CollectService;
import com.example.demo.service.CollectionDetailService;
import com.example.demo.service.FileInfServive;
import com.example.demo.vo.CollectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@RequestMapping("/collect")
@Controller
public class CollectController {
    @Autowired
    CollectService collectService;
    @Autowired
    FileInfServive fileInfServive;
    @Autowired
    CollectionDetailService collectionDetailService;

    //显示:他人的收集
    @RequestMapping("/all")
    public String all(HttpSession session, Map<String,Object> map){
        UserInf userInf = (UserInf) session.getAttribute("USER_SESSION"); //当前用户
        //查询我发起的共享，并返回
        List<CollectVO> collectVOS = collectService.all(userInf.getUserId());
        map.put("collects",collectVOS);
        return "other_collect";
    }

    /*
    * 上传收集文件
    * */
    @ResponseBody
    @RequestMapping("/upload")
    public String upload(MultipartFile uploadfile, Integer dirId,Integer cdId, HttpSession session, Map<String,Object> map){
        UserInf userInf = (UserInf) session.getAttribute("USER_SESSION");
        //判断截止时间
        if(collectionDetailService.checkDeadline(cdId)) return "上传失败，搜集已结束！";
        //上传文件(uploadfile,文件夹id,用户实例)
        fileInfServive.fileUpload(uploadfile,dirId,userInf);
        //改变搜集表(状态（s_status）、文件名（file_name）)
        collectService.upload(uploadfile.getOriginalFilename(),userInf.getUserId(),cdId);

        return "上传成功！";
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
