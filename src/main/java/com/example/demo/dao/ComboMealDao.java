package com.example.demo.dao;

import com.example.demo.entity.ComboMeal;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ComboMealDao {
    int deleteByPrimaryKey(Integer mealId);
    int deleteByIdArray(List comboMealId);

    int insert(ComboMeal record);

    int insertSelective(ComboMeal record);

    ComboMeal selectByPrimaryKey(Integer mealId);
    List<ComboMeal> comboMealList(Integer mStatus);        //查询所有套餐
    /*
     * 查找分页
     * */
    List<ComboMeal> seletePage();
    List<ComboMeal> seletePageByName(String mealName);

    int updateByPrimaryKeySelective(ComboMeal record);

    int updateByPrimaryKey(ComboMeal record);
}