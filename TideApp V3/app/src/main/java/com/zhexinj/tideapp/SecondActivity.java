package com.zhexinj.tideapp;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by zhexinjia on 7/9/16.
 */
public class SecondActivity extends Activity {
    private Dal dal = new Dal(this);
    Cursor cursor = null;
    private String date, day, height, time, city;

    TextView warningTextView;
    ProgressBar progressBar;

    TextView dateOneTextView;
    TextView dayOneTextView;
    TextView timeOneTextView;
    TextView heightOneTextView;

    TextView dateTwoTextView;
    TextView dayTwoTextView;
    TextView timeTwoTextView;
    TextView heightTwoTextView;

    TextView dateThreeTextView;
    TextView dayThreeTextView;
    TextView timeThreeTextView;
    TextView heightThreeTextView;

    TextView dateFourTextView;
    TextView dayFourTextView;
    TextView timeFourTextView;
    TextView heightFourTextView;

    TextView dateFiveTextView;
    TextView dayFiveTextView;
    TextView timeFiveTextView;
    TextView heightFiveTextView;

    TextView dateSixTextView;
    TextView daySixTextView;
    TextView timeSixTextView;
    TextView heightSixTextView;

    TextView dateSevenTextView;
    TextView daySevenTextView;
    TextView timeSevenTextView;
    TextView heightSevenTextView;

