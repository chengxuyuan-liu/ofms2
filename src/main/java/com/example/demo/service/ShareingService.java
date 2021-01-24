package com.example.demo.service;

import com.example.demo.entity.UserInf;
import com.example.demo.vo.ShareingVO;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

public interface ShareingService {


    /*
     * 显示所有记录
     * */
    List<ShareingVO> all(UserInf userInf);

    /*
    * 修改下载人数
    * */
    void updateLoadNum(Integer sdId,Integer userId);


}
