package com.example.demo.dao;


import com.example.demo.entity.Permission;
import com.example.demo.vo.PermissionVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface PermissionDao {
    int deleteByPrimaryKey(Integer psiId);
    int deleteByMemberId(Integer memberId);


    int insert(Permission record);

    int insertSelective(Permission record);

    Permission selectByPrimaryKey(Integer psiId);
    Permission selectByMemberId(Integer memberId);
    List<PermissionVO> selectByUsername(String username, Integer userId);

    int updateByPrimaryKeySelective(Permission record);

    int updateByPrimaryKey(Permission record);
}