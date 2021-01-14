package com.example.demo.controller;


import com.example.demo.entity.*;
import com.example.demo.service.*;
import com.example.demo.vo.MemberVO;
import com.example.demo.vo.PermissionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
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
    @Autowired
    PermissionService permissionService;
    @Autowired
    UserLogService userLogService;

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
            Team team = null;
            Department department = null;
            if(deptMember != null){
                team = teamService.selectByPrimaryKey(deptMember.getTeamId());  //用户所属团队
                department = departmentService.selectByPrimaryKey(deptMember.getDeptId()); //用户所属部门
                map.put("team",team);
                map.put("department", department);
                Permission permission = permissionService.selectByMemberId(deptMember.getId());
                if(permission != null){
                    if(permission.getpAddDept() == 1 || permission.getpEditDept() ==1){
                        List<Department> departmentList = departmentService.selectByTeamId(team.getTeamId());
                        map.put("deptList", departmentList);        //获得团队所有部门

                    }else{
                        map.put("deptList", departmentService.selectByPrimaryKey(deptMember.getDeptId()));
                    }
                }
                //获取权限信息
                map.put("permission", permission);
            }



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
        List<DirInf> originAccessPath = null;     //访问路径
        List<DirInf> accessPath = new ArrayList<>();     //访问路径
        DeptMember deptMember = null;
        String title=null;      //当前文件柜

        FileCabinet fileCabinet;//当前文件的文件柜
        UserInf userInf = (UserInf) session.getAttribute("USER_SESSION");

        //如果dirId为空
        if(dirId != null)
        {
            //没排序的导航路径
            originAccessPath = dirInfService.selectParentDirByDirId(dirId);

            //导航路径排序
            Integer item = dirId;
            for (int i = 0; i < originAccessPath.size(); i++) {
                for (DirInf inf : originAccessPath) {
                    if(inf.getDirId().equals(item)){
                        accessPath.add(0,inf);
                        item = inf.getParentDir();
                    }
                }
            }

            title = accessPath.get(0).getDirName();
            DirInf dirInf = dirInfService.selectByPrimaryKey(dirId);
            if(userInf.getUserId() == dirInf.getUserId() ) {
                fileCabinet = fileCabinetService.selectByDirId(accessPath.get(0).getDirId()); //当前文件柜
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
            map.put("fileCabinet", fileCabinet);
            //获得根文件夹下的文件夹
            dirInfList = dirInfService.selectDirListByParentDirId(rootDir.getDirId());
            //获得根文件夹下的文件
            fileInfList = fileInfServive.selectFileListByFolderId(rootDir.getDirId());
            //导航路径
            accessPath = dirInfService.selectParentDirByDirId(rootDir.getDirId());
            title = accessPath.get(0).getDirName();

        }
        //返回

        map.put("title",title);
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
    public String queryResult(String queryName,Integer dirId,Map<String,Object> map,HttpSession session) {

        //获得当前用户
        UserInf userInf = (UserInf) session.getAttribute("USER_SESSION");

        List<DirInf> originAccessPath = null;     //没有排序的访问路径
        List<DirInf> fatherDir = new ArrayList<>(); //排序的访问路径
        //没排序的导航路径
        originAccessPath = dirInfService.selectParentDirByDirId(dirId);

        //导航路径排序
        Integer item = dirId;
        for (int i = 0; i < originAccessPath.size(); i++) {
            for (DirInf inf : originAccessPath) {
                if(inf.getDirId().equals(item)){
                    fatherDir.add(0,inf);
                    item = inf.getParentDir();
                }
            }
        }

        //当前文件柜
        FileCabinet fileCabinet = fileCabinetService.selectByDirId(fatherDir.get(0).getDirId());

        //所有文件夹
        List<DirInf> dirInfList = dirInfService.selectChildrenDirByDirId(fatherDir.get(0).getDirId()); //所有子文件夹
        List<DirInf> resultDirInf = new ArrayList<DirInf>();    //文件夹搜索结果
        List<FileInf> resultFileInf = new ArrayList<FileInf>();   //文件搜索结果
        for (DirInf dirInf : dirInfList) {
            resultDirInf.addAll(dirInfService.selectByDirName(queryName,dirInf.getDirId()));
            resultFileInf.addAll(fileInfServive.selectListByFileNameAndDirId(queryName,dirInf.getDirId()));
        }

        String title = "\""+queryName+"\""+"搜索结果";
        DirInf dirInf = new DirInf();
        dirInf.setDirId(dirId);
        dirInf.setDirName("返回");
        List<DirInf> accessPath = new ArrayList<>();
        accessPath.add(dirInf);

        //返回map
        map.put("title",title);
        map.put("typeSearch","typeSearch");
        map.put("fileList",resultFileInf);
        map.put("dirList",resultDirInf);
        map.put("accessPath",accessPath);
        map.put("dirId",fileCabinet.getDirId());

        return "file_manage";
    }

    /**
     * 成员管理页面
     **/
    @RequestMapping("/deptMemberView")
    public String deptMember(Integer deptId , Map<String,Object> map , HttpSession session) {

        UserInf userInf = (UserInf) session.getAttribute("USER_SESSION");
        if(userInf.getUserType() == 1) {
            DeptMember deptMember = deptMemberService.selectByUserKey(userInf.getUserId());
            Permission permission = permissionService.selectByMemberId(deptMember.getId());
            map.put("permission", permission);
        }
        //通过部门Id获得部门成员List
        List<MemberVO> memberVOList = deptMemberService.selectListByDeptKey(deptId);
        //部门文件柜
        FileCabinet fileCabinet = fileCabinetService.selectByDeptId(deptId);

        //返回
        map.put("fileCabinet",fileCabinet);
        map.put("memberList", memberVOList);
        map.put("deptId",deptId);

        return "dept_member";
    }


    /**
     * 成员管理搜索
     **/
    @RequestMapping("/searchMember")
    public String searchMember(String userPhone,String userName,Integer deptId,Map<String,Object> map , HttpSession session) {
        Integer userId;
        UserInf userInf = (UserInf) session.getAttribute("USER_SESSION");
        DeptMember deptMember  = deptMemberService.selectByUserKey(userInf.getUserId());

        if(deptMember !=null){
            Team team = teamService.selectByPrimaryKey(deptMember.getTeamId());
            userId = team.getUserId();
        }else{
            userId = userInf.getUserId();
        }

        MemberVO memberVO = null;
        List<MemberVO> memberVOList = new ArrayList<>();
        if(userName != null) {
            //通过用户名进行模糊搜索
            memberVOList =  deptMemberService.selectByUserName(userId,userName);
        }else {
            //通过手机号码搜索
            memberVO = deptMemberService.selectListByPhone(userPhone,deptId);
            if(memberVO != null)
            memberVOList.add(memberVO);
        }


        //部门文件柜
        FileCabinet fileCabinet = fileCabinetService.selectByDeptId(deptId);

        //返回
        map.put("fileCabinet",fileCabinet);
        map.put("deptId",deptId);
        map.put("memberList", memberVOList);
        //返回
        return "dept_member";
    }


    /**
     * 成员管理页面-->待分配
     **/
    @RequestMapping("/toBeAssigned")
    public String toBeAssigned(Map<String,Object> map ,HttpSession session) {
        UserInf userInf = (UserInf) session.getAttribute("USER_SESSION");
        Team team = teamService.selectByUserId(userInf);

        //如果是个人账号
        if(userInf.getUserType() == 1){
            DeptMember deptMember = deptMemberService.selectByUserKey(userInf.getUserId());//通过成员信息获取团队id
            team = teamService.selectByPrimaryKey(deptMember.getTeamId()); //通过团队信息获取用户id
        }

        List<MemberVO> memberVOList = deptMemberService.selectToBeAssignedMemberListByUserId(team.getUserId());  //获取所有待分配成员

        List<Department> departmentList = departmentService.selectDeptListByUserId(team.getUserId());    //获取所有部门，用于编辑时是使用



        List<Department> department = departmentService.selectByTeamId(team.getTeamId());

        List<FileCabinet> fileCabinetList = new ArrayList<>();
        for (Department department1 : department) {
            fileCabinetList.add( fileCabinetService.selectByDeptId(department1.getDeptId()));//获取所有文件柜，用于编辑时是使用
        }


        for (MemberVO memberVO : memberVOList) {
            System.out.println(memberVO.getUserName());
        }

        map.put("memberList", memberVOList);
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
        List<MemberVO> memberVOList = new ArrayList<>();
        if(searchType==1){
            System.out.println("用户名："+userName);
            memberVOList = deptMemberService.selectToBeAssignedMemberByUserName(userInf.getUserId(),userName);
        }else{
            System.out.println("手机号码："+phone);
            memberVOList = deptMemberService.selectToBeAssignedMemberByPhone(userInf.getUserId(),phone);
        }
        map.put("memberList", memberVOList);
        return "to_be_assigned";
    }



    /**
     * 管理用户界面
     **/
    @RequestMapping("/toAdminstrationUser")
    public String toAdminstrationUser(PageRequest pageRequest,Map<String,Object> map ,HttpSession session) {
        UserInf userInf = (UserInf) session.getAttribute("USER_SESSION");
//        List<UserInf> userInfList = userInfService.selectAll();
//        map.put("userInfList",userInfList);

        PageResult pageResult = userInfService.findPage(pageRequest);

        map.put("pageResult",pageResult);
        return "adminstration_user";
    }


    /**
     * 权限管理界面
     **/
    @RequestMapping("/permission")
    public String toPermission(HttpSession session,Map<String,Object> map ) {

        UserInf userInf = (UserInf) session.getAttribute("USER_SESSION");
        List<PermissionVO> permissionVOS = permissionService.selectByUserId(userInf.getUserId());
        List<Department> departmentList = departmentService.selectDeptListByUserId(userInf.getUserId());

        map.put("department",departmentList);
        map.put("permission",permissionVOS);
        return "permission";
    }


    /**
     * 权限管理界面---搜索
     **/
    @RequestMapping("/permissionSearch")
    public String permissionSearch(String userPhone,String username,HttpSession session,Map<String,Object> map ) {

        UserInf userInf = (UserInf) session.getAttribute("USER_SESSION");
        List<PermissionVO> permissionVOS = null;
        //通过手机号查找用户
        if(userPhone != null){
            permissionVOS = permissionService.selectByPhone(userPhone);

        }
        //通过用户名查找用户
        else{
            permissionVOS = permissionService.selectByUsername(username,userInf.getUserId());
        }
        List<Department> departmentList = departmentService.selectDeptListByUserId(userInf.getUserId());
        map.put("department",departmentList);
        map.put("permission",permissionVOS);
        return "permission";
    }









    /**
     * 管理员用户搜索
     **/
    @RequestMapping("/userSearch")
    public String userSearch(Integer searchType,String userName,String phone,Map<String,Object> map,HttpSession session) {

        UserInf userInf = (UserInf) session.getAttribute("USER_SESSION");
        List<UserInf> userInfList = new ArrayList<>();
        if(searchType==1){
            System.out.println("用户名："+userName);
            userInfList = userInfService.selectListByUserName(userName);
        }else{
            System.out.println("手机号码："+phone);
            UserInf userInf1 = userInfService.selectByUserPhone(phone);
            userInfList.add(userInf1);
        }
        map.put("userInfList",userInfList);
        return "adminstration_user";
    }


    @RequestMapping("/toAdminstrationLog")
    public Object  toAdminstrationLog() {
        return "redirect:/userLog?pageNum=1&pageSize=10";
    }



    /**
     * 查看日志
     **/
    @RequestMapping("/userLog")
    public Object  userLog(PageRequest pageQuery,Map<String,Object> map) {
        //获得所有日志
        PageResult pageResult = userLogService.findPage(pageQuery);
        List<UserLog> userLogs = (List<UserLog>) pageResult.getContent();
        map.put("pageRsult",pageResult);
        return "adminstration_log";
    }

    /*
    根据类型查询文件
    */
    @RequestMapping("/searchFile")
    public ModelAndView searchFile(Integer fileType,Integer dirId, ModelAndView modelAndView, HttpSession session){

        UserInf userInf = (UserInf) session.getAttribute("USER_SESSION");

        DirInf nowDirInf =  dirInfService.selectByPrimaryKey(dirId); //当前文件柜的文件夹
        //
        List<FileInf> fileList = null; //请求返回结果
        List<DirInf> accessPath = new ArrayList<>(); //面包屑
        DirInf dirInf = new DirInf();   //
        dirInf.setDirName("返回");
        dirInf.setDirId(dirId);
        String title = null;
        //查询文档
        if(fileType == 1) {
            List<String> list = new ArrayList<String>(Arrays.asList("doc","docx","pptx","xlsx","pdf","txt"));
            fileList = fileInfServive.selectByFileType(list,nowDirInf.getUserId(),dirId);
            title = "文档";
            accessPath.add(dirInf);
        }
        //查询视频
        else if(fileType == 2){
            List<String> list = new ArrayList<String>(Arrays.asList("mp4","wmv","swf","avi","pdf","flv"));
            fileList = fileInfServive.selectByFileType(list,nowDirInf.getUserId(),dirId);
            title = "视频";
            accessPath.add(dirInf);
        }
        //查询音频
        else if(fileType == 3){
            List<String> list = new ArrayList<String>(Arrays.asList("mp3","wma"));
            fileList = fileInfServive.selectByFileType(list,nowDirInf.getUserId(),dirId);
            title = "音频";
            accessPath.add(dirInf);
        }
        //查询图片
        else if(fileType == 4){
            List<String> list = new ArrayList<String>(Arrays.asList("jpg","png","gif","svg"));
            fileList = fileInfServive.selectByFileType(list,nowDirInf.getUserId(),dirId);
            title = "图片";
            accessPath.add(dirInf);
        }

        modelAndView.addObject("typeSearch","typeSearch");
        modelAndView.addObject("title",title);
        modelAndView.addObject("accessPath",accessPath);
        modelAndView.addObject("fileList",fileList);
        modelAndView.setViewName("file_manage");
        return modelAndView;
    }
}
