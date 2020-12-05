package com.example.demo.service;


import com.example.demo.entity.Department;
import com.example.demo.entity.DirInf;
import com.example.demo.entity.FileCabinet;
import com.example.demo.entity.UserInf;

import java.util.List;

public interface FileCabinetService {

    /*
    新建部门
    */
    public FileCabinet newFileCabinet(Department dept, UserInf userInf,DirInf newDeptDir); //通过新建部门创建文件柜
    public Boolean newFileCabinet(DirInf dirInf, UserInf userInf);  //通过新建用户创建文件柜


    public FileCabinet selectByPrimaryKey(Integer deptId);

    FileCabinet selectByDirId(Integer dirId);
    List<FileCabinet> selectByUserId(Integer userId);

    public Boolean deleteDept(Integer deptId);

    public int deleteByUserId(Integer userId);

    Boolean updateByPrimaryKeySelective(String deptName,Integer maxSpace,Integer deptId);
}
