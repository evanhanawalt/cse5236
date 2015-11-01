package edu.osu.RPSEmpire.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import edu.osu.RPSEmpire.Objects.Game;
import edu.osu.RPSEmpire.Objects.Player;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create Player Objects
        // TODO: pull player1 information from login information
        player1 = new Player("Test", true, false);
        Intent intent = getIntent();
        bestOfNumber = intent.getIntExtra("bestOfNumber", 2);
        if (intent.getBooleanExtra("humanOpponent", false)) {
            player2 = new Player("CPU", false, false);
        }
        else {
            // TODO: pull player2 information from connected partner
        }

        //Create Game Objects
        Game game = new Game(player1, player2, bestOfNumber);

        setContentView(R.layout.activity_game);
    }

    public void throwRock() {
        // TODO: find player object based on login information
        player1.setSelection(Player.choice.ROCK);
        throwSelection();
    }
    public void throwScissors() {
        // TODO: find player object based on login information
        player1.setSelection(Player.choice.SCISSORS);
        throwSelection();
    }
    public void throwPaper() {
        // TODO: find player object based on login information
        player1.setSelection(Player.choice.PAPER);
        throwSelection();
    }

    private void throwSelection () {
        if (player2.getSelection() == null) {
            // If you choose first, do nothing and wait
        }
        else {
            // If you choose second, resolve the round
            // TODO: Display player choices
            Player winner = game.resolveTurn();
            if (winner != null) {
                if (winner == player1) {
                    myWins+=1;
                    TextView myWinsField = (TextView) findViewById(R.id.yourwins_num);
                    myWinsField.setText(Integer.toString(myWins));
                }
                if (winner == player2) {
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
