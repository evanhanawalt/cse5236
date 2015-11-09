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
    public enum result {
        WIN, LOSE, TIE
    }

    private ArrayList<Round> rounds;
    private int roundNumber;
    private int bestOfNumber;
    private String player1;
    private String player2;

    public Game() {
        // necessary empty constructor for subclassing parse objects
    }

    // Constructor which will set all data values
    public Game(String givenPlayer1,
                String givenPlayer2,
                int bestOf) {

        // Initialize Variables
        roundNumber = 0;
        bestOfNumber = bestOf;
        player1 = givenPlayer1;
        player2 = givenPlayer2;
        rounds = new ArrayList<Round>();

        // Put relevant variables on Parse
        put(PLAYER_1_ID, player1);
        put(PLAYER_2_ID, player2);
        put(BEST_OF, bestOf);

        // Save game to Parse server then create first round
        saveToServer();
        createNewRound();
    }

    public void createNewRound() {
        roundNumber++;
        String id = this.getObjectId();
        rounds.add(new Round(this.getObjectId(), roundNumber));
    }

    public void createNewTurn() {
        rounds.get(roundNumber - 1).createNewTurn();
    }

    public void endTurn(int player1selection, int player2selection) {
        rounds.get(roundNumber - 1).endTurn(player1selection, player2selection);
    }

    public void saveToServer () {
        try {
            save();
        } catch (ParseException e) {
            // Something wrong with connection to server
        }
    }

    public int getBestOfNumber(){
        return getInt(BEST_OF);
    }
}
