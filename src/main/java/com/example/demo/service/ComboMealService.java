package com.example.demo.service;

import com.example.demo.entity.ComboMeal;
import com.example.demo.entity.PageRequest;
import com.example.demo.entity.PageResult;
import com.example.demo.entity.UserInf;

import java.util.List;

public interface ComboMealService {
    /*
     * 查询所有套餐
     * */
    List<ComboMeal> backComboMealList();
    /*
    * 前台查询套餐
    * */
    List<ComboMeal> foreComboMealList(UserInf userInf);

    /*
     *后台查询套餐,分页
     * */
    PageResult findPage(PageRequest pageRequest,String mealName);


    /*
     * 添加套餐
     * */
    String add(ComboMeal comboMeal);

    /*
     * 批量删除套餐
     * */
    String delete(String comboMealId);

    /*
     * 修改套餐
     * */
    String update(ComboMeal comboMeal);

    /*
     * 上下架
     * */
    String loadingAndunloading(ComboMeal comboMeal);

}
