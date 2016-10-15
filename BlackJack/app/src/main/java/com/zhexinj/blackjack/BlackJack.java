package com.zhexinj.blackjack;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/**
 * Created by zhexinjia on 7/4/16.
 */
public class BlackJack {

    private int[] deck = new int[52];
    private int money, moneyOnDesk;

    private static ArrayList<Integer> playerHand = new ArrayList<Integer>();
    private static ArrayList<Integer> dealerHand = new ArrayList<Integer>();
    private int playerScore, dealerScore;
    private static Queue<Integer> deckQueue;
    Random randomGenerator = new Random();
    private boolean finished = false;
    private boolean playerPlaying = true;
    String winner = "";


    BlackJack(){
        restartGame();
    }

    public int getMoney() {
        return money;
    }

    public int getMoneyOnDesk() {
        return moneyOnDesk;
    }

    public ArrayList<Integer> getPlayerHand() {
        return playerHand;
    }

    public ArrayList<Integer> getDealerHand() {
        return dealerHand;
    }

    public Queue<Integer> getDeckQueue(){
        return deckQueue;
    }

    public int getPlayerScore() {
        return playerScore;
    }

    public int getDealerScore() {
        return dealerScore;
    }



    public boolean isFinished() {
        return finished;
    }

    public boolean isPlayerPlaying() {
        return playerPlaying;
    }

    public String getWinner() {
        return winner;
    }

    public void setPlayerHand(ArrayList<Integer> arrayList){
        playerHand = arrayList;
    }
    public void setDealerHand(ArrayList<Integer> arrayList){
        dealerHand = arrayList;
    }
    public void setDeckQueue(Queue<Integer> queue){
        deckQueue = queue;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void setMoneyOnDesk(int moneyOnDesk) {
        this.moneyOnDesk = moneyOnDesk;
    }


    public void setPlayerScore(int playerScore) {
        this.playerScore = playerScore;
    }

    public void setDealerScore(int dealerScore) {
        this.dealerScore = dealerScore;
    }



    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public void setPlayerPlaying(boolean playerPlaying) {
        this.playerPlaying = playerPlaying;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public void generateDeck(){
        //add shuffled card deck to a queue
        deckQueue = new LinkedList<Integer>();
        for (int i = 0; i < 52; i++){
            deckQueue.add(deck[i]);
        }
    }

    public void initialDeck(){
        //create a deck and randomly shuffle it 3 times
        for (int i = 0; i < 52; i++){
            deck[i] = i+1;
        }

        for (int counter = 0; counter < 3; counter++) {
            for (int i = 0; i < 52; i++) {
                int index = randomGenerator.nextInt(52);
                int temp = deck[index];
                deck[index] = deck[i];
                deck[i] = temp;
            }
        }
    }

    public void dealGame(){
        //start a new game, but do not reset the money
        initialDeck();
        generateDeck();
        playerHand.clear();
        dealerHand.clear();
        finished = false;
        playerPlaying = true;

        playerScore = 0;
        dealerScore = 0;
        winner = "";
        playerHand.add(deckQueue.poll());
        dealerHand.add(deckQueue.poll());
        playerHand.add(deckQueue.poll());
        dealerHand.add(deckQueue.poll());
        caculateScore();
    }


    public void restartGame(){
        //reset money in hand to 1000, usually click it when out of money
        money = 1000;
        moneyOnDesk = 0;
        dealGame();
    }

    public void addMoney(){
        if (money >= 100){
            money -= 100;
            moneyOnDesk += 100;
        }
    }
    public void minusMoney(){
        if(moneyOnDesk>=100){
            moneyOnDesk -= 100;
            money += 100;
        }
    }

    public void draw(){
        //press hit button, draw one card
        if (playerPlaying){
            playerHand.add(deckQueue.poll());
            statusChecking();
        }else{
            dealerHand.add(deckQueue.poll());
            statusChecking();
        }
    }


    public void pass(){
        //do not draw card and pass, hit stand button
        if (playerPlaying){
            playerPlaying = false;
        }else{
            findWinner();
            finished = true;
        }
        //dealer's turn
    }

    public void statusChecking(){
        //check game status, win or lose
        caculateScore();
        if (playerPlaying){
            if (playerScore > 21){
                finished = true;
                winner = "dealer";
                moneyOnDesk = 0;
            }else if(playerScore == 21){
                pass();
            }
        }else{
            if (dealerScore == 21){
                findWinner();
                finished = true;
            }else if(dealerScore > 21){
                winner = "player";
                money += 2*moneyOnDesk;
                moneyOnDesk = 0;
                finished = true;
            }else if(dealerScore >= 17){
                findWinner();
            }
        }

    }

    public void findWinner(){
        if(playerScore > dealerScore && playerScore <= 21){
            winner = "player";
            money += 2*moneyOnDesk;
            moneyOnDesk = 0;
        }else if(playerScore == dealerScore && playerScore <= 21){
            winner = "Tie";
            money += moneyOnDesk;
            moneyOnDesk = 0;
        }else if(playerScore <= 21 && dealerScore > 21){
            winner = "player";
            money += 2*moneyOnDesk;
            moneyOnDesk = 0;
        }else{
            winner = "dealer";
        }

        if (winner.equals("player")){
            money += moneyOnDesk*2;
            moneyOnDesk = 0;
        }else if(winner.equals("Tie")){
            money += moneyOnDesk;
            moneyOnDesk = 0;
        }else{
            moneyOnDesk = 0;
        }
    }

    public int getTotalPoint(ArrayList<Integer> list){
        //get total score for given player/dealer hand
        int sum = 0;
        int ace = 0;
        for (int i = 0; i< list.size(); i++){
            if (getCardValue(list.get(i)) == 1){
                ace = 10;
            }
            sum += getCardValue(list.get(i));
        }
        if ((sum+ace) <= 21){
            sum = sum+ace;
        }
        return sum;
    }

    public int getCardValue(int card){
        //set initial 52 cards values
        if (card == 1 || card == 14 || card == 27 || card == 40){
            return 1;
        }else if(card == 2 || card == 15 || card == 28 || card == 41){
            return 2;
        }else if(card == 3 || card == 16 || card == 29 || card == 42){
            return 3;
        }else if(card == 4 || card == 17 || card == 30 || card == 43){
            return 4;
        }else if(card == 5 || card == 18 || card == 31 || card == 44){
            return 5;
        }else if(card == 6 || card == 19 || card == 32 || card == 45){
            return 6;
        }else if(card == 7 || card == 20 || card == 33 || card == 46){
            return 7;
        }else if(card == 8 || card == 21 || card == 34 || card == 47){
            return 8;
        }else if(card == 9 || card == 22 || card == 35 || card == 48){
            return 9;
        }else if(card == 10 || card == 23 || card == 36 || card == 49){
            return 10;
        }else if(card == 11 || card == 24 || card == 37 || card == 50){
            return 10;
        }else if(card == 12 || card == 25 || card == 38 || card == 51){
            return 10;
        }else if(card == 13 || card == 26 || card == 39 || card == 52){
            return 10;
        }else{
            return 0;
        }
    }

    public void caculateScore(){
        playerScore = getTotalPoint(playerHand);
        dealerScore = getTotalPoint(dealerHand);
    }

}

