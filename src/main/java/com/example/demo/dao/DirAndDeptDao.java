package com.example.demo.dao;


import com.example.demo.entity.DirAndDept;

public interface DirAndDeptDao {
    int deleteByPrimaryKey(Integer id);

    int insert(DirAndDept record);

    int insertSelective(DirAndDept record);

    DirAndDept selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DirAndDept record);

    int updateByPrimaryKey(DirAndDept record);
}