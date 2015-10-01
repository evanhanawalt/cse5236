package edu.osu.RPSEmpire.Objects;

import com.parse.ParseObject;
import java.util.Date;

import com.parse.ParseClassName;

@ParseClassName("Turn")
/**
 * Turn
 *      models one turn in a Round
 */
public class Turn extends ParseObject {

    // parse database keys
    private final String ROUND_ID = "round_id";
    private final String PLAYER_1_MOVE = "player_1_move";
    private final String PLAYER_2_MOVE = "player_2_move";
    private final String TURN_NUMBER = "turn_number";
    private final String TIME_START = "time_start";
    private final String TIME_END = "time_end";

    public Turn () {
        // necessary empty constructor for subclassing parse objects
    }


    public Turn ( String round_id,
                  String player_1_move,
                  String player_2_move,
                  int turn_number,
                  Date time_start,
                  Date time_end ) {

        put(ROUND_ID, round_id);
        put(PLAYER_1_MOVE, player_1_move);
        put(PLAYER_2_MOVE, player_2_move);
        put(TURN_NUMBER, turn_number);
        put(TIME_START, time_start);
        put(TIME_END, time_start);
    }

    public void saveToServer () {
        saveInBackground();
    }

    // getter methods
    public String getRoundID () {
        return getString(ROUND_ID);
    }
    public String getPlayer1Move () {
        return getString(PLAYER_1_MOVE);
    }
    public String getPlayer2Move () {
        return getString(PLAYER_2_MOVE);
    }
    public int getTurnNumber () {
        return getInt(TURN_NUMBER);
    }
    public Date getTimeStart () {
        return getDate(TIME_START);
    }
    public Date getTimeEnd () {
        return getDate(TIME_END);
    }

}
