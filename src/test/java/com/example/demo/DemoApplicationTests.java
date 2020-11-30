package com.example.demo;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.MessagingException;


@SpringBootTest
class DemoApplicationTests {
    @Autowired
    private JavaMailSender javaMailSender;

    @Test
    public void testSend() throws MessagingException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("2919586256@qq.com");
        message.setTo("18877165120@163.com");
        message.setSubject("这是标题");
        message.setText("这是内容");
        javaMailSender.send(message);
    }
}
