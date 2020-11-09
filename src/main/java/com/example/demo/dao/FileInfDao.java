package com.example.demo.dao;

import com.example.demo.entity.FileInf;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface FileInfDao {

    /*
    删除
    */
    int deleteByPrimaryKey(Integer fileId);
    int deleteByDirId(Integer dirId);

    /*
    插入
    */
    int insert(FileInf record);
    int insertSelective(FileInf record);

    /*
    查询
    */
    List<FileInf> selectFileListByFolderId(Integer dirId);
    FileInf selectByPrimaryKey(Integer fileId);
    List<FileInf> selectByFileName(@Param("fileName")String fileName, @Param("userId")Integer userId);  //模糊查询


    /*
    更新
    */
    int updateByPrimaryKeySelective(FileInf record);
    int updateByPrimaryKey(FileInf record);
}