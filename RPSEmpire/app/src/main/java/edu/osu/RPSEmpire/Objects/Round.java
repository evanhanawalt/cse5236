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

    public void createNewTurn(Player.choice player1selection, Player.choice player2selection) {
        turnNumber++;
        turns[turnNumber-1] = new Turn(this.getObjectId(), player1selection, player2selection, turnNumber, turnStartTime, System.nanoTime());
        turns[turnNumber-1].saveToServer();
        turnStartTime = System.nanoTime();
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
