package edu.osu.RPSEmpire.Objects;

import com.parse.ParseObject;

import com.parse.ParseClassName;

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
    private Turn[] turns;
    private int turnNumber;
    private long turnStartTime;

    public Round () {
        // necessary empty constructor for subclassing parse objects
    }

    public Round ( String gameID,
                   int roundNumber ) {

        super("Round");

        turnNumber = 0;
        turnStartTime = System.nanoTime();

        put(GAME_ID, gameID);
        put(ROUND_NUMBER, roundNumber);
    }

    protected void createNewTurn() {
        turnNumber++;
        turns[turnNumber-1] = new Turn(this.getObjectId(), turnNumber, turnStartTime);
        turnStartTime = System.nanoTime();
    }

    protected void endTurn() {
        turns[turnNumber-1].endTurn();
        turns[turnNumber-1].saveToServer();
    }

    protected void setSelection(int playerNumber, Turn.choice choice) {
      turns[turnNumber-1].setSelection(playerNumber, choice);
    }

    protected Turn.choice getSelection(int playerNumber) {
        return turns[turnNumber-1].getSelection(playerNumber);
    }

    public void saveToServer () {
        saveInBackground();
    }

    // getters/setters
    public String getGameID () {
        return getString(GAME_ID);
    }
    public int getRoundNumber () {
        return getInt(ROUND_NUMBER);
    }
}
