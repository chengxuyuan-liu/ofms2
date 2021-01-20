package com.example.demo.service;

import com.example.demo.entity.BuyRecord;
import com.example.demo.entity.PageRequest;
import com.example.demo.entity.PageResult;
import com.example.demo.entity.UserInf;

public interface BuyRecordService {
    /*
     * 购买成功后添加购买记录
     * */
    String add(BuyRecord buyRecord, UserInf userInf);

    /*
     *后台查询购买记录,分页
     * */
    PageResult findPage(PageRequest pageRequest);

    /*
     * 删除购买记录
     * */
    String delete(String recordId);
}
