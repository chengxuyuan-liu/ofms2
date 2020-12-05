package com.example.demo.service.impl;

import com.example.demo.dao.DepartmentDao;
import com.example.demo.dao.FileCabinetDao;
import com.example.demo.entity.Department;
import com.example.demo.entity.FileCabinet;
import com.example.demo.entity.DirInf;
import com.example.demo.entity.UserInf;
import com.example.demo.service.FileCabinetService;
import com.example.demo.service.DeptMemberService;
import com.example.demo.service.DirInfService;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileCabinetServiceImpl implements FileCabinetService {

    @Autowired
    private FileCabinetDao fileCabinetDao;
    @Autowired
    private DirInfService dirInfService;
    @Autowired
    private DepartmentDao departmentDao;


    //新建文件柜
    @Override
    public FileCabinet newFileCabinet(Department dept, UserInf userInf,DirInf newDeptDir) {

        //部门信息
        FileCabinet fileCabinet = new FileCabinet();
        fileCabinet.setFcName(dept.getDeptName());
        fileCabinet.setDirId(newDeptDir.getDirId());
        //deptInf.setMaxSpace(userInf.getMaxSpace());
        //插入数据
        fileCabinetDao.insertSelective(fileCabinet);
        //新建成功
        return fileCabinet;
    }

    @Override
    public Boolean newFileCabinet(DirInf dirInf, UserInf userInf) {
        int result;    //记录结果
        //部门信息
        FileCabinet fileCabinet = new FileCabinet();
        fileCabinet.setFcName(dirInf.getDirName());
        fileCabinet.setDirId(dirInf.getDirId());
        //deptInf.setMaxSpace(userInf.getMaxSpace());
        //插入数据
        result = fileCabinetDao.insertSelective(fileCabinet);
        //新建成功
        if (result != 0)  return true;
        //新建失败
        return false;
    }



    /*
    查询
    */
    @Override
    public FileCabinet selectByPrimaryKey(Integer deptId) {
        Department department = departmentDao.selectByPrimaryKey(deptId);
        return fileCabinetDao.selectByPrimaryKey(department.getFcId());
    }



    @Override
    public FileCabinet selectByDirId(Integer dirId) {
        FileCabinet fileCabinet =  fileCabinetDao.selectByDirId(dirId);
        return fileCabinet;
    }

    @Override
    public List<FileCabinet> selectByUserId(Integer userId) {
        List<FileCabinet> fileCabinetList = fileCabinetDao.selectByUserId(userId);
        return fileCabinetList;
    }


    /*
    解散文件柜
    */
    @Override
    public Boolean deleteDept(Integer deptId) {


        System.out.println("解散");
        //修改部门id为deptId的成员，把该部门的成员都分配到 “待分配”,把该成员的部门设置NULL
        //deptMemberService.updateDeptById(null,deptId);
        //获取部门信息，删除该id为dirId的文件夹
        FileCabinet fileCabinet = fileCabinetDao.selectByPrimaryKey(deptId);      //获取部门信息
        if(fileCabinetDao.deleteByPrimaryKey(deptId) == 0) return false;
        if(!dirInfService.deleteByPrimaryKey(fileCabinet.getDirId())) return false;
        return true;
    }


    @Override
    public int deleteByUserId(Integer userId) {

        //查询部门文件夹
        //dirInfService.se

        //int result = deptInfDao.deleteByDirId(dirId);
        return 0;
    }

    //更新文件柜
    @Override
    public Boolean updateByPrimaryKeySelective(String deptName,Integer maxSpace,Integer deptId) {

        FileCabinet fileCabinet = new FileCabinet();
        fileCabinet.setFcId(deptId);
        fileCabinet.setFcName(deptName);
        fileCabinet.setMaxSpace(maxSpace);

        if(fileCabinetDao.updateByPrimaryKeySelective(fileCabinet) == 0) return false;

        return true;
    }

}
