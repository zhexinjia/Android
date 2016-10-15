package com.zhexinj.blackjack;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by zhexinjia on 7/11/16.
 */
public class SettingActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingFragment()).commit();

    }
}