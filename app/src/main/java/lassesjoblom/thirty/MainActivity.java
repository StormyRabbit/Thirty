package lassesjoblom.thirty;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    private ThrowThirtyModel ttm = new ThrowThirtyModel();
    private ImageView[] dices = null;

    private String[] ddItems;
    private Spinner dropdown;
    private ArrayAdapter<String> ddAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initiateDices();
        initiateDropdown();
        startBoard();
    }
    private void startBoard(){
        updateDiceView(1, 1, false);
        updateDiceView(2, 1, false);
        updateDiceView(3, 1, false);
        updateDiceView(4, 1, false);
        updateDiceView(5, 1, false);
        updateDiceView(6, 1, false);
    }
    private void initiateDices(){
        dices = new ImageView[]{
                (ImageView)findViewById(R.id.dice_one),
                (ImageView)findViewById(R.id.dice_two),
                (ImageView)findViewById(R.id.dice_three),
                (ImageView)findViewById(R.id.dice_four),
                (ImageView)findViewById(R.id.dice_five),
                (ImageView)findViewById(R.id.dice_six)
        };
    }
    private void initiateDropdown(){
        dropdown = (Spinner)findViewById(R.id.spinner);
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

    public void throwButtonEvent(){

    }

    public void diceOnClickEvent(View v){
       switch (v.getId()){
           case R.id.dice_one:

               break;
           case R.id.dice_two:
               break;
           case R.id.dice_three:
               break;
           case R.id.dice_four:
               break;
           case R.id.dice_five:
               break;
           case R.id.dice_six:
               break;
       }
    }

    private void updateDiceView(int idx, int faceValue, boolean isMarked){
        dices[idx].setImageResource(getDrawableID(faceValue, isMarked));
    }

    private int getDrawableID(int faceValue, boolean isMarked){
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
