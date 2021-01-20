package com.example.demo.aop;

import com.example.demo.entity.UserInf;
import com.example.demo.entity.UserLog;
import com.example.demo.service.UserLogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;


@Aspect
@Component
public class ArchivesLogAspect {

    @Autowired
    UserLogService userLogService;


    @Pointcut("@annotation(ArchivesLog)")
    public void controllerAspect() {
        //System.out.println("切入点...");
    }


    /**
     * 方法调用后触发 , 记录正常操作
     *
     * @param joinPoint
     * @throws ClassNotFoundException
     */
    @AfterReturning("controllerAspect()")
    public void after(JoinPoint joinPoint) throws ClassNotFoundException {
        System.out.println("aop日志测试！！！！！！！！！！！！！！！！！");
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = req.getSession();
        String loginStatus = (String) session.getAttribute("LOGIN_STATUS");
        UserInf userInf = getUSerMsg(req);

        //用户日志
        UserLog userLog = new UserLog();
        if(userInf != null) {
            userLog.setUserId(userInf.getEmail());  //邮箱账号
            userLog.setUsername(userInf.getUsername()); //用户名
            userLog.setUserIp(getIpAddr(req));  //ip地址
            userLog.setOperateContent(loginStatus); //操作内容
            //调用服务
            int result = userLogService.insertSelective(userLog);
            System.out.println("插入日志成功");
        }


}


    /**
     * 得到用户信息
     *
     * @return
     */
    public static UserInf getUSerMsg(HttpServletRequest req) {
        // 获取session
        HttpSession session = req.getSession();
        UserInf user = (UserInf) session.getAttribute("LOGIN_USER");
        return user;
    }

    /**
     * 获取登录用户的IP地址
     *
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if ("0:0:0:0:0:0:0:1".equals(ip)) {
            ip = "127.0.0.1";
        }
        if (ip.split(",").length > 1) {
            ip = ip.split(",")[0];
        }
        return ip;
    }


    /**
     * 获取 注解中对方法的描述
     *
     * @return
     * @throws ClassNotFoundException
     */
    public static String getMethodDesc(JoinPoint joinPoint) throws ClassNotFoundException {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        String operteContent = "";
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == arguments.length) {
                    // 操作说明
                    operteContent = method.getAnnotation(ArchivesLog.class).operteContent();
                    break;
                }
            }
        }
        return operteContent;
    }


}