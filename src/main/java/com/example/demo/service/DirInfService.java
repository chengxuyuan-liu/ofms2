package com.example.demo.service;

import com.example.demo.entity.DirInf;
import com.example.demo.entity.UserInf;

import java.util.List;

public interface DirInfService {
    //查询
    DirInf selectRootDirByUserId(Integer userId);
    List<DirInf> selectDirListByDirId(Integer dirId);
    List<DirInf> selectParentDirByDirId(Integer dirId);

    //插入
    int insertSelective(String dirName, Integer dirId, UserInf user);

    //删除
    int deleteByPrimaryKey(Integer dirId);
}
