package com.example.demo.service.impl;

import com.example.demo.entity.Shareing;
import com.example.demo.service.ShareDetailService;
import com.example.demo.service.ShareingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ShareingServiceImpl extends ShareingService {
    @Autowired
    ShareingService shareingService;


}
