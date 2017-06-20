package lassesjoblom.thirty;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static lassesjoblom.thirty.R.id.spinner;

public class GameActivity extends AppCompatActivity implements lassesjoblom.thirty.Observer {

    private GameLogic gl;
    private ImageView[] dices = null;

    private ArrayList<String> ddItems;
    private Spinner dropdown;
    private ArrayAdapter<String> ddAdapter;

    private TextView currentRoundView = null;
    private TextView numberOfRethrowsView = null;

    public void update(int currentRound, int rethrow, ArrayList<Dice> diceList) {
        if( currentRound == gl.getMaxAmountOfRounds() + 1 ) {
            endGame(gl.getRoundScoresList());
        }else {
            updateInfoRow(currentRound, rethrow);
            updateBoard(diceList);
        }

    }

    private void endGame(ArrayList<RoundScore> score) {
        Intent endGameIntent = new Intent(GameActivity.this, ScoreboardActivity.class);
        endGameIntent.putParcelableArrayListExtra("scoreList", score);
        startActivity(endGameIntent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gl = new GameLogic();
        gl.addObserver(this);
        setupBaseState();
        if(savedInstanceState != null){
            restoreSavedState(savedInstanceState);
        }else{
            addDropDownItems();
            initiateDDAdapter();
        }
    }

    private void restoreSavedState(Bundle savedInstanceState) {
        restoreDiceList(savedInstanceState);
        restoreModelIntegers(savedInstanceState);
        restoreDiceList(savedInstanceState);
        restoreScoreRuleList(savedInstanceState);
    }

    private void restoreScoreRuleList(Bundle savedInstanceState) {
        ddItems = savedInstanceState.getStringArrayList("scoreRules");
        initiateDDAdapter();
    }

    private void restoreDiceList(Bundle savedInstanceState) {
        ArrayList<Dice> dices = savedInstanceState.getParcelableArrayList("diceList");
        gl.setDiceList(dices);
    }

    private void restoreModelIntegers(Bundle savedInstanceState) {
        gl.setDiceThrows( savedInstanceState.getInt("currentRethrow") );
        gl.setCurrentRound( savedInstanceState.getInt("currentRound") );
        ArrayList<RoundScore> roundScore = savedInstanceState.getParcelableArrayList("roundScoreList");
        gl.setRoundScoresList( roundScore );
    }
    private void setupBaseState() {
        initiateDices();
        initiateDropdown();
        initiateInfoRow();
        setUnRolledDices();
    }

    private void setUnRolledDices() {
        for(int i = 0; i < dices.length; i++) {
            dices[i].setImageDrawable(null);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelableArrayList("diceList", gl.getDices());
        savedInstanceState.putStringArrayList("scoreRules", ddItems);
        savedInstanceState.putParcelableArrayList("roundScoreList", gl.getRoundScoresList());
        savedInstanceState.putInt("currentRound", gl.getCurrentRound());
        savedInstanceState.putInt("currentRethrow", gl.getDiceThrows());

    }

    private void initiateInfoRow() {
        currentRoundView = (TextView)findViewById(R.id.currentRoundVariable);
        numberOfRethrowsView = (TextView)findViewById(R.id.numberOfRethrowsVariable);
    }

    private void updateInfoRow(int currentRound, int rethrow) {
        currentRoundView.setText(String.valueOf(currentRound));
        numberOfRethrowsView.setText(String.valueOf(rethrow));
    }

    private void updateBoard(ArrayList<Dice> dices) {
        if(!gl.isDicesRolled())
            return;
        int i = 0;
        for(Dice d : dices) {
            updateDiceView(i, d.getFaceValue(), d.isMarked());
            i++;
        }
    }

    private void initiateDices() {
        dices = new ImageView[] {
                (ImageView) findViewById(R.id.dice_one),
                (ImageView)findViewById(R.id.dice_two),
                (ImageView)findViewById(R.id.dice_three),
                (ImageView)findViewById(R.id.dice_four),
                (ImageView)findViewById(R.id.dice_five),
                (ImageView)findViewById(R.id.dice_six)
        };
    }

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

    public void nextRoundButtonEvent(View v) {
        String s = popScoreRule();
        gl.endRound(s);
        gl.unmarkDices();
        gl.setDicesRolled(false);
        setUnRolledDices();
        Toast.makeText(this, R.string.newRoundToast, Toast.LENGTH_SHORT).show();
    }

    public void throwButtonEvent(View v) {
        gl.setDicesRolled(true);
        if(gl.getDiceThrows() > 0){
            gl.playThrow();
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

    private void updateDiceView(int idx, int faceValue, boolean isMarked) {
        dices[idx].setImageResource(getDrawableID(faceValue, isMarked));
    }

    private int getDrawableID(int faceValue, boolean isMarked) {
        int retValue;
        switch( faceValue ) {
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
