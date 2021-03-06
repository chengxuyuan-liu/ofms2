package com.example.demo.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.dao.BuyRecordDao;
import com.example.demo.dao.ComboMealDao;
import com.example.demo.entity.*;
import com.example.demo.service.ComboMealService;
import com.example.demo.util.PageUtils;
import com.example.demo.util.UnitChange;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.transform.Result;
import java.util.Iterator;
import java.util.List;

@Service
public class ComboMealServiceImpl implements ComboMealService {

    @Autowired
    ComboMealDao comboMealDao;
    @Autowired
    BuyRecordDao buyRecordDao;
    /*
     * 添加套餐
     * */
    @Override
    public String add(ComboMeal comboMeal) {
        //空间计算，把用户输入的数转换成byte
        comboMeal.setPerSpace(UnitChange.TranslateGBtoByte(comboMeal.getPerSpace().intValue()));
        if(comboMeal.getTeamSpace() == null){
            comboMeal.setTeamSpace(UnitChange.TranslateGBtoByte(0));
        }else{
            comboMeal.setTeamSpace(UnitChange.TranslateGBtoByte(comboMeal.getTeamSpace().intValue()));
        }


        int result = comboMealDao.insertSelective(comboMeal);
        if(result == 0){
            return "添加失败！";
        }
        return "添加成功！";
    }

    /*
     * 删除套餐
     * */
    @Override
    public String delete(String comboMealId) {
        //JSON转List
        System.out.println(comboMealId);
        JSONObject jsonObject = JSONObject.parseObject(comboMealId);
        JSONArray jsonArray = jsonObject.getJSONArray("comboMealId");
        List<Integer> idList = jsonArray.toJavaList(Integer.class);

        for (Integer integer : idList) {
            List<BuyRecord> buyRecords = buyRecordDao.selectByMealId(integer);
            if(buyRecords.size() != 0){
                ComboMeal comboMeal = comboMealDao.selectByPrimaryKey(integer);
                return "套餐《"+comboMeal.getMealName()+"》删除失败！有"+buyRecords.size()+"条记录关联此套餐！";
            }
        }
        //调用持久层删除
        try {

            int result = comboMealDao.deleteByIdArray(idList);
            if(result == 0){
                return "删除失败";
            }
            return "删除成功";
        }catch (Exception e){
            return "删除失败";
        }

    }


    /*
     * 修改套餐
     * */
    @Override
    public String update(ComboMeal comboMeal) {
        //空间计算，把用户输入的数转换成byte
        comboMeal.setPerSpace(UnitChange.TranslateGBtoByte(comboMeal.getPerSpace().intValue()));
        comboMeal.setTeamSpace(UnitChange.TranslateGBtoByte(comboMeal.getTeamSpace().intValue()));

        List<BuyRecord> buyRecords = buyRecordDao.selectByMealId(comboMeal.getMealId());
        if(buyRecords.size() != 0){
            return "修改失败！有"+buyRecords.size()+"条记录关联此套餐！";
        }

        int result = comboMealDao.updateByPrimaryKeySelective(comboMeal);
        if(result == 0 ){
            return "更新失败";
        }
        return "更新成功";
    }

    @Override
    public String loadingAndunloading(ComboMeal comboMeal) {
        //如果是上架状态则变成下架状态，反之则变成上架状态
        if(comboMeal.getmStatus() == 1){
            comboMeal.setmStatus(0);
        }else{
            comboMeal.setmStatus(1);
        }
        if(comboMealDao.updateByPrimaryKeySelective(comboMeal)==0) return "修改失败！"; //更新
        return "修改成功！";
    }


    /*
    * 上下架，更改状态
    * */



    /*
    *前台页面数据，已上架的套餐
    * */
    @Override
    public List<ComboMeal> foreComboMealList(UserInf userInf) {
        List<ComboMeal> comboMeals = comboMealDao.comboMealList(1);
        Iterator iterator = comboMeals.iterator();
        if(userInf.getUserType() == 1){
            while (iterator.hasNext()){
                ComboMeal comboMeal1 = (ComboMeal) iterator.next();
                if(comboMeal1.getmType() == 2){
                    iterator.remove();
                }
            }
        }else{
            while (iterator.hasNext()){
                ComboMeal comboMeal1 = (ComboMeal) iterator.next();
                if(comboMeal1.getmType() == 1){
                    iterator.remove();
                }
            }
        }
        return comboMeals;
    }

    /*
     *后台查询订单，分页
     * */
    public PageResult findPage(PageRequest pageRequest,String mealName){
        if(mealName != null){
            return PageUtils.getPageResult(pageRequest,getPageInfo(pageRequest,mealName));
        }
        return PageUtils.getPageResult(pageRequest,getPageInfo(pageRequest,null));
    }
    private PageInfo getPageInfo(PageRequest pageRequest,String mealName){
        int pageNum = pageRequest.getPageNum();
        int pageSize = pageRequest.getPageSize();
        //String oderBy = "start_time"+" desc";
        PageHelper.startPage(pageNum, pageSize);
        List<ComboMeal> ordersList;
        if(mealName != null){
            ordersList = comboMealDao.seletePageByName(mealName);   //模糊搜素
        }else{
            ordersList = comboMealDao.seletePage();
        }

        return new PageInfo<ComboMeal>(ordersList);
    }




    /*
    * 后台页面数据，所有套餐
    * */
    @Override
    public List<ComboMeal> backComboMealList() {
        List<ComboMeal> comboMeals = comboMealDao.comboMealList(null);
        return comboMeals;
    }
}
