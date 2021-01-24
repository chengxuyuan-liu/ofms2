package com.example.demo.dao;

import com.example.demo.entity.ShareDetail;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ShareDetailDao {
    int deleteByPrimaryKey(Integer shareId);

    int insert(ShareDetail record);

    int insertSelective(ShareDetail record);

    ShareDetail selectByPrimaryKey(Integer shareId);
    List<ShareDetail> selectAll(Integer userId);

    int updateByPrimaryKeySelective(ShareDetail record);

    int updateByPrimaryKey(ShareDetail record);
}