package com.example.demo.dao;

import com.example.demo.entity.Team;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface TeamDao {
    int deleteByPrimaryKey(Integer teamId);

    int insert(Team record);

    int insertSelective(Team record);


    Team selectByPrimaryKey(Integer teamId);
    Team selectByUserId(Integer userId);

    int updateByPrimaryKeySelective(Team record);

    int updateByPrimaryKey(Team record);
}