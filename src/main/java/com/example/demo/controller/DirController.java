package com.example.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import com.example.demo.entity.DirInf;
import com.example.demo.entity.FileInf;
import com.example.demo.entity.UserInf;
import com.example.demo.service.DirInfService;
import com.example.demo.service.FileInfServive;
import com.example.demo.service.UserInfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
public class DirController {

    @Autowired
    DirInfService dirInfService;
    @Autowired
    UserInfService userInfService;
    @Autowired
    FileInfServive fileInfServive;

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

    @RequestMapping("/getDirListAadMove")
    @ResponseBody
    public String getDirListAadMove(Map<String,Object> map,Integer dirId){
        JSONArray tree = new JSONArray();
        List<DirInf> list = dirInfService.selectChildrenDirByDirId(dirId);
        JSONObject obj;
        for(DirInf resOwner : list){
            obj = new JSONObject();
            obj.put("id", resOwner.getDirId());
            obj.put("isParent", true);
            obj.put("pId", resOwner.getParentDir());
            obj.put("name", resOwner.getDirName().length() > 24?resOwner.getDirName().substring(0,24)+"...":resOwner.getDirName());
           //obj.put("icon", "/images/zTreeStandard.png");
            tree.add(obj);
        }
        map.put("success", new Boolean(true));
        map.put("data", tree);


        return tree.toJSONString();
    }

    @RequestMapping("/moveDirTo")
    @ResponseBody
    public String moveTo(Integer dirId,Integer fileId,Integer parentId,Map<String,Object> map){
        //
        if(dirId != null){
            dirInfService.updateByPrimaryKeySelective(dirId,parentId);
        }
        if(fileId != null){
           fileInfServive.updateByPrimaryKeySelective(fileId,parentId);
        }

        return "OK";
    }





}
