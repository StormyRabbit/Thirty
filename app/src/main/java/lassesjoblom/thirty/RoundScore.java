package lassesjoblom.thirty;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by Lasse on 2017-06-15.
 */

public class RoundScore implements Parcelable {
    private int round;
    private int totalScore;
    private int singleDiceScore;
    private int twoDiceScore;
    private int threeDiceScore;
    private int fourDiceScore;
    private int fiveDiceScore;
    private int sixDiceScore;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RoundScore that = (RoundScore) o;

        if (round != that.round) return false;
        if (totalScore != that.totalScore) return false;
        if (singleDiceScore != that.singleDiceScore) return false;
        if (twoDiceScore != that.twoDiceScore) return false;
        if (threeDiceScore != that.threeDiceScore) return false;
        if (fourDiceScore != that.fourDiceScore) return false;
        if (fiveDiceScore != that.fiveDiceScore) return false;
        return sixDiceScore == that.sixDiceScore;

    }

    @Override
    public int hashCode() {
        int result = round;
        result = 31 * result + totalScore;
        result = 31 * result + singleDiceScore;
        result = 31 * result + twoDiceScore;
        result = 31 * result + threeDiceScore;
        result = 31 * result + fourDiceScore;
        result = 31 * result + fiveDiceScore;
        result = 31 * result + sixDiceScore;
        return result;
    }
    public RoundScore(){}

    public RoundScore(Parcel in){
        this.round = in.readInt();
        this.totalScore = in.readInt();
        this.singleDiceScore = in.readInt();
        this.twoDiceScore = in.readInt();
        this.threeDiceScore = in.readInt();
        this.fourDiceScore = in.readInt();
        this.fiveDiceScore = in.readInt();
        this.sixDiceScore = in.readInt();

    }
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(round);
        out.writeInt(totalScore);
        out.writeInt(singleDiceScore);
        out.writeInt(twoDiceScore);
        out.writeInt(threeDiceScore);
        out.writeInt(fourDiceScore);
        out.writeInt(fiveDiceScore);
        out.writeInt(sixDiceScore);

    }

    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<RoundScore> CREATOR = new Parcelable.Creator<RoundScore>() {
        public RoundScore createFromParcel(Parcel in) {
            return new RoundScore(in);
        }

        public RoundScore[] newArray(int size) {
            return new RoundScore[size];
        }
    };

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void addToTotalScore(int score) {
        totalScore += score;
    }

    public void addToSingleDiceScore(int score) {
        singleDiceScore += score;
    }

    public void addToTwoDiceScore(int score) {
        twoDiceScore += score;
    }

    public void addToThreeDiceScore(int score) {
        threeDiceScore += score;
    }

    public void addToFourDiceScore(int score) {
        fourDiceScore += score;
    }

    public void addToFiveDiceScore(int score) {
        fiveDiceScore += score;
    }

    public void addToSixDiceScore(int score) {
        sixDiceScore += score;
    }

    public int getSingleDiceScore() {
        return singleDiceScore;
    }

    public int getTwoDiceScore() {
        return twoDiceScore;
    }

    public int getThreeDiceScore() {
        return threeDiceScore;
    }

    public int getFourDiceScore() {
        return fourDiceScore;
    }

    public int getFiveDiceScore() {
        return fiveDiceScore;
    }

    public int getSixDiceScore() {
        return sixDiceScore;
    }

    public void logScore() {
        Log.d("CurrentRound", String.valueOf(round));
        Log.d("SingleDice", String.valueOf(singleDiceScore));
        Log.d("PairDice", String.valueOf(twoDiceScore));
        Log.d("TripleDice", String.valueOf(threeDiceScore));
        Log.d("FourDice", String.valueOf(fourDiceScore));
        Log.d("FiveDice", String.valueOf(fiveDiceScore));
        Log.d("SixDice", String.valueOf(sixDiceScore));
        Log.d("totalScore", String.valueOf(totalScore));
    }
}
