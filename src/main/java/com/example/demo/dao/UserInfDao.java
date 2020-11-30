package com.example.demo.dao;


import com.example.demo.entity.UserInf;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface UserInfDao {


    UserInf selectByUsername(String username);
    List<UserInf> selectListByUsername(String username);
    UserInf selectByPrimaryKey(Integer userId);
    UserInf selectByUserPhone(String userPhone);
    List<UserInf> selectAll();
    UserInf selectByEmail(String email);

    int deleteByPrimaryKey(Integer userId);

    int insert(UserInf record);
    int insertSelective(UserInf record);




    int updateByPrimaryKeySelective(UserInf record);
    int updateByPrimaryKey(UserInf record);
}