package edu.osu.RPSEmpire.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import edu.osu.RPSEmpire.Objects.Game;
import edu.osu.RPSEmpire.Objects.Player;
import edu.osu.RPSEmpire.Objects.Turn;
import edu.osu.RPSEmpire.R;

/**
 * Created by Tylor on 10/20/2015.
 */
public class GameActivity extends AppCompatActivity {

    private Player player1;
    private Player player2;
    private Game game;
    public int bestOfNumber;
    private int myWins;
    private int opponentWins;
    private boolean humanOpponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create Player Objects
        // TODO: pull player1 information from login information
        player1 = new Player("Test", true, false);
        Intent intent = getIntent();
        bestOfNumber = intent.getIntExtra("bestOfNumber", 2);
        humanOpponent = intent.getBooleanExtra("humanOpponent", false);

        if (!humanOpponent) {
            player2 = new Player("CPU", false, false);
        }
        else {
            // TODO: pull player2 information from connected partner
        }

        //Create Game Objects
        String p1 = player1.getObjectId();
        String p2 = player2.getObjectId();
        Game game = new Game(p1, p2, bestOfNumber);
        game.startGame();
        setContentView(R.layout.activity_game);
    }

    public void throwRock() {
        // TODO: find player object based on login information
        game.setSelection(player1.getObjectId(), Turn.choice.ROCK);
        throwSelection();
    }
    public void throwScissors() {
        // TODO: find player object based on login information
        game.setSelection(player1.getObjectId(), Turn.choice.ROCK);
        throwSelection();
    }
    public void throwPaper() {
        // TODO: find player object based on login information
        game.setSelection(player1.getObjectId(), Turn.choice.ROCK);
        throwSelection();
    }

    private void throwSelection () {
        if (game.getSelection(player2.getObjectId()) == null) {
            // If you choose first, do nothing and wait
        }
        else {
            // If you choose second, resolve the round
            // TODO: Display player choices
            String winner = game.resolveTurn();
            if (winner != null) {
                if (winner == player1.getObjectId()) {
                    myWins+=1;
                    TextView myWinsField = (TextView) findViewById(R.id.yourwins_num);
                    myWinsField.setText(Integer.toString(myWins));
                }
                if (winner == player2.getObjectId()) {
                    opponentWins+=1;
                    TextView myWinsField = (TextView) findViewById(R.id.oppwins_num);
                    myWinsField.setText(Integer.toString(opponentWins));
                }
            }
            if (myWins >= bestOfNumber) { resolveGame(player1); }
            else if (opponentWins >= bestOfNumber) { resolveGame(player2); }
        }
    }

    private void resolveGame(Player winner) {
        game.saveToServer();
        if (player1 == winner) {
            // TODO: player 2 Display a "You win!" message
            // TODO: player 1 Display a "You lose!" message
        }
        else {
            // TODO: player 2 Display a "You win!" message
            // TODO: player 1 Display a "You lose!" message
        }

        // TODO: Ask if player wants to play again against this opponent using these game settings and resetGame() if so

        // If player does not want to play again, return to game setup page.
        Intent i = new Intent(this, GameSetupActivity.class);
        startActivity(i);
    }

    public void quit() {
        // TODO: implement game ending early and player quitting AND opponent quitting
    }
}
