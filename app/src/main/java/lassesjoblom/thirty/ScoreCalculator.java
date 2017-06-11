package lassesjoblom.thirty;

import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

/**
 * Created by lasse on 6/11/17.
 */

class ScoreCalculator {
    private int score;

    // fält för att underlätta felsökning:
    private int singleScore;
    private int pairScore;
    private int trippleScore;
    private int quadScore;
    private int pentaScore;

    private ArrayList<Dice> diceList;

    public ScoreCalculator(ArrayList<Dice> diceList) {
        this.diceList = diceList;
    }

    public void setScore(int scoreRule) {
        if(scoreRule == 0){
            calculateLowScore();
        }else{
            calculateScore(scoreRule);
        }
    }

    private void calculateScore(int pointValue){
        score = 0;
        singleScore = 0;
        pairScore = 0;
        ArrayList<Dice> tempDice = checkSingleDices(pointValue);
        tempDice = recursiveStackCrap(tempDice, new ArrayList<Dice>(),pointValue);
        //tempDice = checkForThreeOfAKind(tempDice, pointValue);
        logScore();
    }

    private ArrayList<Dice> recursiveStackCrap(
            ArrayList<Dice> dices, ArrayList<Dice> retArr, int pointValue) {

        ArrayList<Dice> recursiveArr = new ArrayList<>();
        Stack<Dice> diceStack = new Stack<>();
        diceStack.addAll(dices);
        Dice d = diceStack.pop();

        while( !diceStack.empty() ){
            Dice otherDice = diceStack.pop();
            if( d.getFaceValue() + otherDice.getFaceValue() == pointValue ) {
                pairScore += pointValue;
                score += pointValue;
            }else {
                recursiveArr.add(otherDice);
                retArr.add(otherDice);
            }
        }

        if(!recursiveArr.isEmpty()){
            recursiveStackCrap(recursiveArr, retArr, pointValue);
        }

        return retArr;
    }

    private ArrayList<Dice> checkSingleDices(int pointValue){
        ArrayList<Dice> dices = new ArrayList<>(diceList);
        if(pointValue <= 6){
            Iterator iter = dices.iterator();
            while(iter.hasNext()){
                Dice d = (Dice)iter.next();
                if(d.getFaceValue() == pointValue){
                    singleScore += pointValue;
                    iter.remove();
                }
            }
        }
        score += singleScore;

        return dices;
    }

    private void updateScore(int points, int numberOfDices){

        switch(numberOfDices){
            case 1:
                break;
            case 2:

                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
        }

    }

    private void logScore(){
        Log.d("SingleDice:", String.valueOf(singleScore));
        Log.d("PairDice:", String.valueOf(pairScore));
        Log.d("TrippleDice:", String.valueOf(trippleScore));
        Log.d("FourDice:", String.valueOf(quadScore));
        Log.d("FiveDice:", String.valueOf(pentaScore));
        Log.d("totalScore:", String.valueOf(score));
    }

    private void calculateLowScore(){
        for(Dice d : diceList){
            if(d.getFaceValue() <= 3){
                score += d.getFaceValue();
            }

        }
    }
}
