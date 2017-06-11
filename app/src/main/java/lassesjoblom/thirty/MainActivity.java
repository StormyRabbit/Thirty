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

    private String[] ddItems;
    private Spinner dropdown;
    private ArrayAdapter<String> ddAdapter;

    private TextView scoreView = null;
    private TextView currentRoundView = null;
    private TextView numberOfRethrowsView = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState == null){
            setupCleanState();
        }else{
            restoreSavedState();
        }
    }

    private void restoreSavedState() {

    }

    private void setupCleanState() {
        initiateDices();
        initiateDropdown();
        initiateInfoRow();
        updateInfoRow();
        updateBoard();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelableArrayList("diceList",ttm.getDices());
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
        ddItems = new String[] {
                getString(R.string.dropdown_text_low),
                getString(R.string.dropdown_text_4),
                getString(R.string.dropdown_text_5),
                getString(R.string.dropdown_text_6),
                getString(R.string.dropdown_text_7),
                getString(R.string.dropdown_text_8),
                getString(R.string.dropdown_text_9),
                getString(R.string.dropdown_text_10),
                getString(R.string.dropdown_text_11),
                getString(R.string.dropdown_text_12)
        };
        ddAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, ddItems);
        dropdown.setAdapter(ddAdapter);
    }

    public void throwButtonEvent(View v) {
        ttm.throwUnmarkedDices();
        ttm.updateRound();
        popScoreRule();
        updateInfoRow();
        updateBoard();
    }

    private String popScoreRule() {
        //dropdown.getItemAtPosition(dropdown.getSelectedItemPosition()).toString();
        //ddAdapter.remove((String)dropdown.getSelectedItem());
        //dropdown.setAdapter(ddAdapter);
        String scoreRule = "";
        return scoreRule;
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
