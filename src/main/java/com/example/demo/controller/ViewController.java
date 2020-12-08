package com.example.demo.controller;


import com.example.demo.entity.*;
import com.example.demo.service.*;
import com.example.demo.vo.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
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
    FileCabinetService fileCabinetService;
    @Autowired
    DeptMemberService deptMemberService;
    @Autowired
    UserInfService userInfService;
    @Autowired
    DepartmentService departmentService;
    @Autowired
    TeamService teamService;

    /**
     * 跳转到菜单页面
     **/
    @RequestMapping("/menu")
    public String toMenuView(Map<String,Object> map, HttpSession session){
        //当前用户
        UserInf userInf = (UserInf) session.getAttribute("USER_SESSION");
        //获得用户或用户所在部门的文件柜
        List<FileCabinet> fileCabinetList = fileCabinetService.selectByUserId(userInf.getUserId());
        //如果是团队账号,获取部门列表
        if(userInf.getUserType() == 2){
            List<Department> departmentList = departmentService.selectDeptListByUserId(userInf.getUserId());
            map.put("deptList", departmentList);
        }

        //如果是个人账号--》团队、部门
        if(userInf.getUserType() == 1){
            DeptMember deptMember=deptMemberService.selectByUserKey(userInf.getUserId()); //用户的成员信息
            Team team = teamService.selectByPrimaryKey(deptMember.getTeamId());  //用户所属团队
            Department department = departmentService.selectByPrimaryKey(deptMember.getDeptId()); //用户所属部门
            map.put("team",team);
            map.put("department", department);

        }
        map.put("fileCabinetList", fileCabinetList);

        return "menu";
    }


    /**
     * 文件管理页面主要数据
    **/
    @RequestMapping("/main")
    public String mainView(Integer dirId,Map<String,Object> map,HttpSession session){

        //调用业务
        List<DirInf> dirInfList;          //文件夹List
        List<FileInf> fileInfList;         //文件List
        List<DirInf> accessPath = null;     //访问路径
        DeptMember deptMember = null;

        FileCabinet fileCabinet;//当前文件的文件柜
        UserInf userInf = (UserInf) session.getAttribute("USER_SESSION");

        //如果dirId为空
        if(dirId != null)
        {
            //导航路径
            accessPath = dirInfService.selectParentDirByDirId(dirId);
            DirInf dirInf = dirInfService.selectByPrimaryKey(dirId);
            if(userInf.getUserId() == dirInf.getUserId() ) {
                fileCabinet = fileCabinetService.selectByDirId(accessPath.get(0).getDirId());
                map.put("fileCabinet", fileCabinet);
            }else{
                deptMember = deptMemberService.selectByUserKey(userInf.getUserId());
                map.put("deptMember", deptMember);
            }

            //获得文件id为dirId文件夹下的文件夹、文件
            dirInfList = dirInfService.selectDirListByParentDirId(dirId);
            fileInfList = fileInfServive.selectFileListByFolderId(dirId);

        }
        else
        {
            //获得根文件夹
            DirInf rootDir= dirInfService.selectRootChildrenDirByUserId(userInf.getUserId(),"我的文件");
            dirId = rootDir.getDirId();
            fileCabinet  = fileCabinetService.selectByDirId(rootDir.getDirId());
            //获得根文件夹下的文件夹
            dirInfList = dirInfService.selectDirListByParentDirId(rootDir.getDirId());
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

        //文件柜
        //FileCabinet fileCabinet = fileCabinetService.selectByDirId();
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


        //通过部门Id获得部门成员List
        List<Member> memberList = deptMemberService.selectListByDeptKey(deptId);
        //部门文件柜
        FileCabinet fileCabinet = fileCabinetService.selectByDeptId(deptId);

        //返回
        map.put("fileCabinet", fileCabinet);
        map.put("memberList",memberList);
        map.put("deptId", deptId);

        return "dept_member";
    }


    /**
     * 成员管理页面-->待分配
     **/
    @RequestMapping("/toBeAssigned")
    public String toBeAssigned(Map<String,Object> map ,HttpSession session) {
        UserInf userInf = (UserInf) session.getAttribute("USER_SESSION");


        List<Member> memberList = deptMemberService.selectToBeAssignedMemberListByUserId(userInf.getUserId());

        List<Department> departmentList = departmentService.selectDeptListByUserId(userInf.getUserId());


        Team team = teamService.selectByUserId(userInf);
        List<Department> department = departmentService.selectByTeamId(team.getTeamId());

        List<FileCabinet> fileCabinetList = new ArrayList<>();
        for (Department department1 : department) {
            fileCabinetList.add( fileCabinetService.selectByDeptId(department1.getDeptId()));
        }


        map.put("memberList",memberList);
        map.put("deptInfList", departmentList);
        map.put("fileCabinetList", fileCabinetList);
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
