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
                  String player1Move,
                  String player2Move,
                  int turnNumber,
                  Date timeStart,
                  Date timeEnd ) {

        put(ROUND_ID, round_id);
        put(PLAYER_1_MOVE, player1Move);
        put(PLAYER_2_MOVE, player2Move);
        put(TURN_NUMBER, turnNumber);
        put(TIME_START, timeStart);
        put(TIME_END, timeEnd);
    }

    public void saveToServer () {
        saveInBackground();
    }

    // getters/setters
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
