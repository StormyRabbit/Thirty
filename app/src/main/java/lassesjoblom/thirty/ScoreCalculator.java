package lassesjoblom.thirty;

import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

/**
 * Created by lasse on 6/11/17.
 */

class ScoreCalculator {

    // fält för att underlätta felsökning:

    private RoundScore currentRoundScore;
    private ArrayList<Dice> diceList;
    private int pointValue = 0;
    private int numberOfDices = 0;


    public ScoreCalculator(ArrayList<Dice> diceList) {
        this.diceList = diceList;
    }

    public RoundScore getCurrentRoundScore(){
        return currentRoundScore;
    }
    public void calculateScore(){
        currentRoundScore = new RoundScore();

        testingMethod(new ArrayList<Dice>());

        currentRoundScore.logScore();
    }

    public void setPointValue(int pointValue) {
        this.pointValue = pointValue;
    }

    public void setNumberOfDices( int numberOfDices ) {
        this.numberOfDices = numberOfDices;
    }

    private void testingMethod(ArrayList<Dice> retArr) {

        Stack<Dice> diceStack = new Stack<>();
        diceStack.addAll(diceList);
        ArrayList<Dice> diceSumArr = new ArrayList<>();

        while( !diceStack.empty() ) {
            Dice d = diceStack.pop();
            diceSumArr.add(d);

            if(diceSumArr.size() == numberOfDices) {
                if(sumArr(diceSumArr) == pointValue) {
                   // updateScores(pointValue, numberOfDices);
                    diceSumArr.clear(); // onödig, hjälper dock för min readability
                }else {
                    retArr.add(d);
                    for(int i = 1; i < diceSumArr.size(); i++) {
                        // ta bort alla förrutom "orginal" tärningen
                        diceSumArr.remove(i);
                    }
                }
            }else {
                diceSumArr.add(d);
            }
        }
        if( !retArr.isEmpty() ){
            testingMethod(retArr);
        }
    }
/*
    private void updateScores(int score, int numberOfDices){
        switch( numberOfDices ){
            case 1:
                singleScore += score;
                break;
            case 2:
                pairScore += score;
                break;
            case 3:
                trippleScore += score;
                break;
            case 4:
                quadScore += score;
                break;
            case 5:
                pentaScore += score;
                break;
            case 6:
                hexaScore += score;
                break;
        }
        this.score += score;
    }
*/
    private int sumArr(ArrayList<Dice> diceArr) {
        int retVal = 0;
        for(Dice d : diceArr){
            retVal += d.getFaceValue();
        }
        return retVal;
    }

    private ArrayList<Dice> recursiveStackCrap(
            ArrayList<Dice> dices, ArrayList<Dice> retArr, int pointValue) {

        ArrayList<Dice> recursiveArr = new ArrayList<>();
        Stack<Dice> diceStack = new Stack<>();
        diceStack.addAll(dices);
        Dice d = diceStack.pop();

        while( !diceStack.empty() ) {
            Dice otherDice = diceStack.pop();
            if( d.getFaceValue() + otherDice.getFaceValue() == pointValue ) {
                currentRoundScore.setTwoDiceScore(pointValue);
                currentRoundScore.setTotalScore(pointValue);
            }else {
                recursiveArr.add(otherDice);
                retArr.add(otherDice);
            }
        }

        if(!recursiveArr.isEmpty()) {
            recursiveStackCrap(recursiveArr, retArr, pointValue);
        }

        return retArr;
    }


}
