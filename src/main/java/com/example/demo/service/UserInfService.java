package com.example.demo.service;

import com.example.demo.entity.UserInf;

public interface UserInfService {

    UserInf verifyLogin(String username, String password);

    int deleteByPrimaryKey(Integer userId);

    int insert(UserInf record);

    int insertSelective(String username,String email,String password,Integer phone);

    UserInf selectByPrimaryKey(Integer userId);

    int updateByPrimaryKeySelective(UserInf record);

    int updateByPrimaryKey(UserInf record);
}
