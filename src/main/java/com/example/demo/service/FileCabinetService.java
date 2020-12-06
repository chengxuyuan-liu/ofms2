package com.example.demo.service;


import com.example.demo.entity.*;

import java.math.BigInteger;
import java.util.List;

public interface FileCabinetService {

    /*
    新建部门
    */
    public FileCabinet newFileCabinet(Department dept, UserInf userInf,DirInf newDeptDir); //通过新建部门创建文件柜
    public Boolean newFileCabinet(DirInf dirInf, UserInf userInf);  //通过新建用户创建文件柜


    public FileCabinet selectByDeptId(Integer deptId);

    FileCabinet selectByDirId(Integer dirId);
    List<FileCabinet> selectByUserId(Integer userId);

    public Boolean deleteDept(Integer deptId);

    public int deleteByUserId(Integer userId);

    Boolean updateByPrimaryKeySelective(Object... param);
    public Boolean updateWhenEditMember(FileCabinet fileCabinet, DeptMember member,BigInteger maxSpace);
    public Boolean updateWhenNewMember(FileCabinet fileCabinet);
    public Boolean updateWhenDeleteMember(FileCabinet fileCabinet, DeptMember member);

}
