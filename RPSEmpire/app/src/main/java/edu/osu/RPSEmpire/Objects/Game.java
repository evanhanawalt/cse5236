package edu.osu.RPSEmpire.Objects;

import android.content.Intent;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseClassName;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.ArrayList;

import edu.osu.RPSEmpire.Activities.GameSetupActivity;

@ParseClassName("Game")

/**
 * Game
 *      models a Game
 */
public class Game extends ParseObject {

    // parse database keys
    private final String PLAYER_1_ID = "player_1_id";
    private final String PLAYER_2_ID = "player_2_id";
    private final String BEST_OF = "best_of";

    // Game variables
    public enum result { WIN, LOSE, TIE }
    private ArrayList<Round> rounds;
    private int roundNumber;
    private int bestOfNumber;
    private String player1;
    private String player2;
    private int player1wins;
    private int player2wins;

    public Game (){
        // necessary empty constructor for subclassing parse objects
    }

    // Constructor which will set all data values
    public Game ( String givenPlayer1,
                  String givenPlayer2,
                  int bestOf ) {

        roundNumber = 0;
        player1wins = 0;
        player2wins = 0;
        bestOfNumber = bestOf;
        player1 = givenPlayer1;
        player2 = givenPlayer2;

        put(PLAYER_1_ID, player1);
        put(PLAYER_2_ID, player2);
        put(BEST_OF, bestOf);

        rounds = new ArrayList<Round>();

        this.saveToServer(new SaveCallback() {
            @Override
            public void done(ParseException e) {
               String id = getObjectId();
                createNewRound();
            }
        });
    }

    private void createNewRound() {
        roundNumber++;
        String id = this.getObjectId();
        rounds.add(new Round(this.getObjectId(), roundNumber));
    }

    public void saveToServer () {
        this.saveInBackground();
    }

    public void saveToServer (SaveCallback saveCallback) {
        this.saveInBackground(saveCallback);
    }

    public void setSelection(String playerID, Turn.choice choice) {
        if (playerID == player1) {
            rounds.get(roundNumber-1).setSelection(1, choice);
        }
        else if (playerID == player2) {
            rounds.get(roundNumber-1).setSelection(2, choice);
        }
    }

    public Turn.choice getSelection(String playerID) {
        Turn.choice selection = null;
        if (playerID == player1) {
            selection = rounds.get(roundNumber-1).getSelection(1);
        }
        else if (playerID == player2) {
            selection = rounds.get(roundNumber-1).getSelection(2);
        }
        return selection;
    }

    public String resolveTurn () {
        String winner = null;
        // Make sure both players have made a choice
        if (getSelection(player1) != null && getSelection(player2) != null) {
            if (getSelection(player1) != getSelection(player2)) {
                // There is a winner or a loser
                winner = resolveRound();
            }
        }
        return winner;
    }

    private String resolveRound() {
        String winner;

        // Determine who wins the round and add point accordingly
        switch (Turn.determineVictory(getSelection(player1), getSelection(player2))) {
            case LOSE: {
                winner = player2;
                break;
            }
            case WIN: {
                winner = player1;
                break;
            }
            default: {
                winner = null;
                break;
            }
        }

        // End round and save it to server
        rounds.get(rounds.size()-1).endTurn();
        rounds.get(roundNumber-1).saveToServer();
        createNewRound();
        return winner;
    }

    // getters/setters
    public String getPlayer1ID () {
        return getString(PLAYER_1_ID);
    }
    public String getPlayer2ID () {
        return getString(PLAYER_2_ID);
    }
    public int getBestOf () {
        return getInt(BEST_OF);
    }



}
