package lassesjoblom.thirty;

import java.util.ArrayList;

/**
 * Created by Lasse on 2017-06-08.
 */

public class ThrowThirtyModel {
    private int score = 0;
    private int currentRound = 0;
    private int rethrow = 0;
    private ArrayList<Dice> diceList = new ArrayList<>(6);

    public int getScore() {
        return score;
    }

    public ThrowThirtyModel() {
        addDices();
    }
    private void addDices() {
        for(int i = 0; i != 6; i++){
            diceList.add(new Dice());
        }
    }
    public int getDiceValue(int idx){
        return diceList.get(idx).getFaceValue();
    }
    public void throwDices(){

    }
    public void rollAllDices() {
        for(Dice d : diceList){
            d.roll();
        }
        currentRound++;
    }

    public int calculateScore() {
        int sum = 0;
        for(Dice d : diceList){
            sum += d.getFaceValue();
        }
        return sum;
    }

}
