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
    public static enum choice { ROCK, PAPER, SCISSORS, QUIT }
    private choice player1move;
    private choice player2move;
    private int turnNumber;
    private String roundId;
    private long timeStart;


    public Turn () {
        // necessary empty constructor for subclassing parse objects
    }

    public Turn ( String roundID,
                  int givenTurnNumber,
                  long timeStarted) {

        player1move = null;
        player2move = null;

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
            e.printStackTrace();
        }
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
        long endTime = System.currentTimeMillis();
        put(PLAYER_1_MOVE, choiceToString(player1move));
        put(PLAYER_2_MOVE, choiceToString(player2move));
        put(TIME_END, endTime);
        put(ROUND_ID, roundId);
        put(TURN_NUMBER, turnNumber);
        put(TIME_START, timeStart);
        this.saveToServer();
    }

    public static Game.result determineVictory(choice player1selection, choice player2selection) {
        if (player1selection == choice.QUIT) {
            return Game.result.LOSE;
        }
        else if (player2selection == choice.QUIT) {
            return Game.result.WIN;
        }
        else if (player1selection == player2selection) {
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

    public static String choiceToString(choice selection) {
        if (selection == choice.ROCK) { return "rock"; }
        else if (selection == choice.PAPER) { return "paper"; }
        else if (selection == choice.SCISSORS) { return "scissors"; }
        else  { return "quit"; }
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
