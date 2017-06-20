package lassesjoblom.thirty;

import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Lasse on 2017-06-08.
 */

public class GameLogic {

    private int currentRound;
    private int rethrow;

    private ArrayList<Dice> diceList;

    private ArrayList<RoundScore> roundScoresList;

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
        throwUnmarkedDices();
        if(updateRound())
            roundScoresList.add(sCalc.calculateScore(11));
    }

    private void throwUnmarkedDices() {
        for(Dice d : diceList){
            if(!d.isMarked()){
                d.roll();
            }
        }
    }

    private boolean updateRound() {
        if(rethrow != 0){
            rethrow--;
            return false;
        }else{
            currentRound++;
            rethrow = 2;
            return true;
        }
    }

}
