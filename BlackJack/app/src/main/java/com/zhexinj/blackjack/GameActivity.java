package com.zhexinj.blackjack;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    ArrayList<ImageView> playerCardImageList;
    ArrayList<ImageView> dealerCardImageList;
    private boolean isCheatOn;
    private boolean dealButtonClicked;
    private String backGroundColor = "Green";
    int[] imageArray;
    RelativeLayout myLayout;
    private static ArrayList<Integer> playerHand, dealerHand;
    private static Queue<Integer> deckQueue;
    TextView moneyOnDesk, moneyInHand, winnerTextView;
    Button newGameButton, addMoneyButton, passButton, hitButton, dealButton, minusButton;
    ImageView dealerCard1ImageView, dealerCard2ImageView,dealerCard3ImageView,dealerCard4ImageView,dealerCard5ImageView,dealerCard6ImageView,dealerCard7ImageView,dealerCard8ImageView,dealerCard9ImageView;
    ImageView playerCard1ImageView, playerCard2ImageView,playerCard3ImageView,playerCard4ImageView,playerCard5ImageView,playerCard6ImageView,playerCard7ImageView,playerCard8ImageView,playerCard9ImageView;

    BlackJack blackJack;
    private int highestScore = 1000;
    private int resetCounter = 0, gameCounter = 0;

    private SharedPreferences savedValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        myLayout = (RelativeLayout)findViewById(R.id.myLayout);
        //textView
        moneyOnDesk = (TextView)findViewById(R.id.turnMoney);
        moneyInHand = (TextView)findViewById(R.id.handMoney);
        winnerTextView = (TextView)findViewById(R.id.winnerTextView);

        //Buttons
        newGameButton = (Button)findViewById(R.id.restartButton);
        newGameButton.setOnClickListener(this);
        addMoneyButton = (Button)findViewById(R.id.raiseButton);
        addMoneyButton.setOnClickListener(this);
        passButton = (Button)findViewById(R.id.passButton);
        passButton.setOnClickListener(this);
        hitButton = (Button)findViewById(R.id.hitButton);
        hitButton.setOnClickListener(this);
        dealButton = (Button)findViewById(R.id.dealButton);
        dealButton.setOnClickListener(this);
        minusButton = (Button)findViewById(R.id.minusButton);
        minusButton.setOnClickListener(this);


        dealerCardImageList = new ArrayList<ImageView>();
        playerCardImageList = new ArrayList<ImageView>();


        //imageViews
        dealerCard1ImageView = (ImageView)findViewById(R.id.dealerCard1);
        dealerCard2ImageView = (ImageView)findViewById(R.id.dealerCard2);
        dealerCard3ImageView = (ImageView)findViewById(R.id.dealerCard3);
        dealerCard4ImageView = (ImageView)findViewById(R.id.dealerCard4);
        dealerCard5ImageView = (ImageView)findViewById(R.id.dealerCard5);
        dealerCard6ImageView = (ImageView)findViewById(R.id.dealerCard6);
        dealerCard7ImageView = (ImageView)findViewById(R.id.dealerCard7);
        dealerCard8ImageView = (ImageView)findViewById(R.id.dealerCard8);
        dealerCard9ImageView = (ImageView)findViewById(R.id.dealerCard9);

        dealerCardImageList.add(dealerCard1ImageView);
        dealerCardImageList.add(dealerCard2ImageView);
        dealerCardImageList.add(dealerCard3ImageView);
        dealerCardImageList.add(dealerCard4ImageView);
        dealerCardImageList.add(dealerCard5ImageView);
        dealerCardImageList.add(dealerCard6ImageView);
        dealerCardImageList.add(dealerCard7ImageView);
        dealerCardImageList.add(dealerCard8ImageView);
        dealerCardImageList.add(dealerCard9ImageView);

        playerCard1ImageView = (ImageView)findViewById(R.id.playerCard1);
        playerCard2ImageView = (ImageView)findViewById(R.id.playerCard2);
        playerCard3ImageView = (ImageView)findViewById(R.id.playerCard3);
        playerCard4ImageView = (ImageView)findViewById(R.id.playerCard4);
        playerCard5ImageView = (ImageView)findViewById(R.id.playerCard5);
        playerCard6ImageView = (ImageView)findViewById(R.id.playerCard6);
        playerCard7ImageView = (ImageView)findViewById(R.id.playerCard7);
        playerCard8ImageView = (ImageView)findViewById(R.id.playerCard8);
        playerCard9ImageView = (ImageView)findViewById(R.id.playerCard9);
        playerCardImageList.add(playerCard1ImageView);
        playerCardImageList.add(playerCard2ImageView);
        playerCardImageList.add(playerCard3ImageView);
        playerCardImageList.add(playerCard4ImageView);
        playerCardImageList.add(playerCard5ImageView);
        playerCardImageList.add(playerCard6ImageView);
        playerCardImageList.add(playerCard7ImageView);
        playerCardImageList.add(playerCard8ImageView);
        playerCardImageList.add(playerCard9ImageView);

        blackJack = new BlackJack();
        initializeImageArray();
        updateWidgets();
        hitButton.setEnabled(false);
        passButton.setEnabled(false);
        savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE);

    }

    @Override
    public void onPause(){
        SharedPreferences.Editor editor = savedValues.edit();
        editor.putInt("highestScore", highestScore);
        editor.putInt("resetNumber", resetCounter);
        editor.putInt("gameNumber", gameCounter);
        editor.putString("Winner", blackJack.getWinner());
        editor.putInt("MoneyInHand", blackJack.getMoney());
        editor.putInt("MoneyOnDeck", blackJack.getMoneyOnDesk());
        editor.putInt("dealerScore", blackJack.getDealerScore());
        editor.putInt("playerScore", blackJack.getPlayerScore());
        editor.putBoolean("finished", blackJack.isFinished());
        editor.putBoolean("playing", blackJack.isPlayerPlaying());
        editor.putBoolean("dealButtonClicked", dealButtonClicked);

        playerHand = new ArrayList<>(blackJack.getPlayerHand());
        dealerHand = new ArrayList<>(blackJack.getDealerHand());
        deckQueue = new LinkedList<>(blackJack.getDeckQueue());

        editor.commit();



        super.onPause();
    }

    @Override
    public void onResume(){
        super.onResume();
        highestScore = (savedValues.getInt("highestScore", 1000));
        resetCounter = (savedValues.getInt("resetNumber", 0));
        gameCounter = (savedValues.getInt("gameNumber", 0));
        blackJack.setWinner(savedValues.getString("Winner", ""));
        blackJack.setMoney(savedValues.getInt("MoneyInHand", 1000));
        blackJack.setMoneyOnDesk(savedValues.getInt("MoneyOnDeck", 0));
        blackJack.setDealerScore(savedValues.getInt("dealerScore", 0));
        blackJack.setPlayerScore(savedValues.getInt("playerScore", 0));
        blackJack.setFinished(savedValues.getBoolean("finished", false));
        blackJack.setPlayerPlaying(savedValues.getBoolean("playing", true));


        dealButtonClicked = savedValues.getBoolean("dealButtonClicked", false);
        isCheatOn = savedValues.getBoolean("Cheat Mode", false);
        backGroundColor = savedValues.getString("background", "Green");
        if (playerHand != null){
            blackJack.setPlayerHand(playerHand);
        }
        if(dealerHand!= null){
            blackJack.setDealerHand(dealerHand);
        }
        if(deckQueue != null){
            blackJack.setDeckQueue(deckQueue);
        }
        updateWidgets();
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
                Intent settings = new Intent(GameActivity.this, SettingActivity.class);
                startActivity(settings);
                return true;
            case R.id.menu_history:
                Intent historyActivity = new Intent(GameActivity.this, GameHistoryActivity.class);
                startActivity(historyActivity);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.restartButton:
                for (int i = 0; i< 9; i++){
                    dealerCardImageList.get(i).setImageResource(0);
                    playerCardImageList.get(i).setImageResource(0);
                }
                blackJack.restartGame();
                resetCounter++;
                gameCounter++;
                break;
            case R.id.raiseButton:
                blackJack.addMoney();
                break;
            case R.id.minusButton:
                blackJack.minusMoney();
                break;
            case R.id.hitButton:
                blackJack.draw();
                if (blackJack.getPlayerScore() == 21){
                    dealerDrawCards();
                }
                break;
            case R.id.passButton:
                System.out.println("going to pass");
                blackJack.pass();
                System.out.println("pass done, dealer going to draw");
                dealerDrawCards();
                showWinner();
                System.out.println("dealer done draw card.");
                break;
            case R.id.dealButton:
                if(blackJack.getMoneyOnDesk() == 0){
                    Toast.makeText(getApplicationContext(), "Please Place Bet", Toast.LENGTH_SHORT).show();
                }else {
                    for (int i = 0; i < 9; i++) {
                        dealerCardImageList.get(i).setImageResource(0);
                        playerCardImageList.get(i).setImageResource(0);
                    }
                    dealButtonClicked = true;
                    blackJack.dealGame();
                    gameCounter++;
                }
                break;
        }
        updateWidgets();
    }





    public void updateWidgets(){
        //update widgets after each button was clicked, or screen rotated
        if(dealButtonClicked){
            dealButton.setVisibility(View.INVISIBLE);
            addMoneyButton.setEnabled(false);
            minusButton.setEnabled(false);
            passButton.setEnabled(true);
            hitButton.setEnabled(true);
        }else{
            dealButton.setVisibility(View.VISIBLE);
            addMoneyButton.setEnabled(true);
            minusButton.setEnabled(true);
            passButton.setEnabled(false);
            hitButton.setEnabled(false);

        }

        if(backGroundColor.equals("Green")){
            myLayout.setBackgroundColor(Color.parseColor("#145807"));
        }else{
            myLayout.setBackgroundColor(Color.parseColor("#58091f"));
        }

        if (isCheatOn) {
            //show dealer's first card if cheat is on
            for (int i = 0; i < blackJack.getPlayerHand().size(); i++) {
                int cardIndex = blackJack.getPlayerHand().get(i);
                setCardImage(playerCardImageList.get(i), cardIndex);
            }
            for (int i = 0; i < blackJack.getDealerHand().size(); i++) {
                int cardIndex = blackJack.getDealerHand().get(i);
                setCardImage(dealerCardImageList.get(i), cardIndex);
            }
            moneyOnDesk.setText(String.valueOf(blackJack.getMoneyOnDesk()));
            moneyInHand.setText(String.valueOf(blackJack.getMoney()));
            showWinner();
        }else{
            if(blackJack.isPlayerPlaying() == false) {
                //show dealer's first card if its dealer's turn
                for (int i = 0; i < blackJack.getPlayerHand().size(); i++) {
                    int cardIndex = blackJack.getPlayerHand().get(i);
                    setCardImage(playerCardImageList.get(i), cardIndex);
                }
                for (int i = 0; i < blackJack.getDealerHand().size(); i++) {
                    int cardIndex = blackJack.getDealerHand().get(i);
                    setCardImage(dealerCardImageList.get(i), cardIndex);
                }
                moneyOnDesk.setText(String.valueOf(blackJack.getMoneyOnDesk()));
                moneyInHand.setText(String.valueOf(blackJack.getMoney()));
                showWinner();
            }else{
                //do not show dealer's first card
                setCardImage(dealerCardImageList.get(0), 0);
                for (int i = 0; i < blackJack.getPlayerHand().size(); i++) {
                    int cardIndex = blackJack.getPlayerHand().get(i);
                    setCardImage(playerCardImageList.get(i), cardIndex);
                }
                for (int i = 1; i < blackJack.getDealerHand().size(); i++) {
                    int cardIndex = blackJack.getDealerHand().get(i);
                    setCardImage(dealerCardImageList.get(i), cardIndex);
                }
                moneyOnDesk.setText(String.valueOf(blackJack.getMoneyOnDesk()));
                moneyInHand.setText(String.valueOf(blackJack.getMoney()));
                showWinner();
            }
        }
        if (blackJack.getMoney() > highestScore){
            highestScore = blackJack.getMoney();
        }

    }

    public void setCardImage(ImageView imageView, int cardIndex){
        imageView.setImageResource(imageArray[cardIndex]);
    }

    public void initializeImageArray(){
        imageArray = new int[53];
        imageArray[0] = R.drawable.back;
        imageArray[1] = R.drawable.club1;
        imageArray[2] = R.drawable.club2;
        imageArray[3] = R.drawable.club3;
        imageArray[4] = R.drawable.club4;
        imageArray[5] = R.drawable.club5;
        imageArray[6] = R.drawable.club6;
        imageArray[7] = R.drawable.club7;
        imageArray[8] = R.drawable.club8;
        imageArray[9] = R.drawable.club9;
        imageArray[10] = R.drawable.club10;
        imageArray[11] = R.drawable.club11;
        imageArray[12] = R.drawable.club12;
        imageArray[13] = R.drawable.club13;

        imageArray[14] = R.drawable.diamond1;
        imageArray[15] = R.drawable.diamond2;
        imageArray[16] = R.drawable.diamond3;
        imageArray[17] = R.drawable.diamond4;
        imageArray[18] = R.drawable.diamond5;
        imageArray[19] = R.drawable.diamond6;
        imageArray[20] = R.drawable.diamond7;
        imageArray[21] = R.drawable.diamond8;
        imageArray[22] = R.drawable.diamond9;
        imageArray[23] = R.drawable.diamond10;
        imageArray[24] = R.drawable.diamond11;
        imageArray[25] = R.drawable.diamond12;
        imageArray[26] = R.drawable.diamond13;

        imageArray[27] = R.drawable.heart1;
        imageArray[28] = R.drawable.heart2;
        imageArray[29] = R.drawable.heart3;
        imageArray[30] = R.drawable.heart4;
        imageArray[31] = R.drawable.heart5;
        imageArray[32] = R.drawable.heart6;
        imageArray[33] = R.drawable.heart7;
        imageArray[34] = R.drawable.heart8;
        imageArray[35] = R.drawable.heart9;
        imageArray[36] = R.drawable.heart10;
        imageArray[37] = R.drawable.heart11;
        imageArray[38] = R.drawable.heart12;
        imageArray[39] = R.drawable.heart13;

        imageArray[40] = R.drawable.spade1;
        imageArray[41] = R.drawable.spade2;
        imageArray[42] = R.drawable.spade3;
        imageArray[43] = R.drawable.spade4;
        imageArray[44] = R.drawable.spade5;
        imageArray[45] = R.drawable.spade6;
        imageArray[46] = R.drawable.spade7;
        imageArray[47] = R.drawable.spade8;
        imageArray[48] = R.drawable.spade9;
        imageArray[49] = R.drawable.spade10;
        imageArray[50] = R.drawable.spade11;
        imageArray[51] = R.drawable.spade12;
        imageArray[52] = R.drawable.spade13;

    }

    public void showWinner(){
        if (blackJack.getWinner() != ""){
            setCardImage(dealerCardImageList.get(0), blackJack.getDealerHand().get(0));
            if(blackJack.getWinner().equals("player")){
                winnerTextView.setText("Congratulations, You Win!");
            }else if(blackJack.getWinner().equals("dealer")){
                winnerTextView.setText("Dealer Wins");
            }else{
                winnerTextView.setText("Tie Game");
            }
            addMoneyButton.setEnabled(false);
            hitButton.setEnabled(false);
            passButton.setEnabled(false);
            minusButton.setEnabled(false);
            if (blackJack.getMoney() >= 100){
                dealButton.setVisibility(View.VISIBLE);
                addMoneyButton.setEnabled(true);
                minusButton.setEnabled(true);
            }
            dealButtonClicked = false;
        }else{
            winnerTextView.setText("");
        }

    }

    public void dealerDrawCards(){
        while (blackJack.getDealerScore() < 17){
            blackJack.draw();
            updateWidgets();
        }
        blackJack.findWinner();
        updateWidgets();
    }

    //Print the ArrayList, used to test decks and player hands for rotating
    /**
    public void printArray(ArrayList<Integer> arrayList){
        System.out.println("printing array:");
        for (int i = 0; i< arrayList.size(); i++){
            System.out.println(arrayList.get(i).toString());
        }
    }
     **/
}
