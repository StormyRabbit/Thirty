package lassesjoblom.thirty;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Lasse on 2017-06-08.
 */

public class GameLogic implements lassesjoblom.thirty.Observable, Parcelable {

    private int currentRound;
    private int diceThrows;

    public static final int MAX_NUMBER_OF_THROWS = 3;
    public static final int TOTAL_NUMBER_OF_DICES = 6;
    public static final int MAX_AMOUNT_OF_ROUNDS = 10;

    private ArrayList<Dice> diceList;
    private ArrayList<RoundScore> roundScoresList;
    private Observer gameActivityObserver;
    private boolean dicesRolled;
    private ScoreCalculator sCalc;

    public GameLogic() {
        diceList = new ArrayList<>(TOTAL_NUMBER_OF_DICES);
        roundScoresList = new ArrayList<>(MAX_AMOUNT_OF_ROUNDS);
        sCalc = new ScoreCalculator();
        createDices();

        updateRoundCounter();
        diceThrows = MAX_NUMBER_OF_THROWS;
    }

    public void addObserver(Observer o){
        this.gameActivityObserver = o;
    }

    public void notifyObserver(){
        // berättar för GameActivity att det är dags att updatera vy elementen
        gameActivityObserver.update(currentRound, diceThrows, diceList);
    }

    public boolean isDicesRolled() {
        return dicesRolled;
    }

    public void setDicesRolled(boolean dicesRolled) {
        this.dicesRolled = dicesRolled;
    }

    public ArrayList<RoundScore> getRoundScoresList() {
        return roundScoresList;
    }

    public Dice setDiceMarker(int idx) {
        // skiftar tärningen med samma id som param värdets status som markerad.
        // används för att hålla koll på vilken tärning som inte ska rullas.
        for(Dice d : diceList){
            if(d.getId() == idx){
                d.setMarked( !d.isMarked() );
                return d;
            }

        }
        return null;
    }

    public int getCurrentRound() {
        // returnerar vilket aktuell runda som spelas.
        return currentRound;
    }

    public int getDiceThrows() {
        // returnerar antal kvarvarande kast på den aktuella rundan
        return diceThrows;
    }

    private void createDices() {
        // skapar och nollställer alla tärningar. Sparar de även i denna klass tärnings lista.
        // skapar även dessa tärningar i scoreCalculator klassen.
        for(int i = 0; i != TOTAL_NUMBER_OF_DICES; i++){
            Dice d = new Dice(i);
            d.resetDice();
            diceList.add(d);
            sCalc.addDice(d);
        }
    }

    public ArrayList<Dice> getDices() {
        // returnerar listan över alla tärningar
        return diceList;
    }

    public void playThrow() {
        // spelar själva rundan, reducerar antal tillgängliga kast, kastar alla omarkerade
        // tärningar samt pushar den nya informationen till observatörerna.
        if(diceThrows != 0) { // om det finns kast kvar på rundan
            diceThrows--;
            throwUnmarkedDices();
            notifyObserver();
        }
    }

    public void unmarkDices() {
        // nollställer markerings statusen för alla tärningar.
        for(Dice d : diceList){
            d.setMarked(false);
        }
        notifyObserver();
    }

    public void resetDices() {
        for(Dice d : diceList) {
            d.resetDice();
        }
        notifyObserver();
    }

    public void endRound(String scoreRule) {
        // avslutar en runda, tar ett specifik poängregel som param.
        // Skapar ett nytt objekt av typen roundScore via Scorecalculatorn och adderar det
        // returnerade objektet till en lista med poängen för alla rundor.
        // updaterar även modellvärdena relaterat för vilken runda som spelas och antalet tillgängliga
        // kast. Slutligen så pushas även en notis om en förändring till observatörerna.
        roundScoresList.add(sCalc.calculateScore(scoreRule, diceList));
        updateRoundCounter();
        diceThrows = MAX_NUMBER_OF_THROWS;
        notifyObserver();
    }

    private void updateRoundCounter() {
        currentRound = roundScoresList.size() + 1;
    }

    private boolean throwUnmarkedDices() {
        boolean diceThrown = false;
        for(Dice d : diceList) {
            if(!d.isMarked()) { // om tärningen inte är markerad rulla tärningen
                d.roll();
                diceThrown = true;
            }
        }
        // retur värdet används sedan för att avgöra om man ska hoppa över de
        // kvarvarande kasten på en runda.
        return diceThrown;
    }

    /* Parcelable interface implementation */
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(currentRound);
        out.writeInt(diceThrows);
        out.writeTypedList(diceList);
        out.writeTypedList(roundScoresList);
    }

    public static final Parcelable.Creator<GameLogic> CREATOR
            = new Parcelable.Creator<GameLogic>() {
        public GameLogic createFromParcel(Parcel in) {
            return new GameLogic(in);
        }

        public GameLogic[] newArray(int size) {
            return new GameLogic[size];
        }
    };

    private GameLogic(Parcel in) {
        currentRound = in.readInt();
        diceThrows = in.readInt();
        in.readTypedList(diceList, Dice.CREATOR);
        in.readTypedList(roundScoresList, RoundScore.CREATOR );
    }
}
