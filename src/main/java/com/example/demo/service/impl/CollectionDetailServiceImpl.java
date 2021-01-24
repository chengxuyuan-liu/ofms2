package com.example.demo.service.impl;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.dao.CollectDao;
import com.example.demo.dao.CollectionDetailDao;
import com.example.demo.dao.DirInfDao;
import com.example.demo.entity.*;
import com.example.demo.service.CollectionDetailService;
import com.google.gson.internal.$Gson$Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CollectionDetailServiceImpl implements CollectionDetailService {

    @Autowired
    CollectionDetailDao collectionDetailDao;
    @Autowired
    CollectDao collectDao;
    @Autowired
    DirInfDao dirInfDao;
    /*
     * 添加收集记录
     * */
    @Override
    public String add(Integer dirId, String jsonMemberId, Integer memberNum, String describe, String datetime,Integer userId) {
        //获得存储路径
        String savePath = "";
        DirInf dirInf = dirInfDao.selectByPrimaryKey(dirId);
        List<DirInf> originAccessPath = null;     //数据库原始访问路径
        List<DirInf> accessPath = new ArrayList<>();     //访问路径
        //没排序的导航路径
        originAccessPath = dirInfDao.selectParentDirByDirId(dirId);
        //导航路径排序
        Integer item = dirId;
        for (int i = 0; i < originAccessPath.size(); i++){
            for (DirInf inf : originAccessPath) {
                if(inf.getDirId().equals(item)){
                    accessPath.add(0,inf);
                    item = inf.getParentDir();
                }
            }
        }

        for (DirInf inf : accessPath) {
            savePath += inf.getDirName()+"\\";
        }

        //处理Id
        JSONObject jsonObject = JSONObject.parseObject(jsonMemberId);
        JSONArray jsonElements = jsonObject.getJSONArray("memberId");
        List<Integer> memberIdList = jsonElements.toJavaList(Integer.class);
        //处理时间
        try {
            SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd hh:mm");
            Date date = ft.parse(datetime);

            //插入收集详情记录（描述、期限、总人数、上传人数、详情状态、用户id、文件夹id）
            CollectionDetail collectionDetail = new CollectionDetail();
            collectionDetail.setCdDescribe(describe);
            collectionDetail.setDeadline(date);
            collectionDetail.setAllNum(memberNum);
            collectionDetail.setUserId(userId);
            collectionDetail.setDirId(dirId);
            collectionDetail.setSavePath(savePath);
            if(collectionDetailDao.insertSelective(collectionDetail)==0) return "发布失败！";
            //插入收集记录（详情id、成员id）
            for (Integer integer : memberIdList) {
                Collect collect = new Collect();
                collect.setCdId(collectionDetail.getCdId());
                collect.setMemberId(integer);
                if(collectDao.insertSelective(collect)==0) return "发布失败！";
            }
            return "发布成功！";
        } catch (ParseException e) {
            System.out.println("解析字符串成时间错误！");
            e.printStackTrace();
        }
        return "发布失败！";
    }

    @Override
    public List<CollectionDetail> all(Integer userId) {
        //查询
        List<CollectionDetail> collectionDetails = collectionDetailDao.selectAll(userId);
        Date date = new Date();
        for (CollectionDetail collectionDetail : collectionDetails) {
            System.out.println(collectionDetail.getDeadline().after(date));
            if(collectionDetail.getDeadline().before(date)){
                CollectionDetail collectionDetail1 = new CollectionDetail();
                collectionDetail1.setCdStatus(0);
                collectionDetail1.setCdId(collectionDetail.getCdId());
                collectionDetailDao.updateByPrimaryKeySelective(collectionDetail1);
            }
        }
        collectionDetails = collectionDetailDao.selectAll(userId);
        return collectionDetails;
    }

    /*
    * 删除
    * */
    @Override
    public String delete(String[] cdId) {
        //查询
        if (cdId != null) {
            for (int i = 0; i < cdId.length; i++) {
                collectionDetailDao.deleteByPrimaryKey(Integer.parseInt(cdId[i]));
            }
        }

        return "删除成功!";
    }

    /*
     * 检查截止时间
     * */
    @Override
    public Boolean checkDeadline(Integer cdId) {
        CollectionDetail collectionDetail = collectionDetailDao.selectByPrimaryKey(cdId);
        Date date = new Date();
        return collectionDetail.getDeadline().before(date);
    }

    /*
    * 编辑时间
    * */
    /*
     * 检查截止时间
     * */
    @Override
    public String eiditDeadline(String deadline, Integer cdId) {
        try {
            Date now = new Date();
            //处理时间
            SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd hh:mm");
            Date date = ft.parse(deadline);
            System.out.println(date);
            //封装
            CollectionDetail collectionDetail = new CollectionDetail();
            collectionDetail.setDeadline(date);
            collectionDetail.setCdId(cdId);
            if(date.after(now)) {
                collectionDetail.setCdStatus(1);
            }else{
                collectionDetail.setCdStatus(0);
            }
            collectionDetailDao.updateByPrimaryKeySelective(collectionDetail);  //修改截止时间
            return "修改成功!";
        } catch (ParseException e) {
            e.printStackTrace();
        }

       return "修改失败！";
    }


}
