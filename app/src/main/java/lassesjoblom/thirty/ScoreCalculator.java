package lassesjoblom.thirty;

import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by lasse on 6/11/17.
 */

class ScoreCalculator {

    private RoundScore currentRoundScore;
    private ArrayList<Dice> diceList = new ArrayList<>();
    private int pointValue;

    public ScoreCalculator() {
        diceList = new ArrayList<>();
    }

    public void addDice(Dice d){
        diceList.add(d);
    }

    public RoundScore getCurrentRoundScore(){
        return currentRoundScore;
    }

    private void scoreRuleToPointValue(String scoreRule) {
        int pointValue = 0;
        switch (scoreRule) {
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
    }
    public RoundScore calculateScore(String scoreRule) {
        currentRoundScore = new RoundScore();
        scoreRuleToPointValue(scoreRule);
        if(pointValue == 0) {
            calculateLowScore();

        }else{

            Collections.sort(diceList);
            ArrayList<Dice> tempDiceArr = calculateSingleDiceScore();
            tempDiceArr = removeTooHighRolls(tempDiceArr);

            if(tempDiceArr.size() >= 2)
                tempDiceArr = calculateTwoDiceScore(tempDiceArr);

            if(tempDiceArr.size() >= 3)
                tempDiceArr = calculateThreeDiceScore(tempDiceArr);

            if(tempDiceArr.size() >= 4)
                tempDiceArr = calculateFourDiceScore(tempDiceArr);

            if(tempDiceArr.size() >= 5)
                tempDiceArr = calculateFiveDiceScore(tempDiceArr);

            if(tempDiceArr.size() == 6)
                calculateSixDiceScore(tempDiceArr);

        }

        currentRoundScore.calculateTotalScore();
        currentRoundScore.logScore();
        return currentRoundScore;
    }

    private void calculateLowScore() {
        for(Dice d : diceList){
            if(d.getFaceValue() <= 3) {
                currentRoundScore.addToLowScore(pointValue);
            }
        }
    }

    private ArrayList<Dice> removeTooHighRolls(ArrayList<Dice> dices) {
        Iterator iter = dices.iterator();
        while(iter.hasNext()){
            Dice d = (Dice)iter.next();
            if(d.getFaceValue() > pointValue){
                iter.remove();
            }
        }
        return dices;
    }

    private int sumArr(ArrayList<Dice> diceArr) {
        int retVal = 0;
        for(Dice d : diceArr){
            retVal += d.getFaceValue();
        }
        return retVal;
    }

    private ArrayList<Dice> calculateSingleDiceScore() {
        ArrayList<Dice> diceArray = new ArrayList<>(diceList);
        Iterator iter = diceArray.iterator();
        while(iter.hasNext()){
            Dice d = (Dice)iter.next();
            if(d.getFaceValue() == pointValue ){
                iter.remove();
                currentRoundScore.addToSingleDiceScore(pointValue);
            }
        }
        return diceArray;
    }

    private void calculateSixDiceScore(ArrayList<Dice> dices) {
        int result = sumArr(dices);
        if(result == pointValue){
            currentRoundScore.addToSixDiceScore(pointValue);
        }
    }

    private ArrayList<Dice> calculateFourDiceScore(ArrayList<Dice> dices) {
        ArrayList<Set<Dice>> tempDices = getSubsets(dices, 4);

        while(!tempDices.isEmpty()) {
            Set<Dice> diceSet = tempDices.remove(0);
            int sum = 0;
            for(Dice d : diceSet) {
                sum += d.getFaceValue();
            }

            if(sum == pointValue) {
                currentRoundScore.addToFourDiceScore(pointValue);
                dices.removeAll(diceSet);
                break;
            }
        }
        return dices;
    }

    /**
     * from https://stackoverflow.com/a/12548381
     * @param superSet
     * @param numberOfDices
     * @param idx
     * @param current
     * @param solution
     */
    private void getSubsets(ArrayList<Dice> superSet,
                                   int numberOfDices, int idx,
                                   Set<Dice> current,
                                   ArrayList<Set<Dice>> solution) {
        //successful stop clause
        if (current.size() == numberOfDices) {
            solution.add(new HashSet<>(current));
            return;
        }
        //unseccessful stop clause
        if (idx == superSet.size()) return;
        Dice d = superSet.get(idx);
        current.add(d);
        //"guess" x is in the subset
        getSubsets(superSet, numberOfDices, idx+1, current, solution);
        current.remove(d);
        //"guess" x is not in the subset
        getSubsets(superSet, numberOfDices, idx+1, current, solution);
    }

    private ArrayList<Set<Dice>> getSubsets(ArrayList<Dice> superSet, int k) {
        ArrayList<Set<Dice>> res = new ArrayList<>();
        getSubsets(superSet, k, 0, new HashSet<Dice>(), res);
        return res;
    }

    private ArrayList<Dice> calculateFiveDiceScore(ArrayList<Dice> dices) {
        ArrayList<Set<Dice>> tempDices = getSubsets(dices, 5);

        while(!tempDices.isEmpty()) {
            Set<Dice> diceSet = tempDices.remove(0);
            int sum = 0;
            for(Dice d : diceSet) {
                sum += d.getFaceValue();
            }

            if(sum == pointValue) {
                currentRoundScore.addToFiveDiceScore(pointValue);
                dices.removeAll(diceSet);
                break;
            }
        }

        return dices;
    }

    private ArrayList<Dice> calculateThreeDiceScore(ArrayList<Dice> dices) {
        for(int i = 0; i < dices.size() - 2; i++) {
            int j = i+1;
            int k = dices.size() - 1;

            while( k >= j) {
                Dice dicei = dices.get(i);
                Dice dicej = dices.get(j);
                Dice dicek = dices.get(k);
                if (evaluateFaceValue(dicei, dicej, dicek)) {
                    currentRoundScore.addToThreeDiceScore(pointValue);
                    dices.remove(dicei);
                    dices.remove(dicej);
                    dices.remove(dicek);
                    break;
                }else{
                    if(evaluateArrayPointers(dicei, dicej, dicek)) {
                        k--;
                    }else{
                        j++;
                    }
                }
            }
        }
        return dices;
    }

    private boolean evaluateArrayPointers(Dice dOne, Dice dTwo, Dice dThree) {
        return dOne.getFaceValue() + dTwo.getFaceValue() + dThree.getFaceValue() > pointValue;
    }
    private boolean evaluateFaceValue(Dice dOne, Dice dTwo, Dice dThree) {
        return dOne.getFaceValue() + dTwo.getFaceValue() + dThree.getFaceValue() == pointValue;
    }

    private ArrayList<Dice> calculateTwoDiceScore(ArrayList<Dice> dices) {
        int i = 0;
        int j = dices.size() - 1;
        while(i < j) {
            Dice d1 = dices.get(i);
            Dice d2 = dices.get(j);
            int x = d1.getFaceValue();
            int y = d2.getFaceValue();
            if( x + y == pointValue) {
                dices.remove(d1);
                dices.remove(d2);
                i = 0;
                j = dices.size() - 1;
                currentRoundScore.addToTwoDiceScore(pointValue);
            }
            if(x + y >= pointValue){
                j--;
            }

            if(x + y <= pointValue){
                i++;
            }

        }
        return dices;
    }
}
