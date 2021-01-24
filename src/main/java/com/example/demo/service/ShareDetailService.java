package com.example.demo.service;

import com.example.demo.entity.ShareDetail;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

public interface ShareDetailService {

    /*
     * 显示我的共享
     * */
    List<ShareDetail> selectAll(Integer userId);

    /*
     * 添加共享
     * */
    String add(Integer fileId, String memberId, Integer memberNum, String describe,Integer userId);

    /*
     * 删除
     * */
    String delete(String[] sdId);
}
