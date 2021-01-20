package com.example.demo.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.dao.*;
import com.example.demo.entity.*;
import com.example.demo.service.BuyRecordService;
import com.example.demo.util.PageUtils;
import com.example.demo.vo.BuyRecordVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class BuyRecordServiceImpl implements BuyRecordService {
    @Autowired
    BuyRecordDao buyRecordDao;
    @Autowired
    ComboMealDao comboMealDao;
    @Autowired
    FileCabinetDao fileCabinetDao;
    @Autowired
    DirInfDao dirInfDao;
    @Autowired
    UserInfDao userInfDao;

    /*
     * 购买成功后添加购买记录
     * */
    @Override
    public String add(BuyRecord buyRecord, UserInf userInf) {

        //开始和结束时间，由套餐来定
        ComboMeal comboMeal = comboMealDao.selectByPrimaryKey(buyRecord.getMealId()); //获得套餐详细信息
        Integer deadline = comboMeal.getDeadline();  //获得套餐期限
        Date now = new Date();  //套餐开始时间
        //计算套餐结束时间
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, deadline);
        //封装
        buyRecord.setStartTime(now);
        buyRecord.setEndTime(calendar.getTime());
        buyRecord.setUserId(userInf.getUserId());

        //数据持久化
        int result = buyRecordDao.insertSelective(buyRecord);
        if (result == 0) return "支付失败！";

        //根据套餐更新用户空间和个人空间
        //获取当前用户的个人文件柜
        DirInf dirInf = dirInfDao.selectRootDirByUserId(userInf.getUserId());// 当前用户的根目录
        DirInf perDirInf = dirInfDao.selectDirByDirName("我的文件", dirInf.getDirId());//个人文件柜的文件夹
        FileCabinet fileCabinet = fileCabinetDao.selectByDirId(perDirInf.getDirId());//个人文件柜
        BigInteger perSpace = comboMeal.getPerSpace();   //个人空间套餐增加量
        BigInteger teamSpace = comboMeal.getTeamSpace(); //团队空间套餐增加量
        //更新
        fileCabinet.setMaxSpace(fileCabinet.getMaxSpace().add(perSpace));   //增加个人空间
        if (fileCabinetDao.updateByPrimaryKeySelective(fileCabinet) == 0) return "支付失败！"; //更新个人空间
        if (userInf.getUserType() == 2) {
            userInf.setMaxSpace(userInf.getMaxSpace().add(teamSpace)); //增加团队空间
            if (userInfDao.updateByPrimaryKeySelective(userInf) == 0) return "支付失败！";   //更新团队空间
        }
        return "支付成功！";
    }

    /*
     *后台查询购买记录，分页
     * */
    public PageResult findPage(PageRequest pageRequest) {
        return PageUtils.getPageResult(pageRequest, getPageInfo(pageRequest));
    }


    /**/
    @Override
    public String delete(String recordId) {
        System.out.println(recordId);
        JSONObject object = JSONObject.parseObject(recordId);
        JSONArray array = object.getJSONArray("recordId");
        List<Integer> id = array.toJavaList(Integer.class);

        //根据id获得的购买记录
        List<BuyRecord> buyRecordList = new ArrayList<>();
        for (Integer integer : id) {
            buyRecordList.add(buyRecordDao.selectByPrimaryKey(integer));
        }

        //更新用户空间
        for (BuyRecord buyRecord : buyRecordList) {
            ComboMeal comboMeal = comboMealDao.selectByPrimaryKey(buyRecord.getMealId());
            UserInf userInf = userInfDao.selectByPrimaryKey(buyRecord.getUserId());
            DirInf rootDir = dirInfDao.selectRootDirByUserId(userInf.getUserId()); //根文件夹
            DirInf dirInf = dirInfDao.selectDirByDirName("我的文件", rootDir.getDirId()); //文件柜文件夹
            FileCabinet fileCabinet = fileCabinetDao.selectByDirId(dirInf.getDirId()); //个人文件柜
            BigInteger perSpace = comboMeal.getPerSpace();   //个人空间套餐增加量
            BigInteger teamSpace = comboMeal.getTeamSpace(); //团队空间套餐增加量
            //更新个人文件柜，如果是团队账号，更新用户空间
            fileCabinet.setMaxSpace(fileCabinet.getMaxSpace().subtract(perSpace));
            if (fileCabinetDao.updateByPrimaryKeySelective(fileCabinet) == 0) System.out.println("更新文件柜失败");
            //更新团队空间
            if (comboMeal.getmType() == 2) {
                userInf.setMaxSpace(userInf.getMaxSpace().subtract(teamSpace)); //增加团队空间
                if (userInfDao.updateByPrimaryKeySelective(userInf) == 0) System.out.println("更新团队失败");   //更新团队空间
            }
        }

            //删除购买记录
            int result = buyRecordDao.deleteByIdList(id);
            if (result == 0) return "删除失败";
            return "删除成功";
        }

        private PageInfo getPageInfo (PageRequest pageRequest){
            int pageNum = pageRequest.getPageNum();
            int pageSize = pageRequest.getPageSize();
            String oderBy = "start_time" + " desc";
            PageHelper.startPage(pageNum, pageSize, oderBy);
            List<BuyRecordVO> buyRecordList = buyRecordDao.seletePage();
            return new PageInfo<BuyRecordVO>(buyRecordList);
        }
        /*
         * 检查到期购买记录，更新空间
         * */
        @Scheduled(cron = "0 0 0 */1 * ?")
        public void checkTime () {

            System.out.println("检查记录期限，更新空间。");
            //获得到期的购买记录
            List<BuyRecord> buyRecordList = buyRecordDao.selectOutDateOrders();
            for (BuyRecord buyRecord : buyRecordList) {
                System.out.println(buyRecord.getEndTime());
            }
            //根据到期的购买记录更新空间
            //循环到期购买记录：根据套餐id获取套餐信息，根据用户id获取用户信息，根据用户信息获取个人文件柜
            for (BuyRecord buyRecord : buyRecordList) {
                ComboMeal comboMeal = comboMealDao.selectByPrimaryKey(buyRecord.getMealId());
                UserInf userInf = userInfDao.selectByPrimaryKey(buyRecord.getUserId());
                DirInf rootDir = dirInfDao.selectRootDirByUserId(userInf.getUserId()); //根文件夹
                DirInf dirInf = dirInfDao.selectDirByDirName("我的文件", rootDir.getDirId()); //文件柜文件夹
                FileCabinet fileCabinet = fileCabinetDao.selectByDirId(dirInf.getDirId()); //个人文件柜

                BigInteger perSpace = comboMeal.getPerSpace();   //个人空间套餐增加量
                BigInteger teamSpace = comboMeal.getTeamSpace(); //团队空间套餐增加量

                //更新个人文件柜，如果是团队账号，更新用户空间
                fileCabinet.setMaxSpace(fileCabinet.getMaxSpace().subtract(perSpace));
                if (fileCabinetDao.updateByPrimaryKeySelective(fileCabinet) == 0) System.out.println("更新文件柜失败");

                //更新团队空间
                if (comboMeal.getmType() == 2) {
                    userInf.setMaxSpace(userInf.getMaxSpace().subtract(teamSpace)); //增加团队空间
                    if (userInfDao.updateByPrimaryKeySelective(userInf) == 0) System.out.println("更新团队失败");   //更新团队空间
                }


                //删除购买记录
                if (buyRecordDao.deleteByPrimaryKey(buyRecord.getRecordId()) == 0) System.out.println("更新购买记录失败");
            }
            System.out.println("更新购买记录、空间成功");
        }
    }

