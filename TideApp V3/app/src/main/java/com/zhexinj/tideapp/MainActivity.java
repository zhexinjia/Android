package com.zhexinj.tideapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    DatePicker datePicker;
    Spinner locationSpinner;
    Button showTideButton;
    int day, month, year;
    String city;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationSpinner = (Spinner)findViewById(R.id.spinner);
        locationSpinner.setOnItemSelectedListener(this);

        datePicker = (DatePicker)findViewById(R.id.datePicker);
        datePicker.setOnClickListener(this);

        showTideButton = (Button)findViewById(R.id.showTideButton);
        showTideButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.datePicker:
                //do something here
                break;
            case R.id.showTideButton:
                //create new intent and show stuff
                String monthString, dayString;
                day = datePicker.getDayOfMonth();
                month = datePicker.getMonth() + 1;
                year = datePicker.getYear();
                if(month < 10){
                    monthString = "0" + String.valueOf(month);
                }else{
                    monthString = String.valueOf(month);
                }
                if(day<10){
                    dayString = "0" + String.valueOf(day);
                }else{
                    dayString = String.valueOf(day);
                }
                String date = String.valueOf(year) + "/" + monthString + "/" + dayString;
                Intent intent = new Intent(getApplicationContext(), SecondActivity.class);
                intent.putExtra("date", date);
                intent.putExtra("city", city);
                startActivity(intent);
                //Toast.makeText(getApplicationContext(), city + " " + date, Toast.LENGTH_SHORT).show();
                break;
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        switch (position){
            case 0:
                city = "Half Moon Bay";
                break;
            case 1:
                city = "Florence USCG Pier";
                break;
            case 2:
                city = "Garibaldi";
                break;
            case 3:
                city = "Charleston";
                break;
            case 4:
                city = "Southbeach";
                break;
            case 5:
                city = "Yaquina River";
                break;
            case 6:
                city = "Dick Point";
                break;
            case 7:
                city = "Rogue River";
                break;
            case 8:
                city = "Port Orford";
                break;
            case 9:
                city = "Alsea Bay";
                break;
            case 10:
                city = "Depoe Bay";
                break;
            case 11:
                city = "Salmon River";
                break;

            default:
                break;
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
