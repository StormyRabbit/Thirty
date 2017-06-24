package lassesjoblom.thirty;

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

    /**
     * Converts the user selected scorerule to an integer pointValue used in score calculations
     * @param scoreRule The selected scoreRule sent from {@link #calculateScore(String)}
     */
    private void scoreRuleToPointValue(String scoreRule) {
        pointValue = 0;
        switch (scoreRule) {
            case "Four":
                pointValue = 4;
                break;
            case "Five":
                pointValue = 5;
                break;
            case "Six":
                pointValue = 6;
                break;
            case "Seven":
                pointValue = 7;
                break;
            case "Eight":
                pointValue = 8;
                break;
            case "Nine":
                pointValue = 9;
                break;
            case "Ten":
                pointValue = 10;
                break;
            case "Eleven":
                pointValue = 11;
                break;
            case "Twelve":
                pointValue = 12;
                break;
        }
    }

    /**
     *
     * @param scoreRule The String value of the user selected value of the views spinner
     * @return An {@link RoundScore} instance containing the result of the score calculation
     */
    public RoundScore calculateScore(String scoreRule) {
        currentRoundScore = new RoundScore();
        currentRoundScore.setScoreRule(scoreRule);
        scoreRuleToPointValue(scoreRule);
        if(pointValue == 0) {
            calculateLowScore();

        }else {

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
        for(Dice d : diceList) {
            if(d.getFaceValue() <= 3) {
                currentRoundScore.addToLowScore(d.getFaceValue());
            }
        }
    }

    private ArrayList<Dice> removeTooHighRolls(ArrayList<Dice> dices) {
        Iterator iter = dices.iterator();
        while(iter.hasNext()) {
            Dice d = (Dice)iter.next();
            if(d.getFaceValue() > pointValue) {
                iter.remove();
            }
        }
        return dices;
    }

    /**
     * @param diceArr array containing {@link Dice} objects
     * @return int of the sum of all the params facevalue.
     */
    private int sumArr(ArrayList<Dice> diceArr) {
        int retVal = 0;
        for(Dice d : diceArr) {
            retVal += d.getFaceValue();
        }
        return retVal;
    }

    /**
     * Iterates over the dice list checking each individual dice facevalue and compares it to the
     * applied score rule, removes valid dices and updates the {@link #currentRoundScore} object.
     * @return The updated arraylist.
     */
    private ArrayList<Dice> calculateSingleDiceScore() {
        ArrayList<Dice> diceArray = new ArrayList<>(diceList);
        Iterator iter = diceArray.iterator();
        while(iter.hasNext()) {
            Dice d = (Dice)iter.next();
            if(d.getFaceValue() == pointValue ) {
                iter.remove();
                currentRoundScore.addToSingleDiceScore(pointValue);
            }
        }
        return diceArray;
    }

    private void calculateSixDiceScore(ArrayList<Dice> dices) {
        int result = sumArr(dices);
        if(result == pointValue) {
            currentRoundScore.addToSixDiceScore(pointValue);
        }
    }

    /**
     * Used {@link #getSubsets(ArrayList, int)} to calculate all possible comibination of the remaning
     * dices. Uses the returning List of {@link Dice} sets to check if any of the possible combination
     * equals to the defined pointValue.
     * @param dices arrayList containing Dice objects
     * @return updated ArrayList containing the remaining dices.
     */
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
     * Bruteforces all possible combinataion of the
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

    /**
     * Finds three dices that sums to the goal pointValue and removes them from the incoming
     * {@link Dice} list.
     * @param dices a sorted list of {@link Dice} objects.
     * @return the incoming arrayList with all point giving combinations of three dices removed.
     */
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
                    }else {
                        j++;
                    }
                }
            }
        }
        return dices;
    }

    /**
     * evaulates if the three dice objects are greater then the defined pointValue
     * @param dOne
     * @param dTwo
     * @param dThree
     * @return true if the sum is bigger else false.
     */
    private boolean evaluateArrayPointers(Dice dOne, Dice dTwo, Dice dThree) {
        return dOne.getFaceValue() + dTwo.getFaceValue() + dThree.getFaceValue() > pointValue;
    }

    /**
     * evaulates if the tree dice objects facevalue is equal to the defined pointValue
     * @param dOne
     * @param dTwo
     * @param dThree
     * @return True if the sum is equal false otherwise.
     */
    private boolean evaluateFaceValue(Dice dOne, Dice dTwo, Dice dThree) {
        return dOne.getFaceValue() + dTwo.getFaceValue() + dThree.getFaceValue() == pointValue;
    }

    /**
     * Finds 2 dice pairs that sums to the defined pointValue. Requires that the dice List is sorted
     * by faceValue.
     * @param dices ArrayList containing {@link Dice} objects. Must be sorted!
     * @return the incoming dice list with all available pairs removed.
     */
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
            if(x + y >= pointValue)
                j--;

            if(x + y <= pointValue)
                i++;
        }
        return dices;
    }
}