    ArrayList<TextView> textViewArrayList = new ArrayList<TextView>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_tide);
        dal = new Dal(this);
        cursor = null;
        textViewArrayList = new ArrayList<>();

        savedInstanceState = getIntent().getExtras();
        city = savedInstanceState.getString("city");
        date = savedInstanceState.getString("date");
        warningTextView = (TextView)findViewById(R.id.warningTextView);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        dateOneTextView = (TextView)findViewById(R.id.Date1);
        dayOneTextView = (TextView)findViewById(R.id.Day1);
        timeOneTextView = (TextView)findViewById(R.id.Time1);
        heightOneTextView = (TextView)findViewById(R.id.Height1);

        dateTwoTextView = (TextView)findViewById(R.id.Date2);
        dayTwoTextView = (TextView)findViewById(R.id.Day2);
        timeTwoTextView = (TextView)findViewById(R.id.Time2);
        heightTwoTextView = (TextView)findViewById(R.id.Height2);

        dateThreeTextView = (TextView)findViewById(R.id.Date3);
        dayThreeTextView = (TextView)findViewById(R.id.Day3);
        timeThreeTextView = (TextView)findViewById(R.id.Time3);
        heightThreeTextView = (TextView)findViewById(R.id.Height3);

        dateFourTextView = (TextView)findViewById(R.id.Date4);
        dayFourTextView = (TextView)findViewById(R.id.Day4);
        timeFourTextView = (TextView)findViewById(R.id.Time4);
        heightFourTextView = (TextView)findViewById(R.id.Height4);

        dateFiveTextView = (TextView)findViewById(R.id.Date5);
        dayFiveTextView = (TextView)findViewById(R.id.Day5);
        timeFiveTextView = (TextView)findViewById(R.id.Time5);
        heightFiveTextView = (TextView)findViewById(R.id.Height5);

        dateSixTextView = (TextView)findViewById(R.id.Date6);
        daySixTextView = (TextView)findViewById(R.id.Day6);
        timeSixTextView = (TextView)findViewById(R.id.Time6);
        heightSixTextView = (TextView)findViewById(R.id.Height6);

        dateSevenTextView = (TextView)findViewById(R.id.Date7);
        daySevenTextView = (TextView)findViewById(R.id.Day7);
        timeSevenTextView = (TextView)findViewById(R.id.Time7);
        heightSevenTextView = (TextView)findViewById(R.id.Height7);

        cursor = dal.getTideInfoByCityAndDate(city, date);


        if (cursor.getCount() == 0){
            //if cursor is empty, check internet connection
            if (checkNetWork(getApplicationContext())){
                //download xml and execute it if internet is connected
                new DownloadFeed().execute();
            }else{
                warningTextView.setText("No Internet!\nCheck your Internet Connection!");
            }
        }else{
            //if cursor is not empty, simply update widgets without accessing internet and processing xml file
            updateWidgets();
        }

    }

    class DownloadFeed extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            //download xml file for given city
            dal.downloadFile(city);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.d("News reader", "Feed downloaded");
            new ReadFeed().execute();
        }
    }

    class ReadFeed extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            //load the xml file to database
            dal.loadDBFromXML(city);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.d("News reader", "Feed read");

            // update the display for the activity
            SecondActivity.this.updateWidgets();
        }
    }

    public void updateWidgets(){
        String tomorrow = getTomorrow(date);
        cursor = dal.getTideInfoByCityAndDate(city, date);
        if(cursor.getCount() == 0){
            warningTextView.setText("No information found on Internet");
            progressBar.setVisibility(View.INVISIBLE);
        }else {
            cursor.moveToFirst();

            //using textView arraylist, easier to udpate input
            textViewArrayList.add(dateOneTextView);
            textViewArrayList.add(dateTwoTextView);
            textViewArrayList.add(dateThreeTextView);
            textViewArrayList.add(dateFourTextView);
            textViewArrayList.add(dateFiveTextView);
            textViewArrayList.add(dateSixTextView);
            textViewArrayList.add(dateSevenTextView);

            textViewArrayList.add(dayOneTextView);
            textViewArrayList.add(dayTwoTextView);
            textViewArrayList.add(dayThreeTextView);
            textViewArrayList.add(dayFourTextView);
            textViewArrayList.add(dayFiveTextView);
            textViewArrayList.add(daySixTextView);
            textViewArrayList.add(daySevenTextView);

            textViewArrayList.add(timeOneTextView);
            textViewArrayList.add(timeTwoTextView);
            textViewArrayList.add(timeThreeTextView);
            textViewArrayList.add(timeFourTextView);
            textViewArrayList.add(timeFiveTextView);
            textViewArrayList.add(timeSixTextView);
            textViewArrayList.add(timeSevenTextView);

            textViewArrayList.add(heightOneTextView);
            textViewArrayList.add(heightTwoTextView);
            textViewArrayList.add(heightThreeTextView);
            textViewArrayList.add(heightFourTextView);
            textViewArrayList.add(heightFiveTextView);
            textViewArrayList.add(heightSixTextView);
            textViewArrayList.add(heightSevenTextView);

            //update widgets
            int counter = 0;
            while (!cursor.isAfterLast() && counter < 4) {
                date = cursor.getString(cursor.getColumnIndex("Date"));
                textViewArrayList.get(counter).setText(date);

                day = cursor.getString(cursor.getColumnIndex("Day"));
                textViewArrayList.get(counter + 7).setText(day);

                time = cursor.getString(cursor.getColumnIndex("Time"));
                textViewArrayList.get(counter + 14).setText(time);

                String highLow = cursor.getString(cursor.getColumnIndex("HighLow"));
                height = cursor.getString(cursor.getColumnIndex("Feet"));
                height += "ft " + highLow;
                textViewArrayList.get(counter + 21).setText(height);

                counter++;
                cursor.move(1);
            }

            Cursor cursorTomorrow = dal.getTideInfoByCityAndDate(city, tomorrow);
            cursorTomorrow.moveToFirst();
            while (!cursorTomorrow.isAfterLast() && counter < 7) {
                date = cursorTomorrow.getString(cursor.getColumnIndex("Date"));
                textViewArrayList.get(counter).setText(date);

                day = cursorTomorrow.getString(cursor.getColumnIndex("Day"));
                textViewArrayList.get(counter + 7).setText(day);

                time = cursorTomorrow.getString(cursor.getColumnIndex("Time"));
                textViewArrayList.get(counter + 14).setText(time);

                String highLow = cursorTomorrow.getString(cursor.getColumnIndex("HighLow"));
                height = cursorTomorrow.getString(cursor.getColumnIndex("Feet"));
                height += "ft " + highLow;
                textViewArrayList.get(counter + 21).setText(height);

                counter++;
                cursorTomorrow.move(1);
            }
            progressBar.setVisibility(View.INVISIBLE);
            warningTextView.setVisibility(View.INVISIBLE);
        }
        //done updating
    }

    //get tomorrow's date for given date
    public String getTomorrow(String date){
        final SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        try {
            final Date currentDate = format.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentDate);
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            return format.format(calendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    //check internet connection
    public static boolean checkNetWork(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null){
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null){
                if(networkInfo.isConnected()){
                    return true;
                }
            }
        }
        return false;
    }
}
