package edu.osu.RPSEmpire.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.List;
import java.util.Random;

import edu.osu.RPSEmpire.Objects.Game;
import edu.osu.RPSEmpire.Objects.Player;
import edu.osu.RPSEmpire.Objects.Turn;
import edu.osu.RPSEmpire.Objects.User;
import edu.osu.RPSEmpire.R;

/**
 * Created by Tylor on 10/20/2015.
 */
public class GameActivity extends AppCompatActivity {

    private String player1Id;
    private String player2Id;
    private Game game;
    public int bestOfNumber;
    private int myWins;
    private int opponentWins;
    private boolean humanOpponent;
    private AlertDialog.Builder alertDialog;
    private String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        startGame();
        // Setup Alert Dialogues
        alertDialog = new AlertDialog.Builder(this);
    }

    protected void startGame() {

        Intent intent = getIntent();
        bestOfNumber = intent.getIntExtra("bestOfNumber", 2);
        humanOpponent = intent.getBooleanExtra("humanOpponent", false);
        player2Id = intent.getStringExtra("player2Id");

        final ParseUser currentUser;
        // Pull player info from database
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.getInBackground(User.getCurrentUser().getObjectId(), new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null) {
                    definePlayer(user);
                } else {
                    // Problem fetching user's player object
                }
            }
        });
    }

    private void definePlayer(ParseUser user) {
        player1Id = (String) user.get("player_id");
        game = new Game(player1Id, player2Id, bestOfNumber);
    }

    public void throwRock(View view) {
        if (game != null) {
            game.setSelection(player1Id, Turn.choice.ROCK);
            throwSelection();
        }
    }
    public void throwScissors(View view) {
        if (game != null) {
            game.setSelection(player1Id, Turn.choice.SCISSORS);
            throwSelection();
        }
    }
    public void throwPaper(View view) {
        if (game != null) {
            game.setSelection(player1Id, Turn.choice.PAPER);
            throwSelection();
        }
    }
    public void quit(View view) {
        if (game != null) {
            message = "You have chosen to quit the game. You will lose the current game if you quit! Are you sure you want to do that?";
            AlertDialog dialog = alertDialog.create();
            dialog.setTitle("Are you sure?");
            dialog.setMessage(message);
            dialog.setCancelable(false);
            final Context currentContext = this;
            dialog.setButton(dialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // If player does want to quit, return to game setup page.
                    game.setSelection(player1Id, Turn.choice.QUIT);
                    throwSelection();
                    finish();
                }
            });
            dialog.setButton(dialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Do nothing if they choose not to quit
                }
            });
            dialog.show();
        }
    }

    private void throwSelection () {
        if (game.getSelection(player2Id) == null) {
            // If you choose first, do nothing and wait unless opponent is computer
            if (!humanOpponent) {
                Random random = new Random();
                int randomChoice = random.nextInt(3);
                Turn.choice selection = null;
                if (randomChoice == 0) { selection = Turn.choice.ROCK; }
                else if (randomChoice == 1) { selection = Turn.choice.SCISSORS; }
                else if (randomChoice == 2) { selection = Turn.choice.PAPER; }
                game.setSelection(player2Id, selection);
                throwSelection();
            }
        }
        else {
            // If you choose second, resolve the round

            if (game.getSelection(player1Id) == Turn.choice.QUIT) {
                message = "You have quit the game. ";
                opponentWins = bestOfNumber;
            }
            else if (game.getSelection(player2Id) == Turn.choice.QUIT) {
                message = "Your opponent has quit the game. ";
                myWins = bestOfNumber;
            }
            else {
                message = "You threw " + Turn.ChoiceToString(game.getSelection(player1Id));
                message += ", and your opponent threw " + Turn.ChoiceToString(game.getSelection(player2Id));
                message += ". ";

                String winner = game.resolveTurn();
                if (winner != null) {
                    if (winner.compareTo(player1Id) == 0) {
                        myWins += 1;
                        TextView myWinsField = (TextView) findViewById(R.id.yourwins_num);
                        myWinsField.setText(Integer.toString(myWins));
                        if (myWins >= bestOfNumber) {
                            resolveGame(player1Id);
                        } else {
                            // Display win turn message
                            message += "You win the round!";
                            AlertDialog dialog = alertDialog.create();
                            dialog.setTitle("You win!");
                            dialog.setMessage(message);
                            dialog.show();
                        }
                    }
                    if (winner.compareTo(player2Id) == 0) {
                        opponentWins += 1;
                        TextView myWinsField = (TextView) findViewById(R.id.oppwins_num);
                        myWinsField.setText(Integer.toString(opponentWins));
                        if (opponentWins >= bestOfNumber) {
                            resolveGame(player2Id);
                        } else {
                            // Display win turn message
                            message += "You lose the round!";
                            AlertDialog dialog = alertDialog.create();
                            dialog.setTitle("You lose!");
                            dialog.setMessage(message);
                            dialog.show();
                        }
                    }
                } else {
                    // Display tie message
                    if (!humanOpponent) {
                        game.setSelection(player2Id, null);
                    }
                    message += "The turn was a tie!";
                    AlertDialog dialog = alertDialog.create();
                    dialog.setTitle("The turn was a tie!");
                    dialog.setMessage(message);
                    dialog.show();
                }
            }
        }
    }

    private void resolveGame(String winner) {
        Turn.choice playerChoice = game.getSelection(player1Id);
        if (player1Id.compareTo(winner) == 0) {
            // Display game end win message
            message = "You win the game. Congratulations!";
            alertDialog.setTitle("You win!");
            alertDialog.setMessage(message);
        } else {
            // Display game end lose message
            message = "You lost the game! Too bad.";
            alertDialog.setTitle("You lose!");
        }

        message += " Would you like to play again using the same settings and opponent?";
        AlertDialog dialog = alertDialog.create();
        dialog.setMessage(message);
        dialog.setCancelable(false);
        final Context currentContext = this;
        dialog.setButton(dialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // If player does want to play again, return to game setup page.
                myWins = 0;
                opponentWins = 0;
                TextView myWinsField = (TextView) findViewById(R.id.yourwins_num);
                myWinsField.setText(Integer.toString(myWins));
                TextView oppWinsField = (TextView) findViewById(R.id.oppwins_num);
                oppWinsField.setText(Integer.toString(opponentWins));
                game.saveToServer();
                game = new Game(player1Id, player2Id, bestOfNumber);
            }
        });
        dialog.setButton(dialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // If player does not want to play again, return to game setup page.
                finish();
            }
        });
        dialog.show();
    }
}
