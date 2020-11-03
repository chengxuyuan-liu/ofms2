package com.example.demo.dao;

import com.example.demo.entity.FileInf;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface FileInfDao {
    int deleteByPrimaryKey(Integer fileId);

    int insert(FileInf record);

    int insertSelective(FileInf record);

    List<FileInf> selectFileListByFolderId(Integer dirId);

    FileInf selectByPrimaryKey(Integer fileId);

    int updateByPrimaryKeySelective(FileInf record);

    int updateByPrimaryKey(FileInf record);
}