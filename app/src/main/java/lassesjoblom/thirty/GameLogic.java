package lassesjoblom.thirty;

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
        addDices();
        currentRound = 1;
        rethrow = 2;
        sCalc = new ScoreCalculator(diceList);
    }

    public void setDiceList(ArrayList<Dice> diceList) {
        this.diceList = diceList;
    }

    public Dice setDiceMarker(int idx) {
        Dice d = diceList.get(idx);
        if(d.isMarked()) {
            d.setMarked(false);
        }else{
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
            diceList.add(new Dice(i));
        }
    }

    public ArrayList<Dice> getDices() {
        return diceList;
    }

    public void playThrow( ){
        throwUnmarkedDices();
        sCalc.calculateScore(5);
        updateRound();
    }

    private void throwUnmarkedDices() {
        for(Dice d : diceList){
            if(!d.isMarked()){
                d.roll();
            }
        }
    }

    private void updateRound() {
        if(rethrow != 0){
            rethrow--;
        }else{
            currentRound++;
            rethrow = 2;
        }
    }

}
