package com.example.demo.dao;

import com.example.demo.entity.UserLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserLogDao {
    int deleteByPrimaryKey(Integer logId);

    int insert(UserLog record);

    int insertSelective(UserLog record);

    UserLog selectByPrimaryKey(Integer logId);

    int updateByPrimaryKeySelective(UserLog record);

    int updateByPrimaryKey(UserLog record);



    /*
    * 查找分页
    * */
    List<UserLog> seletePage();
}