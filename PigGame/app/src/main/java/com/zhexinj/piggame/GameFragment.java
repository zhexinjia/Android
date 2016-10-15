package com.zhexinj.piggame;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by zhexinjia on 7/3/16.
 */
public class GameFragment extends Fragment implements View.OnClickListener, TextView.OnEditorActionListener{

    FrameLayout myLayout;
    EditText nameOneEditText, nameTwoEditText;
    TextView scoreOneTextView, scoreTwoTextView;
    TextView currentPointTextView, currentPlayerTextView;
    Button rollButton, endButton, restartButton;
    ImageView dieImage;
    PigGame pigGame;

    View view;

    //define sharedPreferences object
    private SharedPreferences savedValues;

    //AI settings
    private boolean setAI = false;
    private boolean maxRollMode = true;
    private int counter = 0;

    private boolean startNewGameFromActivity;
    private String name1FromActivity;
    private String name2FromActivity;

    private String backgroundString;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.game_fragment, container, false);

        //Layout
        myLayout = (FrameLayout) view.findViewById(R.id.mainLayout);
        //EditText
        nameOneEditText = (EditText)view.findViewById(R.id.name1);
        nameTwoEditText = (EditText)view.findViewById(R.id.name2);
        nameOneEditText.setOnEditorActionListener(this);
        nameTwoEditText.setOnEditorActionListener(this);

        //Buttons
        rollButton = (Button)view.findViewById(R.id.rollButton);
        endButton = (Button)view.findViewById(R.id.endButton);
        restartButton = (Button)view.findViewById(R.id.restartButton);
        rollButton.setOnClickListener(this);
        endButton.setOnClickListener(this);
        restartButton.setOnClickListener(this);

        //Image
        dieImage = (ImageView)view.findViewById(R.id.imageView);

        //TextView
        currentPlayerTextView = (TextView)view.findViewById(R.id.turnLabel);
        currentPointTextView = (TextView)view.findViewById(R.id.score);
        scoreOneTextView = (TextView)view.findViewById(R.id.displayScore1);
        scoreTwoTextView = (TextView)view.findViewById(R.id.displayScore2);

        //get SharedPreferences object
        savedValues = this.getActivity().getSharedPreferences("SavedValues", getContext().MODE_PRIVATE);


        //new Game Object
        pigGame = new PigGame();



        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.menu_about:
                //getAcitivity() or getContext()// FIXME: 7/3/16 
                Toast.makeText(getActivity(), "This is my first App", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_setting:
                //// FIXME: 7/3/16 
                Intent settings = new Intent(getActivity(), SettingActivity.class);
                startActivity(settings);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.rollButton:
                if (pigGame.isGameFinished() == false){
                    if (NameValidate()){
                        pigGame.rollDice();
                        if(setAI && pigGame.isPlayer1Playing() == false){
                            runAI();
                        }
                        showWinner();
                    }else{
                        Toast.makeText(getActivity(), "Please enter both player's name", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getActivity(), "Click 'New Game' to start a new Game", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.endButton:
                if (pigGame.isGameFinished() == false){
                    if (NameValidate()){
                        pigGame.endTurn();
                        if(setAI){
                            runAI();
                        }
                        showWinner();
                    }else{
                        Toast.makeText(getActivity(), "Please enter both player's name", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getActivity(), "Click 'New Game' to start a new Game", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.restartButton:
                //pigGame.restartGame();
                startButton();
                break;
        }
        updateWidgets();

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        switch(v.getId()){
            case R.id.name1:
                pigGame.setName1(nameOneEditText.getText().toString());
                break;
            case R.id.name2:
                pigGame.setName2(nameTwoEditText.getText().toString());
                break;
        }
        updateWidgets();
        return false;
    }

    @Override
    public void onPause() {
        //store variable
        SharedPreferences.Editor editor = savedValues.edit();
        editor.putString("firstName", pigGame.getName1());
        editor.putString("secondName", pigGame.getName2());
        editor.putBoolean("currentPlaying", pigGame.isPlayer1Playing());
        editor.putBoolean("GameDone", pigGame.isGameFinished());
        editor.putInt("firstScore", pigGame.getScore1());
        editor.putInt("secondScore", pigGame.getScore2());
        editor.putInt("currentScore", pigGame.getCurrentPoint());
        editor.putInt("currentDice", pigGame.getCurrent_Dice());
        editor.putString("background", backgroundString);
        if (maxRollMode == true){
            editor.putString("AIsetting", "0");
        }else{
            editor.putString("AIsetting", "1");
        }
        //editor.putString("AIsetting", String.valueOf(maxRollMode));
        editor.putBoolean("AIplaying", setAI);
        editor.putString("setGoal", String.valueOf(pigGame.getGoal()) );
        editor.commit();

        super.onPause();
    }
    @Override
    public void onResume(){
        super.onResume();
        restoreSettings();
        //get variable
        pigGame.setName1(savedValues.getString("firstName", ""));
        pigGame.setName2(savedValues.getString("secondName", ""));
        pigGame.setCurrentPlaying(savedValues.getBoolean("currentPlaying", true));
        pigGame.setGameFinished(savedValues.getBoolean("GameDone", false));
        pigGame.setScore1(savedValues.getInt("firstScore", 0));
        pigGame.setScore2(savedValues.getInt("secondScore", 0));
        pigGame.setCurrentPoint(savedValues.getInt("currentScore", 0));
        pigGame.setCurrentDice(savedValues.getInt("currentDice", 0));
        if (pigGame.isPlayer1Playing() == false){
            pigGame.gameChecking();
            if (pigGame.isGameFinished() == false && setAI == true){
                runAI();
            }
        }
        updateWidgets();

        try{
            GameActivity gameActivity = (GameActivity)getActivity();
            name1FromActivity = gameActivity.getName1();
            name2FromActivity = gameActivity.getName2();
            startNewGameFromActivity = gameActivity.getNewGame();
        }catch(Exception e){}
        if (startNewGameFromActivity == true){
            pigGame.restartGame();
            pigGame.setName1(name1FromActivity);
            pigGame.setName2(name2FromActivity);
            updateWidgets();


        }
    }

    private void updateWidgets(){
        nameOneEditText.setText(pigGame.getName1());
        nameTwoEditText.setText(pigGame.getName2());
        scoreOneTextView.setText(String.valueOf(pigGame.getScore1()));
        scoreTwoTextView.setText(String.valueOf(pigGame.getScore2()));
        currentPointTextView.setText(String.valueOf(pigGame.getCurrentPoint()));
        if (pigGame.isPlayer1Playing()){
            currentPlayerTextView.setText(pigGame.getName1() + "'s turn");
        }else{
            currentPlayerTextView.setText(pigGame.getName2() + "'s turn");
        }
        switch(pigGame.getCurrent_Dice()){
            case 1:
                dieImage.setImageResource(R.drawable.die1);
                break;
            case 2:
                dieImage.setImageResource(R.drawable.die2);
                break;
            case 3:
                dieImage.setImageResource(R.drawable.die3);
                break;
            case 4:
                dieImage.setImageResource(R.drawable.die4);
                break;
            case 5:
                dieImage.setImageResource(R.drawable.die5);
                break;
            case 6:
                dieImage.setImageResource(R.drawable.die6);
                break;
        }
    }

    public boolean NameValidate(){
        if (pigGame.getName1() == "" | pigGame.getName2() == ""){
            return false;
        }else{
            return true;
        }
    }

    public void showWinner(){
        if (pigGame.getWinner() != ""){
            if (pigGame.getWinner() == "Tie"){
                currentPlayerTextView.setText("Tie Game");
                Toast.makeText(getActivity(), "Tie Game", Toast.LENGTH_SHORT).show();
            }else{
                currentPlayerTextView.setText(pigGame.getWinner() + " Wins");
                Toast.makeText(getActivity(), pigGame.getWinner() + " wins", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void setBackround(int num){
        if (num == 0){
            myLayout.setBackgroundResource(R.drawable.background);
        }else if(num == 1){
            myLayout.setBackgroundResource(R.drawable.background1);
        }else{
            myLayout.setBackgroundResource(R.drawable.background2);
        }
    }

    public void restoreSettings(){
        //setup background
        backgroundString = savedValues.getString("background", "0");
        setBackround(Integer.parseInt(backgroundString));

        //setup score to play to
        String totalScore = savedValues.getString("setGoal", "0");
        pigGame.setGoal(Integer.parseInt(totalScore));

        //setup AI setting
        setAI = savedValues.getBoolean("AIplaying", false);
        String AIsetting = savedValues.getString("AIsetting", "0");
        if (AIsetting == "0"){
            maxRollMode = true;
        }else{
            maxRollMode = false;
        }
    }


    public void runAI(){
        rollButton.setEnabled(false);
        endButton.setEnabled(false);
        restartButton.setEnabled(false);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                rollButton.setEnabled(false);
                endButton.setEnabled(false);
                if(maxRollMode == true){
                    counter++;
                    pigGame.rollDice();
                    updateWidgets();
                    if(counter < 4 && pigGame.isPlayer1Playing() == false){
                        runAI();
                    }
                    if (counter == 4 && pigGame.isPlayer1Playing() == false){
                        pigGame.endTurn();
                        updateWidgets();
                    }
                    if(counter == 4 | pigGame.isPlayer1Playing() == true){
                        rollButton.setEnabled(true);
                        endButton.setEnabled(true);
                        restartButton.setEnabled(true);
                        showWinner();
                        counter = 0;
                    }
                }else{
                    pigGame.rollDice();
                    updateWidgets();
                    if(pigGame.getCurrentPoint() < 12 && pigGame.isPlayer1Playing() == false){
                        runAI();
                    }
                    if (pigGame.getCurrentPoint() >= 12 && pigGame.isPlayer1Playing() == false){
                        pigGame.endTurn();
                        updateWidgets();
                    }
                    if(pigGame.getCurrentPoint() >= 12 | pigGame.isPlayer1Playing() == true) {
                        rollButton.setEnabled(true);
                        endButton.setEnabled(true);
                        restartButton.setEnabled(true);
                        showWinner();
                    }
                }
            }
        }, 1500);
    }

    private void startButton(){
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        int orientation = display.getRotation();
        if(orientation == Surface.ROTATION_90 | orientation == Surface.ROTATION_270){
            //landscape mode
            pigGame.restartGame();
        }else{
            getActivity().finish();
        }
    }


}
