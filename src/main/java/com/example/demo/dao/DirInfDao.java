package com.example.demo.dao;


import com.example.demo.entity.DirInf;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface DirInfDao {
    //删除
    int deleteByPrimaryKey(Integer dirId);

    //插入
    int insert(DirInf record);
    int insertSelective(DirInf record);

    //查询
    List<DirInf> selectDirListByDirId(Integer dirId);   //查询该文件夹下的所有文件夹
    DirInf selectRootDirByUserId(Integer userId);       //根文件夹
    DirInf selectByPrimaryKey(Integer dirId);           //根据id查询制定文件夹
    List<DirInf> selectParentDirByDirId(Integer dirId); //查询该文件夹的所有 父 文件夹
    List<DirInf> selectChildrenDirByDirId(Integer dirId); //查询该文件夹的所有 子 文件夹

    //修改
    int updateByPrimaryKeySelective(DirInf record);
    int updateByPrimaryKey(DirInf record);
}