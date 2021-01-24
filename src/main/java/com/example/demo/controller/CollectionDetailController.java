package com.example.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.entity.CollectionDetail;
import com.example.demo.entity.DirInf;
import com.example.demo.entity.FileInf;
import com.example.demo.entity.UserInf;
import com.example.demo.service.CollectionDetailService;
import com.example.demo.service.DirInfService;
import com.example.demo.service.ShareDetailService;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.util.List;
import java.util.Map;


@RequestMapping("/collectionDetail")
@Controller
public class CollectionDetailController {
    @Autowired
    CollectionDetailService collectionDetailService;
    @Autowired
    DirInfService dirInfService;
    //显示:我的搜集
    @RequestMapping("/all")
    public String all(HttpSession session, Map<String,Object> map){
        UserInf userInf = (UserInf) session.getAttribute("USER_SESSION"); //当前用户
        //查询我发起的搜集，并返回
        List<CollectionDetail> collectionDetails = collectionDetailService.all(userInf.getUserId());
        map.put("collectionDetails",collectionDetails);
        return "my_collect";
    }

    /*
     * 获取发起共享窗口中需要的所有文件数据
     * */
    @ResponseBody
    @RequestMapping("/getDirList")
    public String getDirListAadFileList(HttpSession session){
        UserInf userInf = (UserInf) session.getAttribute("USER_SESSION"); //当前用户
        JSONArray tree = new JSONArray();   //json数组
        //所有文件夹
        DirInf rootDirInf = dirInfService.selectRootDirByUserId(userInf.getUserId());
        DirInf myDir = dirInfService.selectDirByDirName("我的文件",rootDirInf.getDirId());
        List<DirInf> list = dirInfService.selectChildrenDirByDirId(myDir.getDirId());  //用户根文件夹下子文件夹
        //List<DirInf> list2 = dirInfService.selectChildrenDirByDirId(dirId);  //所有子文件夹

        //构建json数据
        JSONObject obj;
        for(DirInf resOwner : list){
            obj = new JSONObject();
            obj.put("id", resOwner.getDirId());
            obj.put("isParent", true);
            obj.put("pId", resOwner.getParentDir());
            obj.put("name", resOwner.getDirName());
            tree.add(obj);
        }
        return tree.toJSONString();
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
    public String add(Integer dirId,String memberId,Integer memberNum,String describe,String datetime,HttpSession session){
        //添加共享
        UserInf userInf = (UserInf) session.getAttribute("USER_SESSION");
        return collectionDetailService.add(dirId,memberId,memberNum,describe,datetime,userInf.getUserId());
    }

    //删除共享
    @ResponseBody
    @RequestMapping("/delete")
    public String delete(String[] cdId){
        return collectionDetailService.delete(cdId);
    }


    //编辑截止时间
    @ResponseBody
    @RequestMapping("/edit")
    public String edit(String deadline,Integer cdId){
        //根据共享关键字进行模糊查询
        return collectionDetailService.eiditDeadline(deadline,cdId);
    }

}
