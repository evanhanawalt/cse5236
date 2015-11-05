package edu.osu.RPSEmpire.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import edu.osu.RPSEmpire.Objects.Game;
import edu.osu.RPSEmpire.Objects.Turn;
import edu.osu.RPSEmpire.Objects.User;
import edu.osu.RPSEmpire.R;

/**
 * Created by Tylor on 10/20/2015.
 */
public class GameActivity extends AppCompatActivity {

    private String player1Id;
    private String player2Id;
    private boolean host;
    private Game game;
    public int bestOfNumber;
    private int myWins;
    private int opponentWins;
    private boolean humanOpponent;
    private AlertDialog.Builder alertDialog;
    private String message;
    private InputStream inStream;
    private OutputStream outStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        startGame();

        // Setup Alert Dialogues
        alertDialog = new AlertDialog.Builder(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        quit();
    }

    protected void quit() {
        game.setSelection(player1Id, Turn.choice.QUIT);
        throwSelection(true);
        if (humanOpponent) {
            try {
                inStream.close();
            } catch (IOException e) {
                // Failed to close input stream
            }
            try {
                outStream.close();
            } catch (IOException e) {
                // Failed to close output stream
            }
            ParseActivity state = ((ParseActivity) getApplicationContext());
            state.setIn(null);
            state.setOut(null);
        }
        finish();
    }

    protected void startGame() {

        Intent intent = getIntent();
        bestOfNumber = intent.getIntExtra("bestOfNumber", 2);
        humanOpponent = intent.getBooleanExtra("humanOpponent", false);
        player2Id = intent.getStringExtra("player2Id");
        player1Id = intent.getStringExtra("player1Id");
        host = intent.getBooleanExtra("isHost", false);

        // Setup bluetooth connection variables if needed
        if (humanOpponent) {
            ParseActivity state = ((ParseActivity) getApplicationContext());
            inStream = state.getIn();
            outStream = state.getOut();
            Timer mTimer = new Timer();
            mTimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    try {
                        int oppSelectionOrd = inStream.read();
                        Turn.choice oppSelection = Turn.choice.values()[oppSelectionOrd];
                        if (oppSelection != null) {
                            game.setSelection(player2Id, oppSelection);
                            throwSelection(false);
                        }
                    } catch (IOException e) {
                        // error recieving selection
                    }
                }
            }, 0, 1000);
        }

        game = new Game(player1Id, player2Id, bestOfNumber);
    }
    public void throwRock(View view) {
        if (game != null) {
            game.setSelection(player1Id, Turn.choice.ROCK);
            throwSelection(true);
        }
    }
    public void throwScissors(View view) {
        if (game != null) {
            game.setSelection(player1Id, Turn.choice.SCISSORS);
            throwSelection(true);
        }
    }
    public void throwPaper(View view) {
        if (game != null) {
            game.setSelection(player1Id, Turn.choice.PAPER);
            throwSelection(true);
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
                    quit();
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

    private void throwSelection (boolean player1thrown) {

        if (humanOpponent && player1thrown) {
            try {
                outStream.write(game.getSelection(player1Id).ordinal());
            } catch (IOException e) {
                // error sending selection
            }
        }

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
                throwSelection(false);
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
                message = "You threw " + Turn.choiceToString(game.getSelection(player1Id));
                message += ", and your opponent threw " + Turn.choiceToString(game.getSelection(player2Id));
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
                            dialog.setTitle("You win the round!");
                            dialog.setMessage(message);
                            dialog.show();
                            game.createNewRound();
                        }
                    }
                    if (winner.compareTo(player2Id) == 0) {
                        opponentWins += 1;
                        TextView myWinsField = (TextView) findViewById(R.id.oppwins_num);
                        myWinsField.setText(Integer.toString(opponentWins));
                        if (opponentWins >= bestOfNumber) {
                            resolveGame(player2Id);
                        } else {
                            // Display lose turn message
                            message += "You lose the round!";
                            AlertDialog dialog = alertDialog.create();
                            dialog.setTitle("You lose the round!");
                            dialog.setMessage(message);
                            dialog.show();
                            game.createNewRound();
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
