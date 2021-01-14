package com.example.demo.dao;

import com.example.demo.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface OrdersDao {
    int deleteByPrimaryKey(Integer orderId);

    int insert(Orders record);

    int insertSelective(Orders record);

    Orders selectByPrimaryKey(Integer orderId);
    List<Orders> selectOutDateOrders();

    int updateByPrimaryKeySelective(Orders record);

    int updateByPrimaryKey(Orders record);
    int deleteByIdList(List<Integer> id);

    /*
     * 查找分页
     * */
    List<Orders> seletePage();
}