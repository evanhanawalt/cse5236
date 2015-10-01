package edu.osu.RPSEmpire.Objects;

import com.parse.ParseObject;
import com.parse.ParseClassName;

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

    public Game (){
        // necessary empty constructor for subclassing parse objects
    }

    // Constructor which will set all data values
    public Game ( String player_1_id,
                  String player_2_id,
                  int best_of ) {

        put( PLAYER_1_ID, player_1_id);
        put( PLAYER_2_ID, player_2_id);
        put( BEST_OF, best_of);
    }

    public void saveToServer () {
        saveInBackground();
    }

    // getter methods
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
