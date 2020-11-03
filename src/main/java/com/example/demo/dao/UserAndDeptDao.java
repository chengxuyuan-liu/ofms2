package com.example.demo.dao;


import com.example.demo.entity.UserAndDept;

public interface UserAndDeptDao {
    int deleteByPrimaryKey(Integer id);

    int insert(UserAndDept record);

    int insertSelective(UserAndDept record);

    UserAndDept selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserAndDept record);

    int updateByPrimaryKey(UserAndDept record);
}