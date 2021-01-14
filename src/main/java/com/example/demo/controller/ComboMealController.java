package com.example.demo.controller;

import com.example.demo.entity.ComboMeal;
import com.example.demo.entity.UserInf;
import com.example.demo.service.ComboMealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@RequestMapping("comboMeal")
@Controller
public class ComboMealController {

    @Autowired
    ComboMealService comboMealService;

    /*
     * 后台页面数据，所有套餐
     * */
    @RequestMapping("/backgroundList")
    public String backgroundList(Map<String,Object> map){
        List<ComboMeal> comboMeals =  comboMealService.backComboMealList();
        map.put("comboMeals",comboMeals);
        return "adminstration_tariff";
    }

    /*
     *前台页面数据，已上架的套餐
     * */
    @RequestMapping("/foregroundList")
    public String foregroundList(Map<String,Object> map,HttpSession session){
        UserInf userInf = (UserInf) session.getAttribute("USER_SESSION");
        List<ComboMeal> comboMeals =  comboMealService.foreComboMealList(userInf);
        map.put("comboMeals",comboMeals);
        if(userInf.getUserType() == 2)  return "tariff";
        return "tariff2";
    }




    /*
     * 添加套餐
     * */
    @RequestMapping("/add")
    @ResponseBody
    public String add(ComboMeal comboMeal){
        String result = comboMealService.add(comboMeal);
        return result;
    }

    /*
     * 删除套餐(批量删除)
     * */
    @RequestMapping("/delete")
    @ResponseBody
    public String delete(@RequestBody String comboMealId){
        String result = comboMealService.delete(comboMealId);
        return result;
    }

    /*
     * 修改套餐
     * */
    @RequestMapping("/edit")
    @ResponseBody
    public String edit(ComboMeal comboMeal){
        String result = comboMealService.update(comboMeal);
        return result;
    }

    /*
    * 上下架
    * */
    @RequestMapping("/loadingAndunloading")
    @ResponseBody
    public String loadingAndunloading(ComboMeal comboMeal){
        String result = comboMealService.loadingAndunloading(comboMeal);
        return result;
    }
}
