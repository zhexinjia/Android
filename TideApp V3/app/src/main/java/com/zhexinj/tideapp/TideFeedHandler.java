package com.zhexinj.tideapp;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Created by zhexinjia on 7/8/16.
 */
public class TideFeedHandler extends DefaultHandler {
    private TideArray tideArray;
    private TideItem item;

    private boolean isDate = false;
    private boolean isDay = false;
    private boolean isTime = false;
    private boolean isPredictionsInFt = false;
    private boolean isPredictionsInCm = false;
    private boolean isHighLow = false;

    public TideArray getFeed(){
        return tideArray;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equals("item")){
            item = new TideItem();
            return;
        }else if(qName.equals("date")){
            isDate = true;
            return;
        }else if(qName.equals("day")){
            isDay = true;
            return;
        }else if(qName.equals("time")){
            isTime = true;
            return;
        }else if(qName.equals("predictions_in_ft")){
            isPredictionsInFt = true;
            return;
        }else if(qName.equals("predictions_in_cm")){
            isPredictionsInCm = true;
            return;
        }else if(qName.equals("highlow")){
            isHighLow = true;
            return;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals("item")){
            tideArray.add(item);
        }
        return;
    }

    @Override
    public void startDocument() throws SAXException {
        tideArray = new TideArray();
        item = new TideItem();
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String s = new String(ch, start, length);
        if (isDate){
            item.setDate(s);
            isDate = false;
        }else if(isDay){
            item.setDay(s);
            isDay = false;
        }else if(isTime){
            item.setTime(s);
            isTime = false;
        }else if(isPredictionsInFt){
            item.setPredictionInFt(s);
            isPredictionsInFt = false;
        }else if(isPredictionsInCm){
            item.setPredictionsInCm(s);
            isPredictionsInCm = false;
        }else if(isHighLow){
            item.setHighLow(s);
            isHighLow = false;
        }

    }



}
