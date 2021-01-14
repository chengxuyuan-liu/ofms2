package com.example.demo.service;

import com.example.demo.entity.ComboMeal;
import com.example.demo.entity.UserInf;

import java.util.List;

public interface ComboMealService {
    /*
     * 查询所有套餐
     * */
    List<ComboMeal> backComboMealList();

    List<ComboMeal> foreComboMealList(UserInf userInf);

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
