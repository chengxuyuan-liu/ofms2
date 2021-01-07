package com.example.demo.service;

import com.example.demo.entity.Permission;
import com.example.demo.vo.PermissionVO;

import java.util.List;


public interface PermissionService {
    int deleteByPrimaryKey(Integer psiId);
    int deleteByMemberId(Integer memberId);


    int insert(Permission record);

    int insertSelective(Permission record);

    Permission selectByPrimaryKey(Integer psiId);
    List<PermissionVO> selectByUserId(Integer userId);
    PermissionVO selectByPsiId(Integer psiId);
    Permission selectByMemberId(Integer memberId);
    List<PermissionVO> selectByPhone(String phone);
    List<PermissionVO> selectByUsername(String username,Integer userId);


    int updateByPrimaryKeySelective(Permission record);

    int updateByPrimaryKey(Permission record);
}
