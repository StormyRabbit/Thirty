package lassesjoblom.thirty;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Random;

/**
 * Created by Lasse on 2017-06-08.
 */

class Dice implements Parcelable {
    private int faceValue;
    private boolean isMarked;
    private int id;

    public Dice(int id){
        this.id = id;
        roll();

    }

    public Dice(Parcel in){
        this.faceValue = in.readInt();
        this.id = in.readInt();
        if(in.readInt() == 1){
            this.isMarked = true;
        }else{
            this.isMarked = false;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(faceValue);
        out.writeInt(id);
        if(isMarked){
            out.writeInt(1);
        }else{
            out.writeInt(0);
        }
    }

    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Dice> CREATOR = new Parcelable.Creator<Dice>() {
        public Dice createFromParcel(Parcel in) {
            return new Dice(in);
        }

        public Dice[] newArray(int size) {
            return new Dice[size];
        }
    };
}
