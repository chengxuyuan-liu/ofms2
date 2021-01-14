package com.example.demo.dao;


import com.example.demo.entity.UserInf;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface UserInfDao {

    UserInf selectByUserName(String username);
    List<UserInf> selectListByUsername(String username);
    UserInf selectByPrimaryKey(Integer userId);
    UserInf selectByUserPhone(String userPhone);
    List<UserInf> selectAll();
    UserInf selectByEmail(String email);

    /*
     * 查找分页
     * */
    List<UserInf> seletePage();


    int deleteByPrimaryKey(Integer userId);

    int insert(UserInf record);
    int insertSelective(UserInf record);




    int updateByPrimaryKeySelective(UserInf record);
    int updateByPrimaryKey(UserInf record);
}