package lassesjoblom.thirty;

import android.os.Parcel;
import android.os.Parcelable;

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
    private ArrayList<Dice> diceList;
    private int pointValue;

    public ScoreCalculator() {
        diceList = new ArrayList<>();
    }

    public void addDice(Dice d){
        diceList.add(d);
    }

    /**
     * Converts the user selected score rule to an integer pointValue used in score calculations
     * @param scoreRule The selected scoreRule sent from {@link #calculateScore(String, ArrayList<Dice>)}
     */
    private void scoreRuleToPointValue(String scoreRule) {
        // konverterar string värdet från användarens valda alternativ till integer som används
        // för att beräkna poäng.
        pointValue = 0;
        switch (scoreRule) {
            case "Low":
                pointValue = 0;
                break;
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
    public RoundScore calculateScore(String scoreRule, ArrayList<Dice> diceList) {
        prepareScoreCalculations(scoreRule, diceList); // initierar alla nödvändiga värden och objekt.

        if(pointValue == 0) { // om Low används som poängräkning
            calculateLowScore();

        }else {
            // sortering som behövs för vissa av poängräkningsalgoritmerna
            Collections.sort(this.diceList);
            calculateSingleDiceScore();
            removeTooHighRolls();

            if(diceList.size() >= 2)
                calculateTwoDiceScore();

            if(diceList.size() >= 3)
                calculateThreeDiceScore();

            if(diceList.size() >= 4)
                calculateFourDiceScore();

            if(diceList.size() >= 5)
                calculateFiveDiceScore();

            if(diceList.size() == 6)
                calculateSixDiceScore();

        }

        currentRoundScore.calculateTotalScore();
        return currentRoundScore;
    }

    private void calculateLowScore() {
        for(Dice d : diceList) {
            if(d.getFaceValue() <= 3) {
                currentRoundScore.addToLowScore(d.getFaceValue());
            }
        }
    }

    private void prepareScoreCalculations(String scoreRule, ArrayList<Dice> diceList) {
        this.diceList = new ArrayList<>(diceList);
        currentRoundScore = new RoundScore();
        currentRoundScore.setScoreRule(scoreRule);
        scoreRuleToPointValue(scoreRule);
    }

    private void removeTooHighRolls() {
        /*
        Tar bort alla singel tärningar som överstiger det poängvärde definerat av vald regel
         */
        Iterator iter = diceList.iterator();
        while(iter.hasNext()) {
            Dice d = (Dice)iter.next();
            if(d.getFaceValue() > pointValue) {
                iter.remove();
            }
        }
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
     */
    private void calculateSingleDiceScore() {
        Iterator iter = diceList.iterator();
        while(iter.hasNext()) {
            Dice d = (Dice)iter.next();
            if(d.getFaceValue() == pointValue ) {
                iter.remove();
                currentRoundScore.addToSingleDiceScore(pointValue);
            }
        }
    }

    private void calculateSixDiceScore() {
        if(sumArr(diceList) == pointValue)
            currentRoundScore.addToSixDiceScore(pointValue);
    }

    /**
     * Bruteforces all possible combinataion of the remaining dices.
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
    /**
     * Used {@link #getSubsets(ArrayList, int)} to calculate all possible comibination of the remaning
     * dices. Uses the returning List of {@link Dice} sets to check if any of the possible combination
     * equals to the defined pointValue.
     */
    private void calculateFourDiceScore() {
        calculateFourOrFiveScore(4);
    }

    private void calculateFiveDiceScore() {
        calculateFourOrFiveScore(5);
    }

    private void calculateFourOrFiveScore(int numberOfDices) {
        // generera alla möjliga kombinationer av param antal tärningar.
        ArrayList<Set<Dice>> tempDices = getSubsets(diceList, numberOfDices);

        while(!tempDices.isEmpty()) { // så länge det finns en möjlig kombination
            Set<Dice> diceSet = tempDices.remove(0);
            int sum = 0;
            for(Dice d : diceSet) {
                sum += d.getFaceValue();
            }

            // om det ovan uthämtade settet summerar till poängvärdet
            // addera summan till currentRoundScore objektet
            if(sum == pointValue) {
                if(numberOfDices == 4){
                    currentRoundScore.addToFourDiceScore(pointValue);
                }else{
                    currentRoundScore.addToFiveDiceScore(pointValue);
                }
                // eftersom det bara kan finnas ett relevant set av 4 / 5 tärningar så avslutas
                // algoritmen efter första hittade.
                diceList.removeAll(diceSet);
                break;
            }
        }
    }

    /**
     * Finds three dices that sums to the goal pointValue and removes them from the {@link Dice} list.
     */
    private void calculateThreeDiceScore() {
        for(int i = 0; i < diceList.size() - 2; i++) {
            int j = i+1;
            int k = diceList.size() - 1;

            while( k >= j) {
                Dice dicei = diceList.get(i);
                Dice dicej = diceList.get(j);
                Dice dicek = diceList.get(k);
                if (evaluateFaceValue(dicei, dicej, dicek)) {
                    // en värdig kombination hittad
                    // lägg till poängen och ta bort de tärningar som användes
                    currentRoundScore.addToThreeDiceScore(pointValue);
                    diceList.remove(dicei);
                    diceList.remove(dicej);
                    diceList.remove(dicek);
                    break;
                }else{
                    // avgör om k eller j pekaren ska flytta på sig
                    if(evaluateArrayPointers(dicei, dicej, dicek)) {
                        // om summan är högre än målet
                        k--;
                    }else {
                        // om summan är lägre än målet
                        j++;
                    }
                }
            }
        }
    }

    /**
     * evaulates if the three dice objects are greater then the defined pointValue
     * @param dOne arbitrary first dice
     * @param dTwo arbitrary second dice
     * @param dThree arbitrary third dice
     * @return true if the sum is bigger else false.
     */
    private boolean evaluateArrayPointers(Dice dOne, Dice dTwo, Dice dThree) {
        return dOne.getFaceValue() + dTwo.getFaceValue() + dThree.getFaceValue() > pointValue;
    }

    /**
     * evaulates if the tree dice objects facevalue is equal to the defined pointValue
     * @param dOne arbitrary first dice
     * @param dTwo  arbitrary second dice
     * @param dThree arbitrary third dice
     * @return True if the sum is equal false otherwise.
     */
    private boolean evaluateFaceValue(Dice dOne, Dice dTwo, Dice dThree) {
        return dOne.getFaceValue() + dTwo.getFaceValue() + dThree.getFaceValue() == pointValue;
    }

    /**
     * Finds 2 dice pairs that sums to the defined pointValue. Requires that the dice List is sorted
     * by faceValue.
     */
    private void calculateTwoDiceScore() {
        int i = 0;
        int j = diceList.size() - 1;
        while(i < j) {
            Dice d1 = diceList.get(i);
            Dice d2 = diceList.get(j);
            int x = d1.getFaceValue();
            int y = d2.getFaceValue();
            if( x + y == pointValue) {
                // kombination hittad, ta bort berörda tärningar och addera poäng till roundscore
                // objektet.
                diceList.remove(d1);
                diceList.remove(d2);
                i = 0;
                j = diceList.size() - 1;
                currentRoundScore.addToTwoDiceScore(pointValue);
            }
            if(x + y >= pointValue)
                j--; // om summan är för hög gå ner med den 'höga' pekaren

            if(x + y <= pointValue)
                i++; // om summan är för låg gå upp med den 'låga' pekaren
        }
    }
}
