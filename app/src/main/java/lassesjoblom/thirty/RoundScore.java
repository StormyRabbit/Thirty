package lassesjoblom.thirty;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by Lasse on 2017-06-15.
 */

public class RoundScore implements Parcelable {
    private String scoreRule;
    private int round;
    private int totalScore;
    private int singleDiceScore;
    private int twoDiceScore;
    private int threeDiceScore;
    private int fourDiceScore;
    private int fiveDiceScore;
    private int sixDiceScore;
    private int lowScore;

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

    public RoundScore() {
        scoreRule = "";
        round = 0;
        totalScore = 0;
        singleDiceScore = 0;
        twoDiceScore = 0;
        threeDiceScore = 0;
        fourDiceScore = 0;
        fiveDiceScore = 0;
        sixDiceScore = 0;
        lowScore = 0;
    }

    public RoundScore(Parcel in){
        this.round = in.readInt();
        this.lowScore = in.readInt();
        this.totalScore = in.readInt();
        this.singleDiceScore = in.readInt();
        this.twoDiceScore = in.readInt();
        this.threeDiceScore = in.readInt();
        this.fourDiceScore = in.readInt();
        this.fiveDiceScore = in.readInt();
        this.sixDiceScore = in.readInt();
        this.scoreRule = in.readString();

    }
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(round);
        out.writeInt(lowScore);
        out.writeInt(totalScore);
        out.writeInt(singleDiceScore);
        out.writeInt(twoDiceScore);
        out.writeInt(threeDiceScore);
        out.writeInt(fourDiceScore);
        out.writeInt(fiveDiceScore);
        out.writeInt(sixDiceScore);
        out.writeString(scoreRule);

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


    public int getTotalScore() {
        return totalScore;
    }

    public void calculateTotalScore() {
        if(scoreRule.equals("Low")) {
            totalScore = lowScore;
        } else {
            totalScore = singleDiceScore + twoDiceScore + threeDiceScore + fourDiceScore + fiveDiceScore + sixDiceScore;
        }
    }

    public void addToLowScore(int score) {
        this.lowScore += score;
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


    public String getScoreRule() {
        return scoreRule;
    }

    public void setScoreRule(String scoreRule) {
        this.scoreRule = scoreRule;
    }

}
