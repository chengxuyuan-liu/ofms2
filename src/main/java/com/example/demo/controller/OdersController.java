package com.example.demo.controller;

import com.example.demo.entity.Orders;
import com.example.demo.entity.PageRequest;
import com.example.demo.entity.PageResult;
import com.example.demo.entity.UserInf;
import com.example.demo.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/orders")
public class OdersController {
    @Autowired
    OrdersService ordersService;

    /*
    * 购买成功后添加订单
    * */
    @ResponseBody
    @RequestMapping("/add")
    public String add(Orders orders, HttpSession session){
        UserInf userInf = (UserInf) session.getAttribute("USER_SESSION");
        System.out.println(userInf.getUserId());
        return ordersService.add(orders,userInf);
    }


    /*
    *后台查询订单，分页
    * */
    @RequestMapping("/backgroundList")
    public String backgroundList(PageRequest pageRequest, Map<String,Object> map){
        PageResult pageResult = ordersService.findPage(pageRequest);
        map.put("pageResult",pageResult);
        return "adminstration_order";
    }

    /*
    * 删除订单
    * */
    @RequestMapping("/delete")
    @ResponseBody
    public String delete(@RequestBody String ordersId){
        String result = ordersService.delete(ordersId);
        return result;
    }
}
