package lassesjoblom.thirty;

import java.util.ArrayList;

/**
 * Created by Lasse on 2017-06-08.
 */

public class GameLogic implements lassesjoblom.thirty.Observable {

    private int currentRound;
    private int diceThrows;
    private static final int MAX_NUMBER_OF_THROWS = 3;
    private static final int TOTAL_NUMBER_OF_DICES = 6;
    private static final int MAX_AMOUNT_OF_ROUNDS = 10;

    public static int getMaxAmountOfRounds() {
        return MAX_AMOUNT_OF_ROUNDS;
    }

    private ArrayList<Dice> diceList;
    private ArrayList<RoundScore> roundScoresList;
    private Observer o;
    private boolean dicesRolled;
    private ScoreCalculator sCalc;

    public void addObserver(Observer o){
        this.o = o;
    }

    public void notifyObserver(){
        o.update(currentRound, diceThrows, diceList);
    }

    public boolean isDicesRolled() {
        return dicesRolled;
    }

    public void setDicesRolled(boolean dicesRolled) {
        this.dicesRolled = dicesRolled;
    }

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
        diceList = new ArrayList<>(TOTAL_NUMBER_OF_DICES);
        roundScoresList = new ArrayList<>(MAX_AMOUNT_OF_ROUNDS);
        sCalc = new ScoreCalculator();
        addDices();
        updateRoundCounter();
        diceThrows = MAX_NUMBER_OF_THROWS;
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

    public int getDiceThrows() {
        return diceThrows;
    }

    public void setDiceThrows(int diceThrows) {
        this.diceThrows = diceThrows;
    }

    private void addDices() {
        for(int i = 0; i != TOTAL_NUMBER_OF_DICES; i++){
            Dice d = new Dice(i);
            diceList.add(d);
            sCalc.addDice(d);
        }
    }

    public ArrayList<Dice> getDices() {
        return diceList;
    }

    public void playThrow( ){
        if(diceThrows != 0) {
            diceThrows--;
            throwUnmarkedDices();
            notifyObserver();
        }
    }

    public void unmarkDices(){
        for(Dice d : diceList){
            d.setMarked(false);
        }
    }

    public void endRound(String scoreRule) {
        roundScoresList.add(sCalc.calculateScore(scoreRule));
        updateRoundCounter();
        diceThrows = MAX_NUMBER_OF_THROWS;
        notifyObserver();
    }

    private void updateRoundCounter() {
        currentRound = roundScoresList.size() + 1;
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
