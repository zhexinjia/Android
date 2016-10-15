package com.zhexinj.piggame;

import java.util.Random;

/**
 * Created by zhexinjia on 6/23/16.
 */
public class PigGame {
    private String player1Name ,player2Name, winner;
    private int player1Score, player2Score, currentPoint, currentDice;
    private int goal = 100;
    private boolean player1Playing, gameFinished;

    PigGame(){
        restartGame();
    }

    //setters and getters
    public String getName1(){
        return player1Name;
    }
    public void setName1(String name){
        player1Name = name;
    }

    public String getName2(){
        return player2Name;
    }
    public void setName2(String name){
        player2Name = name;
    }

    public int getScore1(){
        return player1Score;
    }
    public void setScore1(int score){
        player1Score = score;
    }

    public int getScore2(){
        return player2Score;
    }
    public void setScore2(int score){
        player2Score = score;
    }

    public int getCurrentPoint(){
        return currentPoint;
    }
    public void setCurrentPoint(int point){
        currentPoint = point;
    }

    public boolean isPlayer1Playing(){
        return player1Playing;
    }
    public void setCurrentPlaying(boolean value){
        player1Playing = value;
    }

    public boolean isGameFinished(){
        return gameFinished;
    }
    public void setGameFinished(boolean value){
        gameFinished = value;
    }

    public int getCurrent_Dice(){
        return currentDice;
    }
    public void setCurrentDice(int num){
        currentDice = num;
    }

    public int getGoal(){
        return goal;
    }
    public void setGoal(int num){
        goal = num;
    }

    public String getWinner(){
        return winner;
    }
    public void findWinner(){
        if (player1Score >= goal | player2Score >= goal){
            if(player1Score > player2Score){
                winner = player1Name;
            }else if(player1Score < player2Score){
                winner = player2Name;
            }else{
                winner = "Tie";
            }
            gameFinished = true;
        }
    }

    public void rollDice(){
        if (isGameFinished() == false) {
            Random randomGenerator = new Random();
            currentDice = randomGenerator.nextInt(5) + 1;
            if (currentDice == 1){
                currentPoint = 0;
                endTurn();
            }else{
                currentPoint += currentDice;
            }
        }
    }

    public void endTurn(){
        if(player1Playing){
            player1Playing = false;
            player1Score = currentPoint + player1Score;
        }else {
            player1Playing = true;
            player2Score = currentPoint + player2Score;
            findWinner();
        }
        currentPoint = 0;
    }

    //check if some player reaches score goal
    public void gameChecking(){
        if (player1Playing){
            int total = player1Score + currentPoint;
            if (total >= goal){
                player1Score = total;
                endTurn();
            }
        }else{
            int total = player2Score + currentPoint;
            if(total >= goal){
                player2Score = total;
                findWinner();
            }
        }
    }

    public void restartGame(){
        player1Name = "";
        player2Name = "";
        winner = "";
        player1Score = 0;
        player2Score = 0;
        currentPoint = 0;
        player1Playing = true;
        gameFinished = false;

        currentDice = 1;
    }
}
