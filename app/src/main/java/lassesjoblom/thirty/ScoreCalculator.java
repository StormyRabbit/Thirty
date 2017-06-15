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
    private int hexaScore;

    private ArrayList<Dice> diceList;

    public ScoreCalculator(ArrayList<Dice> diceList) {
        this.diceList = diceList;
    }

    public int getScore(){
        return score;
    }
    public void setScore(int scoreRule) {
        calculateScore(scoreRule);
    }

    public void calculateScore(int pointValue){
        score = 0;
        singleScore = 0;
        pairScore = 0;
        ArrayList<Dice> tempDice = checkSingleDices(pointValue);
        //tempDice = recursiveStackCrap(tempDice, new ArrayList<Dice>(),pointValue);
        //tempDice = checkForThreeOfAKind(tempDice, pointValue);
        for(int i = 0; i < 6; i++){
            testingMethod(tempDice, new ArrayList<Dice>(), pointValue, i);
        }
        logScore();
    }

    private ArrayList<Dice> testingMethod(
            ArrayList<Dice> dices, ArrayList<Dice> retArr, int pointValue, int numberOfDices) {

        ArrayList<Dice> recursiveArr = new ArrayList<>();
        Stack<Dice> diceStack = new Stack<>();
        diceStack.addAll(dices);
        ArrayList<Dice> diceSumArr = new ArrayList<>();

        while( !diceStack.empty() ) {
            Dice d = diceStack.pop();
            diceSumArr.add(d);

            if(diceSumArr.size() == numberOfDices) {
                if(sumArr(diceSumArr) == pointValue) {
                    updateScores(pointValue, numberOfDices);
                    diceSumArr.clear(); // onödig, hjälper dock för min readability
                }else {
                    recursiveArr.add(d);
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
        if( !recursiveArr.isEmpty() ){
            testingMethod(recursiveArr, retArr, pointValue, numberOfDices);
        }
        return retArr;
    }

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
    private int sumArr(ArrayList<Dice> diceArr){
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
