package com.example.demo.service;

import com.example.demo.entity.Permission;
import com.example.demo.vo.PermissionVO;

import java.util.List;


public interface PermissionService {
    int deleteByPrimaryKey(Integer psiId);

    int insert(Permission record);

    int insertSelective(Permission record);

    Permission selectByPrimaryKey(Integer psiId);
    public List<PermissionVO> selectByUserId(Integer userId);
    public PermissionVO selectByPsiId(Integer psiId);
    Permission selectByMemberId(Integer memberId);
    public List<PermissionVO> selectByPhone(String phone);


    int updateByPrimaryKeySelective(Permission record);

    int updateByPrimaryKey(Permission record);
}
