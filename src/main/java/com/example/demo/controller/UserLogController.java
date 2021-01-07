package com.example.demo.controller;

import com.example.demo.service.UserLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
public class UserLogController {

    @Autowired
    UserLogService userLogService;

    //删除日志记录
    @RequestMapping("/deleteLog")
    @ResponseBody
    public String deleteLog(String[] logId) {

        for (int i = 0; i < logId.length; i++) {
           //删除日志
            userLogService.deleteByPrimaryKey(Integer.parseInt(logId[i]));
        }
        return "OK";
    }


    //查询日志记录
    @RequestMapping("/searchLog")
    @ResponseBody
    public String searchLog(HttpSession session,Map<String,Object> map ) {

        //根据查询日志

        return "OK";
    }
}
