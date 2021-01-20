package com.example.demo.dao;

import com.example.demo.entity.BuyRecord;
import com.example.demo.vo.BuyRecordVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface BuyRecordDao {
    int deleteByPrimaryKey(Integer recordId);

    int insert(BuyRecord record);

    int insertSelective(BuyRecord record);

    BuyRecord selectByPrimaryKey(Integer recordId);
    List<BuyRecord> selectOutDateOrders();
    List<BuyRecord> selectByMealId(Integer mealId);

    int updateByPrimaryKeySelective(BuyRecord record);

    int updateByPrimaryKey(BuyRecord record);
    int deleteByIdList(List<Integer> id);

    /*
     * 查找分页
     * */
    List<BuyRecordVO> seletePage();
}