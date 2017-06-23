package lassesjoblom.thirty;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class ScoreboardActivity extends AppCompatActivity {

    private TextView totalScoreLabel;
    private TextView[] scoreBoard;
    private ArrayList<RoundScore> roundScoresList;
    private int totalScore;


    public ScoreboardActivity() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);
        roundScoresList = getIntent().getParcelableArrayListExtra("scoreList");
        initiateScoreBoard();
        updateScoreBoard();
        setupTotalScoreLabel();
    }

    private void setupTotalScoreLabel() {
        totalScoreLabel = (TextView)findViewById(R.id.totalScoreLabel);
        totalScoreLabel.setText(getText(R.string.finalScore) + " " +  String.valueOf(totalScore));
    }

    private void updateScoreBoard() {
        int i = 0;
        for(RoundScore rs : roundScoresList) {
            scoreBoard[i].setText(buildScoreBoardString(i, rs));
            totalScore += rs.getTotalScore();
            i++;
        }
    }

    private String buildScoreBoardString(int idx, RoundScore rs ) {
        StringBuilder sb = new StringBuilder();
        sb.append(getScoreBoardString(idx) + " " + String.valueOf(rs.getTotalScore()));
        sb.append( System.getProperty ("line.separator") );
        sb.append( getText(R.string.scoreRuleText) + " " + rs.getScoreRule());
        sb.append( System.getProperty ("line.separator") );
        if(!rs.getScoreRule().equals(getText(R.string.dropdown_text_low))) {
            sb.append( getText(R.string.oneDiceScore) + " " + String.valueOf(rs.getSingleDiceScore()));
            sb.append( System.getProperty ("line.separator") );
            sb.append( getText(R.string.twoDiceScore) + " " + String.valueOf(rs.getTwoDiceScore()));
            sb.append( System.getProperty ("line.separator") );
            sb.append( getText(R.string.threeDiceScore) + " " + String.valueOf(rs.getThreeDiceScore()));
            sb.append( System.getProperty ("line.separator") );
            sb.append( getText(R.string.fourDiceScore) + " " + String.valueOf(rs.getFourDiceScore()));
            sb.append( System.getProperty ("line.separator") );
            sb.append( getText(R.string.fiveDiceScore) + " " + String.valueOf(rs.getFiveDiceScore()));
            sb.append( System.getProperty ("line.separator") );
            sb.append( getText(R.string.sixDiceScore) + " " + String.valueOf(rs.getSixDiceScore()));
            sb.append( System.getProperty ("line.separator") );
        }
        return sb.toString();
    }

    private String getScoreBoardString(int idx) {
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
                (TextView)findViewById(R.id.r10ScoreLabel)
        };
    }
}
