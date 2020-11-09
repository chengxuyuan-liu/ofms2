package com.example.demo.service;


import com.example.demo.entity.DeptInf;
import com.example.demo.entity.UserInf;

import java.util.List;

public interface DeptInfService {

    /*
    新建部门
    */
    public Boolean newDept(String deptName,UserInf userInf);


    public DeptInf selectByPrimaryKey(Integer deptId);
    List<DeptInf> selectDeptListByUserId(Integer userId);

}
