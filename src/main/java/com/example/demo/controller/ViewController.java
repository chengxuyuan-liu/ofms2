package com.example.demo.controller;


import com.example.demo.entity.*;
import com.example.demo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class ViewController {
    @Autowired
    FileInfServive fileInfServive;
    @Autowired
    DirInfService dirInfService;
    @Autowired
    DeptInfService deptInfService;
    @Autowired
    DeptMemberService deptMemberService;
    @Autowired
    UserInfService userInfService;

    /**
     * 跳转到菜单页面
     **/
    @RequestMapping("/menu")
    public String toMenuView(Map<String,Object> map, HttpSession session){

        UserInf userInf = (UserInf) session.getAttribute("USER_SESSION");
        if(userInf == null)
        {
            return "login";
        }
        List<DeptInf> deptInfList;          //部门List
        //部门
        if (userInf.getUserType() == 1) {
            deptInfList = deptInfService.selectDeptListByUserId(userInf.getUserId());   //个人账户
        }else{
            deptInfList = deptInfService.selectDeptListByUserId(userInf.getUserId());   //团队账户
        }
        for (DeptInf deptInf : deptInfList) {
            System.out.println(deptInf.getDeptName());

        }

        map.put("deptList",deptInfList);
        return "menu";
    }


    /**
     * 文件管理页面主要数据
    **/
    @RequestMapping("/main")
    public String mainView(Integer dirId, Map<String,Object> map, ModelAndView a, HttpSession session){
        //调用业务
        List<DirInf> dirInfList;          //文件夹List
        List<FileInf> fileInfList;         //文件List
        List<DirInf> accessPath = null;     //访问路径


        UserInf userInf = (UserInf) session.getAttribute("USER_SESSION");
        if(userInf == null)
        {
            return "login";
        }

        //如果dirId为空
        if(dirId != null)
        {
            //获得文件id为dirId文件夹下的文件夹、文件
            dirInfList = dirInfService.selectDirListByDirId(dirId);
            fileInfList = fileInfServive.selectFileListByFolderId(dirId);
            //获得父文件夹
//            DirInf fatherFolder = dirInfService.selectFatherFolderById(
//                    dirInfService.selectFolderById(folderId).getParentFolder());
//            model.addAttribute("fatherFolder",fatherFolder);

            //导航路径
            accessPath = dirInfService.selectParentDirByDirId(dirId);

        }
        else
        {
            //获得根文件夹
            DirInf rootDir= dirInfService.selectRootChildrenDirByUserId(userInf.getUserId(),"我的文件");
            dirId = rootDir.getDirId();
            //获得根文件夹下的文件夹
            dirInfList = dirInfService.selectDirListByDirId(rootDir.getDirId());
            //获得根文件夹下的文件
            fileInfList = fileInfServive.selectFileListByFolderId(rootDir.getDirId());
            //导航路径
            accessPath = dirInfService.selectParentDirByDirId(rootDir.getDirId());
        }


        //返回
        map.put("dirId",dirId);
        map.put("fileList",fileInfList);
        map.put("dirList",dirInfList);
        map.put("accessPath",accessPath);
        return "file_manage";

    }

    /**
     * 查询结果
     **/
    @RequestMapping("/query")
    public String queryResult(String queryName,Map<String,Object> map,HttpSession session) {

        //获得当前用户
        UserInf userInf = (UserInf) session.getAttribute("USER_SESSION");

        //文件
        List<FileInf> fileInfList = fileInfServive.selectByFileName(queryName,userInf.getUserId());
        //文件夹
        List<DirInf> dirInfList = dirInfService.selectByDirName(queryName,userInf.getUserId());

        DirInf dirInf = new DirInf();
        dirInf.setDirName("《=\""+queryName+"\""+"搜索结果");
        List<DirInf> accessPath = new ArrayList<>();
        accessPath.add(dirInf);

        //返回map
        map.put("fileList",fileInfList);
        map.put("dirList",dirInfList);
        map.put("accessPath",accessPath);

        return "file_manage";
    }

    /**
     * 成员管理页面
     **/
    @RequestMapping("/deptMemberView")
    public String deptMember(Integer deptId , Map<String,Object> map , HttpSession session) {

        //调用业务
        //通过部门Id获得部门成员List
        List<Member> memberList = deptMemberService.selectListByDeptKey(deptId);
        DeptInf deptInf = deptInfService.selectByPrimaryKey(deptId);


        map.put("dept",deptInf);
        map.put("memberList",memberList);

        return "dept_member";
    }


    /**
     * 成员管理页面-->待分配
     **/
    @RequestMapping("/toBeAssigned")
    public String toBeAssigned(Map<String,Object> map ,HttpSession session) {
        UserInf userInf = (UserInf) session.getAttribute("USER_SESSION");
        List<Member> memberList = deptMemberService.selectToBeAssignedMemberListByDeptKey(userInf.getUserId());

        List<DeptInf> deptInfList = deptInfService.selectDeptListByUserId(userInf.getUserId());
        map.put("memberList",memberList);
        map.put("deptInfList",deptInfList);
        return "to_be_assigned";
    }

        /*
    待分配-搜索
    */
    @RequestMapping("/toBeAssignedSearch")
    public String toBeAssignedSearch(Integer searchType,String userName,String phone,Map<String,Object> map,HttpSession session) {
        UserInf userInf = (UserInf) session.getAttribute("USER_SESSION");
        List<Member> memberList = new ArrayList<>();
        if(searchType==1){
            System.out.println("用户名："+userName);
            memberList = deptMemberService.selectToBeAssignedMemberByUserName(userInf.getUserId(),userName);
        }else{
            System.out.println("手机号码："+phone);
            memberList = deptMemberService.selectToBeAssignedMemberByPhone(userInf.getUserId(),phone);
        }
        map.put("memberList",memberList);
        return "to_be_assigned";
    }



    /**
     * 管理用户界面
     **/
    @RequestMapping("/toAdminstrationUser")
    public String toAdminstrationUser(Map<String,Object> map ,HttpSession session) {
        UserInf userInf = (UserInf) session.getAttribute("USER_SESSION");
        List<UserInf> userInfList = userInfService.selectAll();
        map.put("userInfList",userInfList);
        return "adminstration_user";
    }





}
