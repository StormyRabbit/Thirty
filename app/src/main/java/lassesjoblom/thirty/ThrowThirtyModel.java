package lassesjoblom.thirty;

import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

/**
 * Created by Lasse on 2017-06-08.
 */

public class ThrowThirtyModel {
    private int score = 0;
    private int singleScore = 0;
    private int pairScore = 0;
    private int currentRound = 0;
    private int rethrow = 0;
    private ArrayList<Dice> diceList = new ArrayList<>(6);

    public int getScore() {
        return score;
    }

    public ThrowThirtyModel() {
        addDices();
    }

    public Dice setDiceMarker(int idx){
        Dice d = diceList.get(idx);
        if(d.isMarked()){
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

    public ArrayList<Dice> getDices(){
        return diceList;
    }

    public void throwUnmarkedDices(){
        for(Dice d : diceList){
            if(!d.isMarked()){
                d.roll();
            }
        }
    }

    public void updateRound(){
        if(rethrow != 2){
            rethrow++;
        }else{
            currentRound++;
        }
    }

    public void setScore(int scoreRule) {
        switch( scoreRule ){
            case 0:
                calculateLowScore();
                break;
            case 4:
                calculateScore(4);
                break;
            case 5:
                calculateScore(5);
                break;
            case 6:
                calculateScore(6);
                break;
            case 7:
                calculateScore(7);
                break;
            case 8:
                calculateScore(8);
                break;
            case 9:
                calculateScore(9);
                break;
            case 10:
                calculateScore(10);
                break;
            case 11:
                calculateScore(11);
                break;
            case 12:
                calculateScore(12);
                break;
        }
    }

    private void calculateScore(int pointValue){
        score = 0;
        singleScore = 0;
        pairScore = 0;
        ArrayList<Dice> tempDice = checkSingleDices(pointValue);
        tempDice = recursiveStackCrap(tempDice, new ArrayList<Dice>(),pointValue);
        Log.d("checkSingleDice:", String.valueOf(singleScore));
        Log.d("checkPairDice:", String.valueOf(pairScore));
        Log.d("totalScore:", String.valueOf(score));
        //tempDice = checkForThreeOfAKind(tempDice, pointValue);
    }

    private ArrayList<Dice> checkForThreeOfAKind(
            ArrayList<Dice> dices, ArrayList<Dice> retArr,int pointValue){
        Iterator threeOfAKindIterator = dices.iterator();
        ArrayList<Dice> recursiveArr = new ArrayList<>();
        ArrayList<Dice> sumArr = new ArrayList<>();
        Stack<Dice> diceStack = new Stack<>();
        diceStack.addAll(dices);
        Dice d = diceStack.pop();
        int sum = 0;
        while( !diceStack.empty() ){
            if( (sumArr.size() == 3) && isSumEnough(sumArr, pointValue)){
                score += pointValue;
            }else{
                Dice otherDice = diceStack.pop();
                sumArr.add(otherDice);
                recursiveArr.add(otherDice);
                retArr.add(otherDice);
            }
        }
        return retArr;
    }
    private boolean isSumEnough(ArrayList<Dice> dices, int targetValue){
        int compareSum = 0;
        for(Dice d : dices){
            compareSum += d.getFaceValue();
        }
        return compareSum == targetValue;
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
    private void calculateLowScore(){
        for(Dice d : diceList){
            if(d.getFaceValue() <= 3){
                score += d.getFaceValue();
            }
            Log.d("calculateLowScore:", String.valueOf(score));
        }
    }
}
