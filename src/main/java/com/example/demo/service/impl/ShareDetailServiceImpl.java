package com.example.demo.service.impl;

import com.example.demo.service.ShareDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShareDetailServiceImpl extends ShareDetailService {
    @Autowired
    ShareDetailService shareDetailService;


}
