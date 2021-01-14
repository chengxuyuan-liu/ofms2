package com.example.demo.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.dao.*;
import com.example.demo.entity.*;
import com.example.demo.service.OrdersService;
import com.example.demo.util.PageUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class OrdersServiceImpl implements OrdersService {
    @Autowired
    OrdersDao ordersDao;
    @Autowired
    ComboMealDao comboMealDao;
    @Autowired
    FileCabinetDao fileCabinetDao;
    @Autowired
    DirInfDao dirInfDao;
    @Autowired
    UserInfDao userInfDao;

    /*
     * 购买成功后添加订单
     * */
    @Override
    public String add(Orders orders,UserInf userInf) {

        //开始和结束时间，由套餐来定
        ComboMeal comboMeal = comboMealDao.selectByPrimaryKey(orders.getMealId()); //获得套餐详细信息
        Integer deadline = comboMeal.getDeadline();  //获得套餐期限
        Date now = new Date();  //套餐开始时间
        //计算套餐结束时间
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR,deadline);
        //封装
        orders.setStartTime(now);
        orders.setEndTime(calendar.getTime());
        orders.setUserId(userInf.getUserId());

        //数据持久化
        int result = ordersDao.insertSelective(orders);
        if(result == 0) return "添加失败";

        //根据套餐更新用户空间和个人空间
        //获取当前用户的个人文件柜
        DirInf dirInf = dirInfDao.selectRootDirByUserId(userInf.getUserId());// 当前用户的根目录
        DirInf perDirInf = dirInfDao.selectDirByDirName("我的文件",dirInf.getDirId());//个人文件柜的文件夹
        FileCabinet fileCabinet = fileCabinetDao.selectByDirId(perDirInf.getDirId());//个人文件柜
        BigInteger perSpace = comboMeal.getPerSpace();   //个人空间套餐增加量
        BigInteger teamSpace = comboMeal.getTeamSpace(); //团队空间套餐增加量
        //更新
        fileCabinet.setMaxSpace(fileCabinet.getMaxSpace().add(perSpace));   //增加个人空间
        if(fileCabinetDao.updateByPrimaryKeySelective(fileCabinet) == 0) return "添加失败"; //更新个人空间
        if(userInf.getUserType() == 2){
            userInf.setMaxSpace(userInf.getMaxSpace().add(teamSpace)); //增加团队空间
            if(userInfDao.updateByPrimaryKeySelective(userInf) == 0) return "添加失败";   //更新团队空间
        }
        return "添加成功";
    }

    /*
     *后台查询订单，分页
     * */
    public PageResult findPage(PageRequest pageRequest){
        return PageUtils.getPageResult(pageRequest,getPageInfo(pageRequest));
    }

    @Override
    public String delete(String ordersId) {
        JSONObject object = JSONObject.parseObject(ordersId);
        JSONArray array = object.getJSONArray("ordersId");
        List<Integer> id = array.toJavaList(Integer.class);
        int result = ordersDao.deleteByIdList(id);
        if(result == 0) return "删除失败";
        return "删除成功";
    }

    private PageInfo getPageInfo(PageRequest pageRequest){
        int pageNum = pageRequest.getPageNum();
        int pageSize = pageRequest.getPageSize();
        String oderBy = "start_time"+" desc";
        PageHelper.startPage(pageNum, pageSize,oderBy);
        List<Orders> ordersList = ordersDao.seletePage();
        return new PageInfo<Orders>(ordersList);
    }
    /*
     * 检查到期订单，更新空间
     * */
   // @Scheduled(fixedDelay = 1000000000)
    public void checkTime(){

        //获得到期的订单
        List<Orders> ordersList = ordersDao.selectOutDateOrders();
        for (Orders orders : ordersList) {
            System.out.println(orders.getEndTime());
        }
        //根据到期的订单更新空间
        //循环到期订单：根据套餐id获取套餐信息，根据用户id获取用户信息，根据用户信息获取个人文件柜
        for (Orders orders : ordersList) {
            ComboMeal comboMeal = comboMealDao.selectByPrimaryKey(orders.getMealId());
            UserInf userInf = userInfDao.selectByPrimaryKey(orders.getUserId());
            DirInf rootDir = dirInfDao.selectRootDirByUserId(userInf.getUserId()); //根文件夹
            DirInf dirInf = dirInfDao.selectDirByDirName("我的文件",rootDir.getDirId()); //文件柜文件夹
            FileCabinet fileCabinet = fileCabinetDao.selectByDirId(dirInf.getDirId()); //个人文件柜

            BigInteger perSpace = comboMeal.getPerSpace();   //个人空间套餐增加量
            BigInteger teamSpace = comboMeal.getTeamSpace(); //团队空间套餐增加量

            //更新个人文件柜，如果是团队账号，更新用户空间
            fileCabinet.setMaxSpace(fileCabinet.getMaxSpace().subtract(perSpace));
            if(fileCabinetDao.updateByPrimaryKeySelective(fileCabinet)==0) System.out.println("更新文件柜失败");

            //更新团队空间
            if(comboMeal.getmType() == 2){
                userInf.setMaxSpace(userInf.getMaxSpace().subtract(teamSpace)); //增加团队空间
                if(userInfDao.updateByPrimaryKeySelective(userInf) == 0) System.out.println("更新团队失败");   //更新团队空间
            }


            //删除订单
            if(ordersDao.deleteByPrimaryKey(orders.getOrderId())==0) System.out.println("更新订单失败");

        }
        System.out.println("更新订单、空间成功");

    }

}
