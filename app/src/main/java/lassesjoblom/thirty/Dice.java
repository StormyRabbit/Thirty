package lassesjoblom.thirty;

import java.util.Random;

/**
 * Created by Lasse on 2017-06-08.
 */

public class Dice {
    private int faceValue;
    private boolean isMarked;

    public Dice(){
        roll();
    }
    public boolean isMarked() {
        return isMarked;
    }

    public void setMarked(boolean marked) {
        isMarked = marked;
    }


    public void roll() {
        Random rng = new Random();
        faceValue = rng.nextInt(6);
    }
    public int getFaceValue() {
        return faceValue;
    }

    public void setFaceValue(int faceValue) {
        this.faceValue = faceValue;
    }
}
