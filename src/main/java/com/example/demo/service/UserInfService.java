package com.example.demo.service;

import com.example.demo.entity.UserInf;

import java.util.List;

public interface UserInfService {

    UserInf verifyLogin(String username, String password);
    UserInf selectByPrimaryKey(Integer userId);
    UserInf selectByUserPhone(String userPhone);
    List<UserInf> selectListByUserName(String userName);
    UserInf selectByEmail(String email);

    List<UserInf> selectAll();

    int deleteByPrimaryKey(Integer userId);

    int insert(UserInf record);
    UserInf insertSelective(UserInf userInf);

    int updateByPrimaryKeySelective(UserInf record);
    int updateByPrimaryKey(UserInf record);
}
