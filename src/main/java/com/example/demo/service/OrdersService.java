package com.example.demo.service;

import com.example.demo.entity.Orders;
import com.example.demo.entity.PageRequest;
import com.example.demo.entity.PageResult;
import com.example.demo.entity.UserInf;

public interface OrdersService {
    /*
     * 购买成功后添加订单
     * */
    String add(Orders orders, UserInf userInf);

    /*
     *后台查询订单,分页
     * */
    PageResult findPage(PageRequest pageRequest);

    /*
     * 删除订单
     * */
    String delete(String ordersId);
}
