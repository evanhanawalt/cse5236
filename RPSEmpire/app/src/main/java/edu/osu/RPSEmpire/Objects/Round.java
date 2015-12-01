package edu.osu.RPSEmpire.Objects;

import com.parse.ParseException;
import com.parse.ParseObject;

import com.parse.ParseClassName;
import com.parse.SaveCallback;

import java.util.ArrayList;

@ParseClassName("Round")
/**
 * Round
 *      models one round of a Game
 */
public class Round extends ParseObject {

    // parse database keys
    private final String GAME_ID = "game_id";
    private final String ROUND_NUMBER = "round_number";

    // Round variables
    private ArrayList<Turn> turns;
    private int turnNumber;

    public Round () {
        // necessary empty constructor for subclassing parse objects
    }

    public Round ( String gameID,
                   int roundNumber ) {

        // Initialize Variables
        turnNumber = 0;
        turns = new ArrayList<>();

        // Put relevant variables on parse
        put(GAME_ID, gameID);
        put(ROUND_NUMBER, roundNumber);

        // Save object to server and create first turn
        saveToServer();
        createNewTurn();
    }

    protected void createNewTurn() {
        turnNumber++;
        turns.add(new Turn(this.getObjectId(), turnNumber, System.currentTimeMillis()));
    }

    protected void endTurn(int player1selection, int player2selection) {
        turns.get(turnNumber-1).endTurn(player1selection, player2selection);
    }

    public void saveToServer () {
        try {
            save();
        }
        catch (ParseException e) {
            pinInBackground();
        }
    }

    public void saveToServer (SaveCallback saveCallback) {
        this.saveInBackground(saveCallback);
    }


    // getters/setters
    public String getGameID () {
        return getString(GAME_ID);
    }
    public int getRoundNumber () {
        return getInt(ROUND_NUMBER);
    }
}
