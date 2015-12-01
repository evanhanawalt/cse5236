package edu.osu.RPSEmpire.Objects;

import com.parse.ParseException;
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

    // turn variables
    private int turnNumber;
    private String roundId;
    private long timeStart;


    public Turn () {
        // necessary empty constructor for subclassing parse objects
    }

    public Turn ( String roundID,
                  int givenTurnNumber,
                  long timeStarted) {

        // Initialize variables
        roundId = roundID;
        turnNumber = givenTurnNumber;
        timeStart = timeStarted;
    }

    public void saveToServer () {
        try {
            save();
        }
        catch (ParseException e) {
            // Something wrong with connection to server
            pinInBackground();
        }
    }

    protected void endTurn(int player1selection, int player2selection) {
        long endTime = System.currentTimeMillis();
        put(PLAYER_1_MOVE, player1selection);
        put(PLAYER_2_MOVE, player2selection);
        put(TIME_END, endTime);
        put(ROUND_ID, roundId);
        put(TURN_NUMBER, turnNumber);
        put(TIME_START, timeStart);
        saveToServer();
    }

    // getters/setters
    public String getRoundID () {
        return getString(ROUND_ID);
    }
    public int getPlayer1Move () {
        return (int) getNumber(PLAYER_1_MOVE);
    }
    public int getPlayer2Move () {
        return (int) getNumber(PLAYER_2_MOVE);
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
