package com.example.demo.service;

import com.example.demo.entity.Team;
import com.example.demo.entity.UserInf;


public interface TeamService {
    int deleteByPrimaryKey(Integer teamId);

    int insert(Team record);

    int insertSelective(Team record, UserInf userInf);

    Team selectByPrimaryKey(Integer teamId);
    Team selectByUserId(UserInf userInf);

    int updateByPrimaryKeySelective(Team record);

    int updateByPrimaryKey(Team record);
}
