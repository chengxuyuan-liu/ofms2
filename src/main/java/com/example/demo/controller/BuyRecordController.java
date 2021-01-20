package com.example.demo.controller;

import com.example.demo.entity.BuyRecord;
import com.example.demo.entity.PageRequest;
import com.example.demo.entity.PageResult;
import com.example.demo.entity.UserInf;
import com.example.demo.service.BuyRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Map;


@Controller
@RequestMapping("/buyRecord")
public class BuyRecordController {
    @Autowired
    BuyRecordService buyRecordService;

    /*
    * 购买成功后添加购买记录
    * */
    @ResponseBody
    @RequestMapping("/add")
    public String add(BuyRecord buyRecord, HttpSession session){
        UserInf userInf = (UserInf) session.getAttribute("USER_SESSION");
        System.out.println(userInf.getUserId());
        return buyRecordService.add(buyRecord,userInf);
    }


    /*
    *后台查询购买记录，分页
    * */
    @RequestMapping("/backgroundList")
    public String backgroundList(PageRequest pageRequest, Map<String,Object> map){
        if(pageRequest.getPageNum() == 0 || pageRequest.getPageSize() == 0){
            pageRequest.setPageNum(1);
            pageRequest.setPageSize(10);
        }
        PageResult pageResult = buyRecordService.findPage(pageRequest);
        map.put("pageResult",pageResult);
        return "adminstration_order";
    }

    /*
    * 删除购买记录
    * */
    @RequestMapping("/delete")
    @ResponseBody
    public String delete(@RequestBody String recordId){
        String result = buyRecordService.delete(recordId);
        return result;
    }
}
