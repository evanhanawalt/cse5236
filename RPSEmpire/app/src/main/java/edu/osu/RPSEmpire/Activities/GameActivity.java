package edu.osu.RPSEmpire.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import edu.osu.RPSEmpire.Objects.Game;
import edu.osu.RPSEmpire.Objects.Player;
import edu.osu.RPSEmpire.Objects.Turn;
import edu.osu.RPSEmpire.Objects.User;
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
        setContentView(R.layout.activity_game);
       // startGame();
    }

    protected void startGame() {

        // Create Player Objects
        // TODO: pull player1 information from login information

        String player1id;
        String player2id;

        player1 = new Player("Test", true, false);
        player1.saveToServer();

        Intent intent = getIntent();
        bestOfNumber = intent.getIntExtra("bestOfNumber", 2);
        humanOpponent = intent.getBooleanExtra("humanOpponent", false);

        if (!humanOpponent) {
            player2 = new Player("CPU", false, false);
            player2.saveToServer();
        }
        else {
            // TODO: pull player2 information from connected partner
        }

        //Create Game Objects

        while ( player1.getObjectId() == null || player2.getObjectId() == null ) {
            // Wait until both callbacks finish
        }

        game = new Game(player1.getObjectId(), player2.getObjectId(), bestOfNumber);

    }

    public void throwRock(View view) {
        // TODO: find player object based on login information
        game.setSelection(player1.getObjectId(), Turn.choice.ROCK);
        throwSelection();
    }
    public void throwScissors(View view) {
        // TODO: find player object based on login information
        game.setSelection(player1.getObjectId(), Turn.choice.SCISSORS);
        throwSelection();
    }
    public void throwPaper(View view) {
        // TODO: find player object based on login information
        game.setSelection(player1.getObjectId(), Turn.choice.PAPER);
        throwSelection();
    }
    public void quit(View view) {
        game.setSelection(player1.getObjectId(), Turn.choice.QUIT);
        throwSelection();
    }

    private void throwSelection () {
        if (game.getSelection(player2.getObjectId()) == null) {
            // If you choose first, do nothing and wait unless opponent is computer
            if (!humanOpponent) {
                // TODO: choose randomly
                game.setSelection(player2.getObjectId(), Turn.choice.ROCK);
                throwSelection();
            }
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


                if (myWins >= bestOfNumber) { resolveGame(player1); }
                else if (opponentWins >= bestOfNumber) { resolveGame(player2); }
            }
            else {
                // TODO: Tied game message
            }
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
}
