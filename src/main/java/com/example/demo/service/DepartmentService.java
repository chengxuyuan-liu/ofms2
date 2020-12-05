package com.example.demo.service;

import com.example.demo.entity.Department;
import com.example.demo.entity.FileCabinet;
import com.example.demo.entity.Team;

import java.util.List;

public interface DepartmentService {
    int deleteByPrimaryKey(Integer deptId);

    int insert(Department record);

    int insertSelective(FileCabinet fileCabinet, Team team);

    Department selectByPrimaryKey(Integer deptId);
    List<Department> selectDeptListByUserId(Integer userId);

    int updateByPrimaryKeySelective(Department record);

    int updateByPrimaryKey(Department record);
}
