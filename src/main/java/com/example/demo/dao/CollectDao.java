package com.example.demo.dao;


import com.example.demo.entity.Collect;
import com.example.demo.vo.CollectVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface CollectDao {
    int deleteByPrimaryKey(Integer collectId);

    int insert(Collect record);

    int insertSelective(Collect record);

    Collect selectByPrimaryKey(Integer collectId);
    List<CollectVO> selectAll(Integer memberId);
    List<Collect> selectBycdId(Integer cdId);

    int updateByPrimaryKeySelective(Collect record);
    int updateByPrimaryKey(Collect record);
    int updateByMemberIdAanCdId(String fileName,Integer memberId,Integer cdId);
}