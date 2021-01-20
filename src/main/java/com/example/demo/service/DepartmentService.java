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
    Department selectByFileCabinetId(Integer fcId);
    List<Department> selectByTeamId(Integer teamId);

    /*
    * 检查部门名称是否重名
    * */
    Department selectRepeatDeptName(Department department, Team team);

    int updateByPrimaryKeySelective(Object... param);
    int updateByPrimaryKey(Department record);
}
