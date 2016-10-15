package com.zhexinj.tideapp;

import java.text.SimpleDateFormat;

/**
 * Created by zhexinjia on 7/8/16.
 */
public class TideItem {
    private String date;
    private String day;
    private String time;
    private String predictionsInFt;
    private String predictionsInCm;
    private String highLow;

    public void setDate(String date){
        this.date = date;
    }
    public String getDate(){
        return date;
    }

    public void setDay(String day){
        this.day = day;
    }
    public String getDay(){
        return day;
    }

    public void setTime(String time){
        this.time = time;
    }
    public String getTime(){
        return time;
    }

    public void setPredictionInFt(String predictionsInFt){
        this.predictionsInFt = predictionsInFt;
    }
    public String getPredictionsInFt(){
        return predictionsInFt;
    }

    public void setPredictionsInCm(String predictionsInCm){
        this.predictionsInCm = predictionsInCm;
    }
    public String getPredictionsInCm(){
        return predictionsInCm;
    }

    public void setHighLow(String highLow){
        this.highLow = highLow;
    }
    public String getHighLow(){
        return highLow;
    }
}
