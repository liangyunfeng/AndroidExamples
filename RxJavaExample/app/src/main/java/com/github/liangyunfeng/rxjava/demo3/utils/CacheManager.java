package com.github.liangyunfeng.rxjava.demo3.utils;

import com.github.liangyunfeng.rxjava.demo3.model.FoodList;

/**
 * Created by lyf on 2018/6/10.
 */

public class CacheManager {
    private static CacheManager instance;
    private FoodList footListJson;

    private CacheManager(){}

    public static CacheManager getInstance(){
        if (instance == null){
            instance = new CacheManager();
        }
        return instance;
    }

    public FoodList getFoodListData(){
        return this.footListJson;
    }

    public void setFoodListData(FoodList data){
        this.footListJson = data;
    }
}
