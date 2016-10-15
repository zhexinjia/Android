package com.zhexinj.tideapp;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by zhexinjia on 7/8/16.
 */
public class TideArray extends ArrayList<TideItem> {

    private String city = "";

    public void setCity(String city){
        this.city = city;
    }
    public String getCity(){
        return city;
    }
}
