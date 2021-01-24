package com.example.demo.service;

import com.example.demo.entity.CollectionDetail;
import java.util.List;

public interface CollectionDetailService {

    /*
     * 添加收集记录
     * */
    String add(Integer dirId, String JsonMemberId, Integer memberNum, String describe, String datetime,Integer userId);

    List<CollectionDetail> all(Integer userId);


    /*
    * 删除
    * */
    String delete(String[] cdId);

    /*
     * 删除
     * */
    Boolean checkDeadline(Integer cdId);

    /*
    * 编辑时间
    * */
    /*
     * 检查截止时间
     * */
    String eiditDeadline(String deadline, Integer cdId);
}
