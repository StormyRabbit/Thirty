package lassesjoblom.thirty;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Lasse on 2017-06-08.
 */

public class GameLogic implements lassesjoblom.thirty.Observable, Parcelable {

    private int currentRound;
    private int diceThrows;

    public static final int MAX_NUMBER_OF_THROWS = 3;
    public static final int TOTAL_NUMBER_OF_DICES = 6;
    public static final int MAX_AMOUNT_OF_ROUNDS = 10;

    private ArrayList<Dice> diceList;
    private ArrayList<RoundScore> roundScoresList;
    private Observer gameActivityObserver;
    private boolean dicesRolled;
    private ScoreCalculator sCalc;

    public GameLogic() {
        diceList = new ArrayList<>(TOTAL_NUMBER_OF_DICES);
        roundScoresList = new ArrayList<>(MAX_AMOUNT_OF_ROUNDS);
        sCalc = new ScoreCalculator();
        createDices();

        updateRoundCounter();
        diceThrows = MAX_NUMBER_OF_THROWS;
    }

    public void addObserver(Observer o){
        this.gameActivityObserver = o;
    }

    public void notifyObserver(){
        Log.d("notifyObserver", diceList.toString());
        gameActivityObserver.update(currentRound, diceThrows, diceList);
    }

    public boolean isDicesRolled() {
        return dicesRolled;
    }

    public void setDicesRolled(boolean dicesRolled) {
        this.dicesRolled = dicesRolled;
    }

    public ArrayList<RoundScore> getRoundScoresList() {
        return roundScoresList;
    }

    public Dice setDiceMarker(int idx) {
        for(Dice d : diceList){
            if(d.getId() == idx){
                if(d.isMarked()) {
                    d.setMarked(false);
                }else {
                    d.setMarked(true);
                }
                return d;
            }

        }
        return null;
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public int getDiceThrows() {
        return diceThrows;
    }

    private void createDices() {

        for(int i = 0; i != TOTAL_NUMBER_OF_DICES; i++){
            Dice d = new Dice(i);
            d.resetDice();
            Log.d("creating dice #", String.valueOf(i));
            diceList.add(d);
            sCalc.addDice(d);
        }
    }
    public void resetDices() {
        for(Dice d : diceList) {
            d.resetDice();
        }
    }
    public ArrayList<Dice> getDices() {
        return diceList;
    }

    public void playThrow() {
        if(diceThrows != 0) {
            diceThrows--;
            throwUnmarkedDices();
            notifyObserver();
        }
    }

    public void unmarkDices() {
        for(Dice d : diceList){
            d.setMarked(false);
        }
    }

    public void endRound(String scoreRule) {
        roundScoresList.add(sCalc.calculateScore(scoreRule, diceList));
        updateRoundCounter();
        diceThrows = MAX_NUMBER_OF_THROWS;
        notifyObserver();
    }

    private void updateRoundCounter() {
        currentRound = roundScoresList.size() + 1;
    }

    private boolean throwUnmarkedDices() {
        boolean diceThrown = false;
        for(Dice d : diceList) {
            if(!d.isMarked()) {
                d.roll();
                diceThrown = true;
            }
        }
        Log.d("dices:", diceList.toString());
        return diceThrown;
    }

    /* Parcelable interface implementation */
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(currentRound);
        out.writeInt(diceThrows);
        out.writeTypedList(diceList);
        out.writeTypedList(roundScoresList);
    }

    public static final Parcelable.Creator<GameLogic> CREATOR
            = new Parcelable.Creator<GameLogic>() {
        public GameLogic createFromParcel(Parcel in) {
            return new GameLogic(in);
        }

        public GameLogic[] newArray(int size) {
            return new GameLogic[size];
        }
    };

    private GameLogic(Parcel in) {
        currentRound = in.readInt();
        diceThrows = in.readInt();
        in.readTypedList(diceList, Dice.CREATOR);
        in.readTypedList(roundScoresList, RoundScore.CREATOR );
    }
}
