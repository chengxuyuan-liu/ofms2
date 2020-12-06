package com.example.demo.dao;

import com.example.demo.entity.Department;
import com.example.demo.entity.FileCabinet;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface DepartmentDao {
    int deleteByPrimaryKey(Integer deptId);

    int insert(Department record);

    int insertSelective(Department record);

    Department selectByPrimaryKey(Integer deptId);
    List<Department> selectDeptListByUserId(Integer userId);
    Department selectByFileCabinetId(Integer fcId);
    List<Department> selectByTeamId(Integer teamId);

    int updateByPrimaryKeySelective(Department record);

    int updateByPrimaryKey(Department record);
}