package com.zhexinj.blackjack;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by zhexinjia on 7/11/16.
 */
public class GameHistoryActivity extends AppCompatActivity {
    TextView highestScoreTextView, numberOfGameTextView, numberofResetTextView;
    private SharedPreferences savedValues;
    private String backGroundColor;
    RelativeLayout historyLayout;


    private int gameCounter, resetCounter, highestScore;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_history);

        highestScoreTextView = (TextView)findViewById(R.id.highestScore);
        numberOfGameTextView = (TextView)findViewById(R.id.numberOfGame);
        numberofResetTextView = (TextView)findViewById(R.id.numberOfReset);
        historyLayout = (RelativeLayout)findViewById(R.id.historyLayout);
        savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    public void onResume(){
        super.onResume();
        highestScore = savedValues.getInt("highestScore", 1000);
        resetCounter = savedValues.getInt("resetNumber", 0);
        gameCounter = savedValues.getInt("gameNumber", 0);
        backGroundColor = savedValues.getString("background", "Green");

        highestScoreTextView.setText(String.valueOf(highestScore));
        numberOfGameTextView.setText(String.valueOf(gameCounter));
        numberofResetTextView.setText(String.valueOf(resetCounter));

        if(backGroundColor.equals("Green")){
            historyLayout.setBackgroundColor(Color.parseColor("#145807"));
        }else{
            historyLayout.setBackgroundColor(Color.parseColor("#58091f"));
        }
    }


    //Method to show Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.menu_about:
                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.dialog);
                dialog.setTitle("About");
                dialog.show();
                return true;
            case R.id.menu_setting:
                Intent settings = new Intent(GameHistoryActivity.this, SettingActivity.class);
                startActivity(settings);
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
