package com.zhexinj.tideapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by zhexinjia on 7/8/16.
 */
public class Dal {
    private Context context =null;
    private String urlString = "http://tidesandcurrents.noaa.gov/noaatidepredictions/NOAATidesFacade.jsp?datatype=Annual+XML&Stationid=";
    private final String FILENAME = "tide.xml";

    public Dal(Context context){
        this.context = context;
    }


    //Load data if no given city found, not going to use this method in Tide Version 3
    public void loadTestData(String cityName){
        DBHelper helper = new DBHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        if(db.rawQuery("SELECT * FROM TideInfo", null).getCount() == 0)
        {
            db = helper.getWritableDatabase();
            loadDBFromXML("Empire");
            loadDBFromXML("Florence");
            loadDBFromXML("PortOrford");
        }
        db.close();
    }

    public void loadDBFromXML(String cityName){
        //String fileName = cityName + ".xml";
        TideArray tideArray = parseXmlFile(FILENAME);

        tideArray.setCity(cityName); //set CityName

        //initialize db
        DBHelper help = new DBHelper(context);
        SQLiteDatabase db = help.getWritableDatabase();

        //put TideInfo in database
        ContentValues cv = new ContentValues();
        for (TideItem item : tideArray){
            cv.put("City", tideArray.getCity());
            cv.put("Date", item.getDate());
            cv.put("Day", item.getDay());
            cv.put("Time", item.getTime());
            cv.put("HighLow", item.getHighLow());
            cv.put("Feet", item.getPredictionsInFt());
            cv.put("CM", item.getPredictionsInCm());
            db.insert("TideInfo", null, cv);
        }
        db.close();
    }

    public Cursor getTideInfoByCityAndDate(String location, String Date){
        //loadTestData(location);

        //get database
        DBHelper help = new DBHelper(context);
        SQLiteDatabase db = help.getReadableDatabase();

        String query = "SELECT * FROM TideInfo WHERE City = ? AND Date = ?";
        String[] variables = new String[]{location, Date};

        return  db.rawQuery(query, variables);
    }




    public TideArray parseXmlFile(String fileName){
        try{
            //get the XML reader
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            XMLReader xmlReader = parser.getXMLReader();

            //set content handler
            TideFeedHandler handler = new TideFeedHandler();
            xmlReader.setContentHandler(handler);

            //read file from xml
            InputStream in = context.openFileInput(fileName);

            //parse the data
            InputSource is = new InputSource(in);
            xmlReader.parse(is);

            TideArray items = handler.getFeed();
            return items;
        }catch (Exception e){
            System.out.println("parseXML error");
            return null;
        }
    }

    public void downloadFile(String city){
        String cityID;
        switch (city){
            case "Half Moon Bay":
                cityID = "9433445";
                break;
            case "Florence USCG Pier":
                cityID = "9434098";
                break;
            case "Charleston":
                cityID = "9432780";
                break;
            case "Southbeach":
                cityID = "9435380";
                break;
            case "Yaquina River":
                cityID = "9435308";
                break;
            case "Garibaldi":
                cityID = "9437540";
                break;
            case "Dick Point":
                cityID = "9437381";
                break;
            case "Rogue River":
                cityID = "9431011";
                break;
            case "Port Orford":
                cityID = "9431647";
                break;
            case "Alsea Bay":
                cityID = "9434939";
                break;
            case "Depoe Bay":
                cityID = "9435827";
                break;
            case "Salmon River":
                cityID = "9436381";
                break;
            default:
                cityID = "0";
                break;
        }
        String finalUrl = urlString + cityID;
        try{
            // get final URL address
            URL url = new URL(finalUrl);

            //get input stream
            InputStream in = url.openStream();

            //get output stream
            FileOutputStream out = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);

            //read input and write output
            byte[] buffer = new byte[1024];
            int bytesRead = in.read(buffer);
            while(bytesRead != -1){
                out.write(buffer, 0, bytesRead);
                bytesRead = in.read(buffer);
            }
            out.close();
            in.close();
        }catch (IOException e){
            Log.e("News reader", e.toString());
        }
    }
}
