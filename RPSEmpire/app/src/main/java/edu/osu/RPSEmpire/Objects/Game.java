package edu.osu.RPSEmpire.Objects;

import android.content.Intent;

import com.parse.ParseObject;
import com.parse.ParseClassName;

import edu.osu.RPSEmpire.Activities.GameSetupActivity;

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

    // Game variables
    private enum result { WIN, LOSE, TIE }
    private Round[] rounds;
    private int roundNumber;
    private int bestOfNumber;
    private Player player1;
    private Player player2;
    private int player1wins;
    private int player2wins;

    public Game (){
        // necessary empty constructor for subclassing parse objects
    }

    // Constructor which will set all data values
    public Game ( Player givenPlayer1,
                  Player givenPlayer2,
                  int bestOf ) {

        roundNumber = 0;
        player1wins = 0;
        player2wins = 0;
        createNewRound();
        bestOfNumber = bestOf;

        put(PLAYER_1_ID, player1.getObjectId());
        put( PLAYER_2_ID, player2.getObjectId());
        put( BEST_OF, bestOf);
    }

    private void createNewRound() {
        roundNumber++;
        rounds[roundNumber-1] = new Round(this.getObjectId(), roundNumber);
    }

    public void saveToServer () {
        saveInBackground();
    }

    public Player resolveTurn () {
        Player winner = null;
        rounds[rounds.length - 1].createNewTurn(player1.getSelection(), player2.getSelection());
        if (player1.getSelection() != player2.getSelection()) {
            // There is a winner or a loser
            winner = resolveRound();
        }
        // Reset turn variables
        player1.setSelection(null);
        player2.setSelection(null);
        return winner;
    }

    public result determineVictory(Player.choice player1selection, Player.choice player2selection) {
        if (player1selection == player2selection) {
            return result.TIE;
        }
        else if ((player1selection == Player.choice.ROCK && player2selection == Player.choice.PAPER) ||
                (player1selection == Player.choice.PAPER && player2selection == Player.choice.SCISSORS) ||
                (player1selection == Player.choice.SCISSORS && player2selection == Player.choice.ROCK )) {
            return result.LOSE;
        }
        else  {
            return result.WIN;
        }
    }

    private Player resolveRound() {
        Player winner;

        // Determine who wins the round and add point accordingly
        switch (determineVictory(player1.getSelection(), player2.getSelection())) {
            case LOSE: {
                winner = player2;
                break;
            }
            case WIN: {
                winner = player1;
                break;
            }
            default: {
                winner = null;
                break;
            }
        }

        // End round and save it to server
        rounds[roundNumber-1].saveToServer();
        createNewRound();
        return winner;
    }

    // getters/setters
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
