package com.example.demo.dao;

import com.example.demo.entity.DirShareLevel;

public interface DirShareLevelDao {
    int deleteByPrimaryKey(Integer dslId);

    int insert(DirShareLevel record);

    int insertSelective(DirShareLevel record);

    DirShareLevel selectByPrimaryKey(Integer dslId);

    int updateByPrimaryKeySelective(DirShareLevel record);

    int updateByPrimaryKey(DirShareLevel record);
}