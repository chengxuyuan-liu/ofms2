package com.example.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.entity.DirInf;
import com.example.demo.entity.FileInf;
import com.example.demo.entity.UserInf;
import com.example.demo.service.CheckPermissionsService;
import com.example.demo.service.DirInfService;
import com.example.demo.service.FileInfServive;
import com.example.demo.service.UserInfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Iterator;
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
    @Autowired
    CheckPermissionsService checkPermissionsService;
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
            //检查上传权限，如果返回为false；
            if(!checkPermissionsService.checkUploadPremission(user)){
                return "您没有创建文件夹权限!";
            }
            user = userInfService.selectByPrimaryKey(dirInf.getUserId());
        }
        //调用业务层
        /*int result = */dirInfService.insertSelective(dirName,dirId,user);
         return "创建成功！";
    }


    /*
    删除文件夹
    */
    @RequestMapping("/deleteDir")
    @ResponseBody
    public String deleteDir(Integer dirId,HttpSession session){
        UserInf userInf = (UserInf) session.getAttribute("USER_SESSION");
        //校验操作合法性
        DirInf dirInf = dirInfService.selectByPrimaryKey(dirId);
        if(!dirInf.getUserId().equals(userInf.getUserId())){
            List<FileInf> fileInfs = fileInfServive.selectFileListByFolderId(dirId);
            for (FileInf fileInf : fileInfs) {
                if(!fileInf.getUserId().equals(userInf.getUserId())){
                    return "删除失败！该文件夹已关联其他成员！";
                }
            }
        }
        //调用业务层
        Boolean result = dirInfService.deleteByPrimaryKey(dirId); //删除文件和文件夹
        if(result) return "删除成功！";
        return "删除失败！";
    }


    /*
    * 获得文件夹目录
    * */
    @RequestMapping("/getDirListAadMove")
    @ResponseBody
    public String getDirListAadMove(Map<String,Object> map,Integer dirId,Integer parentId){
        JSONArray tree = new JSONArray();   //json数组

        System.out.println(dirId);
        System.out.println(parentId);


        List<DirInf> list = dirInfService.selectChildrenDirByDirId(parentId);  //所有子文件夹
        List<DirInf> list2 = dirInfService.selectChildrenDirByDirId(dirId);  //所有子文件夹
        Iterator<DirInf> di = list.iterator();

        while (di.hasNext()){
            DirInf dirInf = di.next();
            for (DirInf Inf : list2) {
                //
                if (dirInf.getDirId().equals(Inf.getDirId())) {
                    di.remove();
                }
            }
        }

        JSONObject obj;
        for(DirInf resOwner : list){
            obj = new JSONObject();
            obj.put("id", resOwner.getDirId());
            obj.put("isParent", true);
            obj.put("pId", resOwner.getParentDir());
            obj.put("name", resOwner.getDirName());

            tree.add(obj);
        }
        map.put("success", new Boolean(true));
        map.put("data", tree);


        return tree.toJSONString();
    }


    /*
    * 移动文件夹
    * */
    @RequestMapping("/moveDirTo")
    @ResponseBody
    public String moveTo(Integer dirId,Integer fileId,Integer parentId,Map<String,Object> map,HttpSession session){

        UserInf userInf = (UserInf) session.getAttribute("USER_SESSION");

        if(dirId != null){
            DirInf dirInf = dirInfService.selectByPrimaryKey(dirId);
            DirInf pDir = dirInfService.selectByPrimaryKey(dirInf.getParentDir()); //移动对象所在文件夹
            //校验操作合法性
            if(!pDir.getUserId().equals(userInf.getUserId())){
                List<FileInf> fileInfs = fileInfServive.selectFileListByFolderId(dirId);
                for (FileInf fileInf : fileInfs) {
                    if(!fileInf.getUserId().equals(userInf.getUserId())){
                        return "操作失败！文件夹已关联其他成员";
                    }
                }
            }
        }

//        DirInf pDir;   //移动对象所在文件夹
//        if(dirId != null){
//            DirInf dirInf = dirInfService.selectByPrimaryKey(dirId);
//            pDir = dirInfService.selectByPrimaryKey(dirInf.getParentDir());
//        } else{
//            FileInf fileInf = fileInfServive.selectByPrimaryKey(fileId);
//            pDir = dirInfService.selectByPrimaryKey(fileInf.getDirId());
//        }
//
//        if(!pDir.getUserId().equals(userInf.getUserId())){
//            //检查上传权限，如果返回为false；
//            if(checkPermissionsService.checkUploadPremission(userInf)){
//             return "您没有移动权限!";
//            }
//        }

        //
        if(dirId != null){
            return dirInfService.updateByPrimaryKeySelective(dirId,parentId);  //移动文件夹
        }
        return fileInfServive.updateByPrimaryKeySelective(fileId,parentId); //移动文件
    }

    /*
    * 修改该文件夹名
    * */
    @RequestMapping("/dirRename")
    @ResponseBody
    public String dirRename(Integer dirId,String dirName,HttpSession session) {
        UserInf userInf = (UserInf) session.getAttribute("USER_SESSION");
        //校验操作合法性
        DirInf pDir = dirInfService.selectByPrimaryKey(dirId); //移动对象所在文件夹
        if(!pDir.getUserId().equals(userInf.getUserId())){
            List<FileInf> fileInfs = fileInfServive.selectFileListByFolderId(dirId);
            for (FileInf fileInf : fileInfs) {
                if(!fileInf.getUserId().equals(userInf.getUserId())){
                    return "操作失败！文件夹已关联其他成员";
                }
            }
        }

        return dirInfService.updateDirName(dirId,dirName);
    }


}
