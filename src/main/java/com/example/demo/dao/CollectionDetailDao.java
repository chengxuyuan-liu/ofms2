package com.example.demo.dao;
import com.example.demo.entity.CollectionDetail;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface CollectionDetailDao {
    int deleteByPrimaryKey(Integer cdId);

    int insert(CollectionDetail record);

    int insertSelective(CollectionDetail record);

    CollectionDetail selectByPrimaryKey(Integer cdId);
    List<CollectionDetail> selectAll(Integer userId);

    int updateByPrimaryKeySelective(CollectionDetail record);

    int updateByPrimaryKey(CollectionDetail record);
}