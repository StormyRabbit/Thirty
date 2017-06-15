package lassesjoblom.thirty;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import static lassesjoblom.thirty.R.id.spinner;

public class MainActivity extends AppCompatActivity {

    private ThrowThirtyModel ttm = new ThrowThirtyModel();
    private ImageView[] dices = null;

    private ArrayList<String> ddItems;
    private Spinner dropdown;
    private ArrayAdapter<String> ddAdapter;

    private TextView scoreView = null;
    private TextView currentRoundView = null;
    private TextView numberOfRethrowsView = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupBaseState();
        if(savedInstanceState != null){
            restoreSavedState(savedInstanceState);
        }else{
            addDropDownItems();
            initiateDDAdapter();
        }
        updateView();
    }

    private void restoreSavedState(Bundle savedInstanceState) {
        restoreDiceList(savedInstanceState);
        restoreModelIntegers(savedInstanceState);
        restoreDiceList(savedInstanceState);
        restoreScoreRuleList(savedInstanceState);
        updateInfoRow();
    }

    private void restoreScoreRuleList(Bundle savedInstanceState) {
        ddItems = savedInstanceState.getStringArrayList("scoreRules");
        initiateDDAdapter();
    }

    private void restoreDiceList(Bundle savedInstanceState) {
        ArrayList<Dice> dices = savedInstanceState.getParcelableArrayList("diceList");
        ttm.setDiceList(dices);
    }

    private void restoreModelIntegers(Bundle savedInstanceState) {
        ttm.setRethrow( savedInstanceState.getInt("currentRethrow") );
        ttm.setCurrentRound( savedInstanceState.getInt("currentRound") );
        ttm.setScore( savedInstanceState.getInt("currentScore") );
    }
    private void setupBaseState() {
        initiateDices();
        initiateDropdown();
        initiateInfoRow();

    }
    private void updateView() {
        updateInfoRow();
        updateBoard();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelableArrayList("diceList",ttm.getDices());
        savedInstanceState.putStringArrayList("scoreRules", ddItems);
        savedInstanceState.putInt("currentScore", ttm.getScore());
        savedInstanceState.putInt("currentRound", ttm.getCurrentRound());
        savedInstanceState.putInt("currentRethrow", ttm.getRethrow());
        super.onSaveInstanceState(savedInstanceState);
    }

    private void initiateInfoRow() {
        scoreView = (TextView)findViewById(R.id.currentScoreVariable);
        currentRoundView = (TextView)findViewById(R.id.currentRoundVariable);
        numberOfRethrowsView = (TextView)findViewById(R.id.numberOfRethrowsVariable);
    }

    private void updateInfoRow() {
        scoreView.setText(String.valueOf(ttm.getScore()));
        currentRoundView.setText(String.valueOf(ttm.getCurrentRound()));
        numberOfRethrowsView.setText(String.valueOf(ttm.getRethrow()));
    }

    private void updateBoard() {
        ArrayList<Dice> dices = ttm.getDices();
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
        Dice d = null;
        switch( v.getId() ) {
            case R.id.dice_one:
                d = ttm.setDiceMarker(0);
                break;
            case R.id.dice_two:
                d = ttm.setDiceMarker(1);
                break;
            case R.id.dice_three:
                d = ttm.setDiceMarker(2);
                break;
            case R.id.dice_four:
                d = ttm.setDiceMarker(3);
                break;
            case R.id.dice_five:
                d = ttm.setDiceMarker(4);
                break;
            case R.id.dice_six:
                d = ttm.setDiceMarker(5);
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
    public void throwButtonEvent(View v) {
        ttm.playThrow();
        if(ttm.getCurrentRound() == 0){
            popScoreRule();
        }
        updateInfoRow();
        updateBoard();

    }

    private String popScoreRule() {
        removeScoreRuleFromDropDown();
        String scoreRule = "";
        return scoreRule;
    }

    private void removeScoreRuleFromDropDown(){
        Object ddObj = dropdown.getSelectedItem();
        ddAdapter.remove((String)ddObj);
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
