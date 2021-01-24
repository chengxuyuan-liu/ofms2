package com.example.demo.dao;

import com.example.demo.entity.Shareing;
import com.example.demo.vo.ShareingVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;


@Mapper
@Repository
public interface ShareingDao {
    int deleteByPrimaryKey(Integer sdId);

    int insert(Shareing record);

    int insertSelective(Shareing record);

    Shareing selectByPrimaryKey(Integer sdId);
    List<ShareingVO> selectAll(Integer memberId);
    List<Shareing> selectBySdId(Integer sdId);

    int updateByPrimaryKeySelective(Shareing record);

    int updateByPrimaryKey(Shareing record);
}