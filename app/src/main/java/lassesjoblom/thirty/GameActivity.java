package lassesjoblom.thirty;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import static lassesjoblom.thirty.R.id.spinner;

public class GameActivity extends AppCompatActivity implements lassesjoblom.thirty.Observer {

    private GameLogic gl;
    private ImageView[] dicesView = null;


    private ArrayList<String> ddItems;
    private Spinner dropdown;
    private ArrayAdapter<String> ddAdapter;

    // textViews
    private TextView currentRoundView = null;
    private TextView numberOfRethrowsView = null;

    // buttons
    private Button throwButton = null;
    private Button nextRoundButton = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupInterfaceViews();
        if(savedInstanceState != null) {
            restoreSavedState(savedInstanceState);
        }else {
            gl = new GameLogic();
            setButtonState();
            addDropDownItems();
            initiateDDAdapter();
        }
        gl.addObserver(this);
        updateBoard(gl.getDices());
    }

    /**
     * Part of observerpattern called when the model layer is changed. Updates the view.
     * @param currentRound int value of the current game round.
     * @param rethrow int value of the current amount of throws left.
     * @param diceList List containing references to the {@link Dice} objects in play.
     */
    public void update(int currentRound, int rethrow, ArrayList<Dice> diceList) {
        if( currentRound == gl.MAX_AMOUNT_OF_ROUNDS + 1 ) {
            if( gl.getRoundScoresList() != null)
                endGame();

        }else {
            updateBoard(diceList);
            updateInfoRow(currentRound, rethrow);

        }
    }

    /**
     * Starts the process of ending the game, creates the {@link ScoreboardActivity} intent starts
     * the next activity.
     */
    private void endGame() {
        Intent endGameIntent = new Intent(GameActivity.this, ScoreboardActivity.class);
        endGameIntent.putParcelableArrayListExtra("scoreList", gl.getRoundScoresList());
        startActivity(endGameIntent);
    }

    private void setButtonState() {
        nextRoundButton.setEnabled(false);
    }

    private void restoreSavedState(Bundle savedInstanceState) {
        gl = savedInstanceState.getParcelable("gameLogic");
        Log.i("restored diceList", gl.getDices().toString() );
        restoreScoreRuleList(savedInstanceState);
        restoreInfoRow();
        restoreButtonState(savedInstanceState);
    }

    private void restoreButtonState(Bundle savedInstanceState) {
        nextRoundButton.setEnabled(savedInstanceState.getBoolean("nextRoundButtonState"));
        throwButton.setEnabled(savedInstanceState.getBoolean("throwButtonState"));
    }

    private void restoreInfoRow() {
        updateInfoRow(gl.getCurrentRound(), gl.getDiceThrows());
    }

    private void restoreScoreRuleList(Bundle savedInstanceState) {
        ddItems = savedInstanceState.getStringArrayList("scoreRules");
        initiateDDAdapter();
    }

    private void setupInterfaceViews() {
        initiateButtons();
        initiateDices();
        initiateDropdown();
        initiateInfoRow();

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Log.i("save diceList", gl.getDices().toString() );
        savedInstanceState.putParcelable("gameLogic", gl);
        savedInstanceState.putStringArrayList("scoreRules", ddItems);
        savedInstanceState.putBoolean("throwButtonState", throwButton.isEnabled());
        savedInstanceState.putBoolean("nextRoundButtonState", nextRoundButton.isEnabled());
        super.onSaveInstanceState(savedInstanceState);
    }

    private void initiateButtons() {
        throwButton = (Button)findViewById(R.id.throwButton);
        nextRoundButton = (Button)findViewById(R.id.nextRoundButton);
    }

    private void initiateInfoRow() {
        currentRoundView = (TextView)findViewById(R.id.currentRoundVariable);
        numberOfRethrowsView = (TextView)findViewById(R.id.numberOfRethrowsVariable);
    }

    private void updateInfoRow(int currentRound, int rethrow) {
        currentRoundView.setText(String.valueOf(currentRound));
        numberOfRethrowsView.setText(String.valueOf(rethrow));
    }

    private void initiateDices() {
        dicesView = new ImageView[] {
                (ImageView) findViewById(R.id.dice_one),
                (ImageView)findViewById(R.id.dice_two),
                (ImageView)findViewById(R.id.dice_three),
                (ImageView)findViewById(R.id.dice_four),
                (ImageView)findViewById(R.id.dice_five),
                (ImageView)findViewById(R.id.dice_six)
        };
    }

    /**
     * onClick method for the ImageView representing the dicesView. On click causes change in the
     * {@link Dice#setMarked(boolean)} value.
     * @param v the view associated with the activity
     */
    public void diceOnClick(View v) {
        if(!gl.isDicesRolled())
            return;
        Dice d = null;
        switch( v.getId() ) {
            case R.id.dice_one:
                d = gl.setDiceMarker(0);
                break;
            case R.id.dice_two:
                d = gl.setDiceMarker(1);
                break;
            case R.id.dice_three:
                d = gl.setDiceMarker(2);
                break;
            case R.id.dice_four:
                d = gl.setDiceMarker(3);
                break;
            case R.id.dice_five:
                d = gl.setDiceMarker(4);
                break;
            case R.id.dice_six:
                d = gl.setDiceMarker(5);
                break;
        }
        updateDiceView(d.getId(), d.getFaceValue(), d.isMarked());
    }

    private void initiateDropdown() {
        dropdown = (Spinner)findViewById(spinner);
        ddItems = new ArrayList<>(10);
    }
    private void initiateDDAdapter() {
        ddAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, ddItems);
        dropdown.setAdapter(ddAdapter);
    }
    private void addDropDownItems(){
        ddItems.add(getString(R.string.dropdown_text_low));
        ddItems.add(getString(R.string.dropdown_text_4));
        ddItems.add(getString(R.string.dropdown_text_5));
        ddItems.add(getString(R.string.dropdown_text_6));
        ddItems.add(getString(R.string.dropdown_text_7));
        ddItems.add(getString(R.string.dropdown_text_8));
        ddItems.add(getString(R.string.dropdown_text_9));
        ddItems.add(getString(R.string.dropdown_text_10));
        ddItems.add(getString(R.string.dropdown_text_11));
        ddItems.add(getString(R.string.dropdown_text_12));
    }

    /**
     * Event method for the next round button, responsible for poping the selected scorerule
     * and ending the round starting the score calculation method chain inside of the {@link GameLogic}
     * as well as restoring the game to a new round state.
     * @param v the view associated with the activity
     */
    public void nextRoundButtonEvent(View v) {
        String s = popScoreRule();
        gl.endRound(s);
        gl.unmarkDices();
        gl.setDicesRolled(false);
        nextRoundButton.setEnabled(false);
        throwButton.setEnabled(true);
    }

    /**
     * Event method for the the throw button.
     * @param v view associated with the activity
     */
    public void throwButtonEvent(View v) {
        if(gl.getDiceThrows() > 0){
            gl.playThrow();
            gl.setDicesRolled(true);
        }
        if(gl.getDiceThrows() == 0) {
            throwButton.setEnabled(false);
            nextRoundButton.setEnabled(true);
        }
    }

    private String popScoreRule() {
        Object ddObj = dropdown.getSelectedItem();
        String SelectedScoreRule = (String)ddObj;
        removeScoreRuleFromDropDown(SelectedScoreRule);
        return SelectedScoreRule;
    }

    private void removeScoreRuleFromDropDown(String SelectedScoreRule){
        ddAdapter.remove(SelectedScoreRule);
        ddAdapter.notifyDataSetChanged();
    }

    private void updateBoard(ArrayList<Dice> diceList) {
        for(Dice d : diceList) {
            updateDiceView(d.getId(), d.getFaceValue(), d.isMarked());
        }
    }

    /**
     * Responsible for updating the {@link ImageView} array containing the graphical representations
     * of the dicesView. Uses {@link #getDrawableID} to determine what drawable resource to use.
     * @param diceID value of the specific dice.
     * @param faceValue the face value of targeted dice.
     * @param isMarked boolean value determining if the dice should be grey (marked) or white (unmarked)
     */
    private void updateDiceView(int diceID, int faceValue, boolean isMarked) {
        dicesView[diceID].setImageResource(getDrawableID(faceValue, isMarked));
    }

    private int getDrawableID(int faceValue, boolean isMarked) {
        int retValue;
        switch( faceValue ) {
            case 0:
                retValue = R.drawable.unrolled;
                break;
            case 1:
                retValue = isMarked ? R.drawable.grey1 : R.drawable.white1;
                break;
            case 2:
                retValue = isMarked ? R.drawable.grey2 : R.drawable.white2;
                break;
            case 3:
                retValue = isMarked ? R.drawable.grey3 : R.drawable.white3;
                break;
            case 4:
                retValue = isMarked ? R.drawable.grey4 : R.drawable.white4;
                break;
            case 5:
                retValue = isMarked ? R.drawable.grey5 : R.drawable.white5;
                break;
            default:
                retValue = isMarked ? R.drawable.grey6 : R.drawable.white6;
                break;
        }
        return retValue;
    }

}
