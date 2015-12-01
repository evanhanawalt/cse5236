package edu.osu.RPSEmpire.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Random;
import java.util.Timer;

import edu.osu.RPSEmpire.Objects.Game;
import edu.osu.RPSEmpire.Objects.Turn;
import edu.osu.RPSEmpire.Objects.User;
import edu.osu.RPSEmpire.R;

/**
 * Created by Tylor on 10/20/2015.
 */

public class GameActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

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
    private int player1Selection;
    private int player2Selection;
    private ParseActivity state;
    public boolean acceptSelection;
    private GoogleApiClient mGoogleApiClient;


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("GameActivity", "onConnectionFailed Called");
        CharSequence text = "Google Play connection Failed";
        Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
        toast.show();
    }



    @Override
    public void onConnectionSuspended(int i) {
        Log.d("MainActivity", "onConnectionSuspended Called");
        // Attempt to reconnect
        mGoogleApiClient.connect();
    }


    @Override
    public void onConnected(Bundle connectionHint) {
        Log.d("MainActivity", "onConnected Called");
        // The player is signed in. Hide the sign-in button and allow the
        // player to proceed.
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .build();

        mGoogleApiClient.connect();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        alertDialog = new AlertDialog.Builder(this);
        state = ((ParseActivity) getApplicationContext());

        Intent intent = getIntent();
        bestOfNumber = intent.getIntExtra("bestOfNumber", 2);
        humanOpponent = intent.getBooleanExtra("humanOpponent", false);
        player2Id = intent.getStringExtra("player2Id");
        player1Id = intent.getStringExtra("player1Id");
        host = intent.getBooleanExtra("isHost", false);

        opponentWins = 0;
        myWins = 0;
        player1Selection = -1;
        player2Selection = -1;

        if (host || !humanOpponent) {
            game = new Game(player1Id, player2Id, bestOfNumber);
        }

        acceptSelection = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        quit();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (humanOpponent) {
            try {
                state.getIn().close();
            } catch (IOException e) {
                // Failed to close input stream
            }
            try {
                state.getOut().close();
            } catch (IOException e) {
                // Failed to close output stream
            }
            state.setIn(null);
            state.setOut(null);
        }
    }

    public void throwRock(View view) {
        player1Selection = 0;
        if (acceptSelection) throwSelection();
    }

    public void throwPaper(View view) {
        player1Selection = 1;
        if (acceptSelection) throwSelection();
    }

    public void throwScissors(View view) {
        player1Selection = 2;
        if (acceptSelection) throwSelection();
    }

    public void quit(View view) {
        if (player1Selection == -1) {
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

    private void throwSelection() {
        state = ((ParseActivity) getApplicationContext());
        acceptSelection = false;

        // Get player2 Selection
        if (humanOpponent) {
            try {
                state.getOut().write(player1Selection);
            } catch (IOException e) {
                // error sending selection
            }

            if (player1Selection < 3) try {
                player2Selection = state.getIn().read();
            } catch (IOException e) {
                // error receiving selection
            }
        }
        else {
            Random random = new Random();
            player2Selection = random.nextInt(3);
        }

        // Compare selections and resolve turn/round/game
        int turnResult;
        if (host || !humanOpponent) game.endTurn(player1Selection, player2Selection);
        turnResult = determineVictory(player1Selection, player2Selection);
        if (player2Selection < 3) {
            if (turnResult == 0) {
                if (host || !humanOpponent) game.createNewTurn();
            } else {
                if (turnResult == -1) {
                    opponentWins++;
                } else {
                    myWins++;
                }
                if ((host || !humanOpponent) && (myWins < bestOfNumber && opponentWins < bestOfNumber))
                    game.createNewRound();
            }
        } else {
            displayMessage(player2Selection);
        }

        // Display message regarding results
        if (player1Selection < 3 && player2Selection < 3) {
            if (myWins >= bestOfNumber || opponentWins >= bestOfNumber) {
                turnResult += turnResult;
                endGame();
            }
            displayMessage(turnResult);
        }
        updateTextFields();

        // reset player selections
        acceptSelection = false;
        player1Selection = -1;
        player2Selection = -1;

        final Handler h = new Handler();
        final int delay = 200;

        h.postDelayed(new Runnable(){
            public void run(){
                acceptSelection = true;
            }
        }, delay);
    }

    private void updateTextFields() {
        TextView myWinsField = (TextView) findViewById(R.id.yourwins_num);
        myWinsField.setText(Integer.toString(myWins));
        TextView oppWinsField = (TextView) findViewById(R.id.oppwins_num);
        oppWinsField.setText(Integer.toString(opponentWins));
    }

    private void quit() {
        player1Selection = 3;
        throwSelection();
        finish();
    }

    private void endGame() {

        AlertDialog dialog = alertDialog.create();
        dialog.setTitle("Play again?");
        dialog.setMessage("Would you like to play again using the same settings and opponent?");
        dialog.setCancelable(false);
        final Context currentContext = this;

        dialog.setButton(dialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // If player does want to play again, start another game
                if (humanOpponent) {
                    try {
                        // Send request to play second game
                        state.getOut().write(1);
                        try {
                            if (state.getIn().read() != 1) {
                                AlertDialog dialog2 = alertDialog.create();
                                dialog2.setTitle("Opponent has declined.");
                                dialog2.setMessage("The opponent has declined to play another game.");
                                dialog2.setCanceledOnTouchOutside(false);
                                dialog2.setCancelable(false);
                                dialog2.setButton(dialog.BUTTON_NEUTRAL, "Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                });
                                dialog2.show();
                            }
                        } catch (IOException e) {
                            AlertDialog dialog2 = alertDialog.create();
                            dialog2.setTitle("Opponent has declined.");
                            dialog2.setMessage("The opponent has declined to play another game.");
                            dialog2.setCanceledOnTouchOutside(false);
                            dialog2.setCancelable(false);
                            dialog2.setButton(dialog.BUTTON_NEUTRAL, "Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });
                            dialog2.show();
                        }
                    }
                    catch (IOException e) {
                        // Failed to write to opponent
                        finish();
                    }
                }
                myWins = 0;
                opponentWins = 0;
                updateTextFields();
                if (host | !humanOpponent) game = new Game(player1Id, player2Id, bestOfNumber);
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

    public int determineVictory(int player1selection, int player2selection) {
        if (player1selection == 3) {
            return -1;
        }
        else if (player2selection == 3) {
            return 1;
        }
        else if (player1selection == player2selection) {
            return 0;
        }
        else if ((player1selection == 0 && player2selection == 1) ||
                (player1selection == 1 && player2selection == 2) ||
                (player1selection == 2 && player2selection == 0 )) {
            return -1;
        }
        else  {
            return 1;
        }
    }

    public String choiceToString(int selection) {
        if (selection == 0) { return "rock"; }
        else if (selection == 1) {
            return "paper";
        } else if (selection == 2) { return "scissors"; }
        else if (selection == 3) { return "quit"; }
        else  { return ""; }
    }

    public void displayMessage(int messageNumber) {
        AlertDialog dialog = alertDialog.create();
        String message = "";

        // Show choice if it is a regular game message
        message += "You threw "+choiceToString(player1Selection)+", and your opponent threw "+choiceToString(player2Selection)+". ";

        switch (messageNumber) {
            case -2: {
                if (mGoogleApiClient.isConnected()) {
                    Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement_you_lose));
                } else {
                    CharSequence text = "Connect online to unlock achievements";
                    Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
                    toast.show();
                }
                dialog.setTitle("You lose!");
                dialog.setMessage(message+="You lose the game. Too bad! ");

                break;
            } case -1: {
                dialog.setTitle("You lose.");
                dialog.setMessage(message+="You lost the round. Your opponent scores 1 point. ");
                break;
            } case 0: {
                dialog.setTitle("You tie.");
                dialog.setMessage(message+="You win the turn. The round continues! ");
                break;
            } case 1: {

                if (mGoogleApiClient.isConnected()) {
                    if (player1Selection == 0) {
                        Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement_rocky_road));
                    } else if(player1Selection == 1) {
                        Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement_paper_weight));
                    } else if (player1Selection == 2){
                        Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement_split_finger));
                    }
                } else {
                    CharSequence text = "Connect online to unlock achievements";
                    Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
                    toast.show();
                }
                dialog.setTitle("You win.");
                dialog.setMessage(message+="You win the round and score 1 point! ");
                break;
            } case 2: {
                if (mGoogleApiClient.isConnected()) {
                    Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement_you_win));
                } else {
                    CharSequence text = "Connect online to unlock achievements";
                    Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
                    toast.show();
                }
                dialog.setTitle("You win!");
                dialog.setMessage(message+="You win the game. Congratulations! ");

                break;
            }
            case 3: {

                dialog.setTitle("You win!");
                dialog.setMessage("Your opponent has quit the game. You win by default!");
                dialog.setCancelable(false);
                dialog.setButton(dialog.BUTTON_NEUTRAL, "Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // If player does not want to play again, return to game setup page.
                        finish();
                    }
                });
                break;
            }
        }

        dialog.show();
    }
}