package com.example.demo.service.impl;

import com.example.demo.dao.DeptInfDao;
import com.example.demo.entity.DeptInf;
import com.example.demo.entity.DirInf;
import com.example.demo.entity.UserInf;
import com.example.demo.service.DeptInfService;
import com.example.demo.service.DeptMemberService;
import com.example.demo.service.DirInfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeptInfServiceImpl implements DeptInfService {

    @Autowired
    private DeptInfDao deptInfDao;
    @Autowired
    private DirInfService dirInfService;
    @Autowired
    private DeptMemberService deptMemberService;

    @Override
    public Boolean newDept(String deptName,UserInf userInf) {

        int result;    //记录结果

        //新建部门同时新建部门文件夹
        DirInf dirInf = dirInfService.selectRootDirByUserId(userInf.getUserId()); //根文件夹
        //调用新建文件夹服务  新部门文件夹：用户根目录+部门名称=== “\” + “开发部"
        result = dirInfService.insertSelective(deptName,dirInf.getDirId(),userInf);
        if(result == 0){ System.out.println("新建部门失败！");  return false; } //判断是否成功新建部门文件夹


        //新部门保存到数据库
        DirInf newDeptDir = dirInfService.selectRootChildrenDirByUserId(userInf.getUserId(),deptName);
        //部门信息
        DeptInf deptInf = new DeptInf();
        deptInf.setDeptName(deptName);
        deptInf.setDirId(newDeptDir.getDirId());
        deptInf.setMaxSpace(userInf.getMaxSpace());
        //插入数据
        result = deptInfDao.insertSelective(deptInf);
        //新建成功
        if (result != 0)  return true;
        //新建失败
        return false;
    }


    /*
    查询
    */
    @Override
    public DeptInf selectByPrimaryKey(Integer deptId) {
        return deptInfDao.selectByPrimaryKey(deptId);
    }

    @Override
    public List<DeptInf> selectDeptListByUserId(Integer userId) {
        List<DeptInf> deptInfList = deptInfDao.selectDeptListByUserId(userId);
        return deptInfList;
    }



    /*
    解散部门
    */
    @Override
    public Boolean deleteDept(Integer deptId) {

        //修改部门id为deptId的成员，把该部门的成员都分配到 “待分配”,把该成员的部门设置NULL
        if(!deptMemberService.updateDeptById(null,deptId)) return false;
        //获取部门信息，删除该id为dirId的文件夹
        DeptInf deptInf = deptInfDao.selectByPrimaryKey(deptId);      //获取部门信息
        if(deptInfDao.deleteByPrimaryKey(deptId) == 0) return false;
        if(!dirInfService.deleteByPrimaryKey(deptInf.getDirId())) return false;
        return true;
    }

    @Override
    public int deleteByUserId(Integer userId) {

        //查询部门文件夹
        //dirInfService.se

        //int result = deptInfDao.deleteByDirId(dirId);
        return 0;
    }

    @Override
    public Boolean updateByPrimaryKeySelective(String deptName,Integer maxSpace,Integer deptId) {

        DeptInf deptInf = new DeptInf();
        deptInf.setDeptId(deptId);
        deptInf.setDeptName(deptName);
        deptInf.setMaxSpace(maxSpace);

        if(deptInfDao.updateByPrimaryKeySelective(deptInf) == 0) return false;

        return true;
    }

}
