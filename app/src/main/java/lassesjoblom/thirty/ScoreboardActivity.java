package lassesjoblom.thirty;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ScoreboardActivity extends AppCompatActivity {


    private TextView[] scoreBoard;
    private Button playAgainBut;

    public ScoreboardActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);
        initiateScoreBoard();
        updateScoreBoard(savedInstanceState.getIntArray("scores"));
    }

    private void updateScoreBoard(int[] scores) {
        for(int i = 0; i < scoreBoard.length; i++){
            String text = getScoreBoardString(i);
            scoreBoard[i].setText(text + " " + scores[i]);
        }
    }
    private String getScoreBoardString(int idx){
        String retString = "";
        switch( idx ) {
            case 0:
                retString = (String)getText(R.string.scoreRoundOne);
                break;
            case 1:
                retString = (String)getText(R.string.scoreRoundTwo);
                break;
            case 2:
                retString = (String)getText(R.string.scoreRoundThree);
                break;
            case 3:
                retString = (String)getText(R.string.scoreRoundFour);
                break;
            case 4:
                retString = (String)getText(R.string.scoreRoundFive);
                break;
            case 5:
                retString = (String)getText(R.string.scoreRoundSix);
                break;
            case 6:
                retString = (String)getText(R.string.scoreRoundSeven);
                break;
            case 7:
                retString = (String)getText(R.string.scoreRoundEight);
                break;
            case 8:
                retString = (String)getText(R.string.scoreRoundNine);
                break;
            case 9:
                retString = (String)getText(R.string.scoreRoundTen);
                break;
            default:
                retString = (String)getText(R.string.finalScore);
                break;
        }
        return retString;
    }
    private void initiateScoreBoard() {
        scoreBoard = new TextView[]{
                (TextView)findViewById(R.id.r1ScoreLabel),
                (TextView)findViewById(R.id.r2ScoreLabel),
                (TextView)findViewById(R.id.r3ScoreLabel),
                (TextView)findViewById(R.id.r4ScoreLabel),
                (TextView)findViewById(R.id.r5ScoreLabel),
                (TextView)findViewById(R.id.r6ScoreLabel),
                (TextView)findViewById(R.id.r7ScoreLabel),
                (TextView)findViewById(R.id.r8ScoreLabel),
                (TextView)findViewById(R.id.r9ScoreLabel),
                (TextView)findViewById(R.id.r10ScoreLabel),
                (TextView)findViewById(R.id.finalScoreLabel)
        };
    }

    public void playAgainButEvent(View v){

    }
}
