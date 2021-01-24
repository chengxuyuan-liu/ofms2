package com.example.demo.controller;


import com.example.demo.entity.DirInf;
import com.example.demo.entity.FileInf;
import com.example.demo.entity.ShareDetail;
import com.example.demo.entity.UserInf;
import com.example.demo.service.DirInfService;
import com.example.demo.service.FileInfServive;
import com.example.demo.service.ShareDetailService;
import com.example.demo.service.ShareingService;
import com.example.demo.vo.ShareingVO;
import com.sun.deploy.net.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@RequestMapping("/shareing")
@Controller
public class ShareingController {

    @Autowired
    ShareingService shareingService;
    @Autowired
    FileInfServive fileInfServive;
    @Autowired
    DirInfService dirInfService;
    @Autowired
    ShareDetailService shareDetailService;


    //显示
    @RequestMapping("/all")
    public String all(HttpSession session, Map<String,Object> map){
        UserInf user = (UserInf) session.getAttribute("USER_SESSION");
        List<ShareingVO> shareingVOS = shareingService.all(user);
        map.put("shareingVOS",shareingVOS);
        return "other_distribute";
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

    //删除共享
    @ResponseBody
    @RequestMapping("/shareDownload")
    public void shareDownload(Integer fileId,Integer sdId,HttpServletResponse response, HttpSession session){
        FileInf fileInf  = fileInfServive.selectByPrimaryKey(fileId);
        DirInf dirInf = dirInfService.selectByPrimaryKey(fileInf.getDirId());
        fileInfServive.download(fileId,dirInf.getDirId(),response,session);

        //下载数量+1
        UserInf userInf = (UserInf) session.getAttribute("USER_SESSION");
        shareingService.updateLoadNum(sdId,userInf.getUserId());
    }
}
