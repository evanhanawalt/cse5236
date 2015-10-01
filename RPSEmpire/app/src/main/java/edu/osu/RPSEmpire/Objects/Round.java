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

    public Round () {
        // necessary empty constructor for subclassing parse objects
    }

    public Round ( String game_id,
                   int round_number ) {

        super("Round");
        put(GAME_ID, game_id);
        put(ROUND_NUMBER, round_number);
    }

    public void saveToServer () {
        saveInBackground();
    }

    // getter methods
    public String getGameID () {
        return getString(GAME_ID);
    }
    public int getRoundNumber () {
        return getInt(ROUND_NUMBER);
    }
}
