package com.example.demo.service;

import com.example.demo.entity.FileCabinet;
import com.example.demo.entity.UserInf;

import javax.jws.soap.SOAPBinding;
import java.math.BigInteger;
import java.util.List;

public interface UserInfService {


    //判断
    Boolean judgeSpace(UserInf userInf);
    //
    UserInf verifyLogin(String username, String password);
    UserInf selectByPrimaryKey(Integer userId);
    UserInf selectByUserPhone(String userPhone);
    List<UserInf> selectListByUserName(String userName);
    UserInf selectByEmail(String email);

    //
    List<UserInf> selectAll();
    //
    int deleteByPrimaryKey(Integer userId);

    //
    int insert(UserInf record);
    UserInf insertSelective(UserInf userInf);


    //
    int updateByPrimaryKeySelective(UserInf record);
    int updateSpaceWhenNewDept(UserInf record);        //新建部门时
    int updateSpaceWhenDeleteDept(UserInf record, FileCabinet fileCabinet); //删除部门时
    int updateSpaceWhenEditDept(UserInf record, FileCabinet fileCabinet, BigInteger maxSpace);
    int updateByPrimaryKey(UserInf record);
}
