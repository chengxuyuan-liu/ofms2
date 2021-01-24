package com.example.demo.service;

import com.example.demo.vo.CollectVO;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface CollectService {
    /*
    * 显示记录
    * */
    List<CollectVO> all(Integer userId);

    /*
     * 上传时收集表动作
     * */
    String upload(String fileName,Integer userId,Integer cdId);
}
