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
    private long turnStartTime;

    public Round () {
        // necessary empty constructor for subclassing parse objects
    }

    public Round ( String gameID,
                   int roundNumber ) {

        super("Round");

        turnNumber = 0;
        turnStartTime = System.currentTimeMillis();

        put(GAME_ID, gameID);
        put(ROUND_NUMBER, roundNumber);

        turns = new ArrayList<>();
        saveToServer();

        String id = getObjectId();
        createNewTurn();
    }

    protected void createNewTurn() {
        turnNumber++;
        turns.add(new Turn(this.getObjectId(), turnNumber, turnStartTime));
        turnStartTime = System.currentTimeMillis();
    }

    protected void endTurn() {
        turns.get(turnNumber-1).endTurn();
    }

    protected void setSelection(int playerNumber, Turn.choice choice) {
        turns.get(turnNumber-1).setSelection(playerNumber, choice);
    }

    protected Turn.choice getSelection(int playerNumber) {
        return turns.get(turnNumber-1).getSelection(playerNumber);
    }

    public void saveToServer () {
        try {
            save();
        }
        catch (ParseException e) {
            // Something wrong with connection to server
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
