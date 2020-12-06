package com.example.demo.service;

import com.example.demo.entity.Department;
import com.example.demo.entity.FileCabinet;
import com.example.demo.entity.Team;
import com.example.demo.entity.UserInf;

import java.util.List;

public interface DepartmentService {
    int deleteByPrimaryKey(Integer deptId);

    int insert(Department record);

    int insertSelective(FileCabinet fileCabinet, Team team);

    Department selectByPrimaryKey(Integer deptId);
    List<Department> selectDeptListByUserId(Integer userId);
    Department selectByFileCabinetId(Integer fcId);
    List<Department> selectByTeamId(Integer teamId);


    int updateByPrimaryKeySelective(Object... param);
    int updateByPrimaryKey(Department record);
}
