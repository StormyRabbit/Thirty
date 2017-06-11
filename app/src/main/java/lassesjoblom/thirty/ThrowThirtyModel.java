package lassesjoblom.thirty;

import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

/**
 * Created by Lasse on 2017-06-08.
 */

public class ThrowThirtyModel {

    private int score;
    private int singleScore;
    private int pairScore;

    private int currentRound;
    private int rethrow;

    private ArrayList<Dice> diceList;

    private ScoreCalculator sCalc;

    public int getScore() {
        return score;
    }

    public ThrowThirtyModel() {
        diceList = new ArrayList<>(6);
        addDices();
        sCalc = new ScoreCalculator(diceList);
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

}
