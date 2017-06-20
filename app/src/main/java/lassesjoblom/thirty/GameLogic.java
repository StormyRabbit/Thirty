package lassesjoblom.thirty;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.Observable;

/**
 * Created by Lasse on 2017-06-08.
 */

public class GameLogic implements lassesjoblom.thirty.Observable {

    private int currentRound;
    private int rethrow;

    private ArrayList<Dice> diceList;

    private ArrayList<RoundScore> roundScoresList;
    private Observer o;
    private boolean dicesRolled;

    public void addObserver(Observer o){
        this.o = o;
    }

    public void notifyObserver(){
        o.update(currentRound, rethrow, diceList);
    }

    public boolean isDicesRolled() {
        return dicesRolled;
    }

    public void setDicesRolled(boolean dicesRolled) {
        this.dicesRolled = dicesRolled;
    }

    private ScoreCalculator sCalc;

    public RoundScore getScore() {
        return sCalc.getCurrentRoundScore();
    }

    public ArrayList<RoundScore> getRoundScoresList() {
        return roundScoresList;
    }

    public void setRoundScoresList(ArrayList<RoundScore> roundScores) {
        this.roundScoresList = roundScores;
    }
    public GameLogic() {
        diceList = new ArrayList<>(6);
        roundScoresList = new ArrayList<>(10);
        sCalc = new ScoreCalculator();
        addDices();
        currentRound = 1;
        rethrow = 2;
    }

    public void setDiceList(ArrayList<Dice> diceList) {
        this.diceList = diceList;
    }

    public Dice setDiceMarker(int idx) {
        Dice d = diceList.get(idx);
        if(d.isMarked()) {
            d.setMarked(false);
        }else {
            d.setMarked(true);
        }
        return d;
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public void setCurrentRound(int currentRound) {
        this.currentRound = currentRound;
    }

    public int getRethrow() {
        return rethrow;
    }

    public void setRethrow(int rethrow) {
        this.rethrow = rethrow;
    }

    private void addDices() {
        for(int i = 0; i != 6; i++){
            Dice d = new Dice(i);
            diceList.add(d);
            sCalc.addDice(d);
        }
    }

    public ArrayList<Dice> getDices() {
        return diceList;
    }

    public void playThrow( ){
        if(rethrow != 0) {
            rethrow--;
            throwUnmarkedDices();
            notifyObserver();
        }
    }

    public void endRound(String scoreRule) {
        getRoundScore(scoreRule);
        currentRound++;
        rethrow = 2;
        notifyObserver();
    }
    private void getRoundScore(String scoreRule) {
        int pointValue = 0;
        switch ( scoreRule ) {
            case "4":
                pointValue = 4;
                break;
            case "5":
                pointValue = 5;
                break;
            case "6":
                pointValue = 6;
                break;
            case "7":
                pointValue = 7;
                break;
            case "8":
                pointValue = 8;
                break;
            case "9":
                pointValue = 9;
                break;
            case "10":
                pointValue = 10;
                break;
            case "11":
                pointValue = 11;
                break;
            case "12":
                pointValue = 12;
                break;

        }
        roundScoresList.add(sCalc.calculateScore(pointValue));
    }

    private boolean throwUnmarkedDices() {
        boolean diceThrown = false;
        for(Dice d : diceList){
            if(!d.isMarked()){
                d.roll();
                diceThrown = true;
            }
        }
        return diceThrown;
    }

}
