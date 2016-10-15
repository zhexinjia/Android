package com.zhexinj.piggame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by zhexinjia on 7/3/16.
 */
public class StartFragment extends Fragment implements View.OnClickListener, TextView.OnEditorActionListener {

    private EditText name1EditText, name2EditText;
    private Button startButton;
    private String name1 = "";
    private String name2 = "";
    View view;
    private MainActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.start_fragment, container, false);
        name1EditText = (EditText)view.findViewById(R.id.player1nameEditText);
        name2EditText = (EditText)view.findViewById(R.id.player2nameEditText);
        name1EditText.setText("");
        name2EditText.setText("");
        name1EditText.setOnEditorActionListener(this);
        name2EditText.setOnEditorActionListener(this);
        startButton = (Button)view.findViewById(R.id.newgameButton);
        startButton.setOnClickListener(this);
        activity = (MainActivity)getActivity();

        return view;
    }

    @Override
    public void onClick(View v) {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        int orientation = display.getRotation();
        if(orientation == Surface.ROTATION_90 | orientation == Surface.ROTATION_270){
            //landScape mode
            if (NameValidate()) {
                activity = (MainActivity) getActivity();
                activity.setName1(name1);
                activity.setName2(name2);
                activity.setNameGame(true);
                activity.update();
            }else{
                Toast.makeText(getActivity(), "Please enter both player's name", Toast.LENGTH_SHORT).show();
            }

        }else{
            //port mode
            if (NameValidate()) {
                Intent game = new Intent(getActivity(), GameActivity.class);
                game.putExtra("name1", name1);
                game.putExtra("name2", name2);
                game.putExtra("startNewGame", true);
                startActivity(game);
            }else{
                Toast.makeText(getActivity(), "Please enter both player's name", Toast.LENGTH_SHORT).show();
            }
        }


    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        switch (v.getId()){
            case R.id.player1nameEditText:
                name1 = name1EditText.getText().toString();
                break;
            case R.id.player2nameEditText:
                name2 = name2EditText.getText().toString();
                break;
        }

        return false;
    }

    public boolean NameValidate(){
        if (name1 == ""  || name2 == ""){
            return false;
        }else{
            return true;
        }
    }
}
