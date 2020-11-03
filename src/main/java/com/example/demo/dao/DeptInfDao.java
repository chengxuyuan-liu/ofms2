package com.example.demo.dao;


import com.example.demo.entity.DeptInf;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface DeptInfDao {
    int deleteByPrimaryKey(Integer deptId);

    int insert(DeptInf record);

    int insertSelective(DeptInf record);

    DeptInf selectByPrimaryKey(Integer deptId);

    int updateByPrimaryKeySelective(DeptInf record);

    int updateByPrimaryKey(DeptInf record);
}