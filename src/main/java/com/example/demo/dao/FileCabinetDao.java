package com.example.demo.dao;

import com.example.demo.entity.FileCabinet;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface FileCabinetDao {
    int deleteByPrimaryKey(Integer deptId);
    int deleteByDirId(Integer dirId);

    int insert(FileCabinet record);
    int insertSelective(FileCabinet record);

    FileCabinet selectByPrimaryKey(Integer fc_Id);
    FileCabinet selectByDirId(Integer dirId);
    List<FileCabinet> selectByUserId(Integer userId);

    int updateByPrimaryKeySelective(FileCabinet record);  //动态
    int updateByPrimaryKey(FileCabinet record);
}