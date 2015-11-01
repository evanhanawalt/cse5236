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

    // turn variables
    public static enum choice { ROCK, PAPER, SCISSORS, QUIT }
    private choice player1move;
    private choice player2move;

    public Turn () {
        // necessary empty constructor for subclassing parse objects
    }

    public Turn ( String roundID,
                  int turnNumber,
                  long timeStart) {

        player1move = null;
        player2move = null;

        put(ROUND_ID, roundID);
        put(TURN_NUMBER, turnNumber);
        put(TIME_START, timeStart);
    }

    public void saveToServer () {
        this.saveInBackground();
    }

    protected void setSelection(int playerNumber, choice chosenSelection)
    {
        if (playerNumber == 1) {
            player1move = chosenSelection;
        }
        else if (playerNumber == 2) {
            player2move = chosenSelection;
        }
    }

    protected choice getSelection(int playerNumber) {
        choice selection = null;
        if (playerNumber == 1) {
            selection = player1move;
        }
        else if (playerNumber == 2) {
            selection = player2move;
        }
        return selection;
    }

    protected void endTurn() {
        long endTime = System.nanoTime();
        put(PLAYER_1_MOVE, player1move.hashCode());
        put(PLAYER_2_MOVE, player2move.hashCode());
        put(TIME_END, endTime);
        this.saveToServer();
    }

    public static Game.result determineVictory(choice player1selection, choice player2selection) {
        if (player1selection == player2selection) {
            return Game.result.TIE;
        }
        else if ((player1selection == choice.ROCK && player2selection == choice.PAPER) ||
                (player1selection == choice.PAPER && player2selection == choice.SCISSORS) ||
                (player1selection == choice.SCISSORS && player2selection == choice.ROCK )) {
            return Game.result.LOSE;
        }
        else  {
            return Game.result.WIN;
        }
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
