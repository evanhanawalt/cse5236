package edu.osu.RPSEmpire.Activities;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.LabeledIntent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.Set;
import java.util.UUID;

import edu.osu.RPSEmpire.Objects.Game;
import edu.osu.RPSEmpire.Objects.Player;
import edu.osu.RPSEmpire.Objects.User;
import edu.osu.RPSEmpire.R;

public class GameSetupActivity extends AppCompatActivity {

    AlertDialog.Builder alertDialog;
    private int bestOfNumber;
    private boolean humanOpponent;
    private UUID APP_UUID = UUID.fromString("361fe410-80d5-11e5-8bcf-feff819cdc9f");
    private BluetoothAdapter mBluetoothAdapter;
    private String opponentID;
    private String playerID;
    private boolean host;
    private ParseActivity state;
    private BluetoothServerSocket serverSocket;
    private BluetoothSocket clientSocket;
    private AlertDialog discoveryDialog;

    private BroadcastReceiver deviceReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() == BluetoothDevice.ACTION_FOUND) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                try {
                    clientSocket = device.createRfcommSocketToServiceRecord(APP_UUID);
                    try {
                        clientSocket.connect();
                        host = false;
                        mBluetoothAdapter.cancelDiscovery();
                        connectedWithOpponent(clientSocket);
                    } catch (IOException e) {
                        Log.d("GameSetupActivity", " Failed to connect");
                    }
                } catch (IOException e) {
                    Log.d("GameSetupActivity", " Failed to connect");
                }
            } else if (intent.getAction() == BluetoothAdapter.ACTION_DISCOVERY_FINISHED) {
                discoveryDialog.dismiss();
            } else if (intent.getAction() == BluetoothAdapter.ACTION_DISCOVERY_STARTED) {
                discoveryDialog = alertDialog.create();
                discoveryDialog.setTitle("Discovering new devices");
                discoveryDialog.show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_setup);
    }

    @Override
    public void onStart(){
        super.onStart();
        IntentFilter i = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        i.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        i.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        registerReceiver(deviceReciever, i);
        final ParseUser currentUser;
        // Pull player info from database
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        try {
            ParseUser user = query.get(User.getCurrentUser().getObjectId());
            playerID = (String) user.get("player_id");
        }
        catch (ParseException e) {
            // Failed to get player info from parse database
        }

        // Setup Alert Dialogues
        alertDialog = new AlertDialog.Builder(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (serverSocket != null) try {
            serverSocket.close();
            serverSocket = null;
        } catch (IOException e) {
            // Error closing socket
        }
        if (clientSocket != null) try {
            clientSocket.close();
            clientSocket = null;
        } catch (IOException e) {
            // Error closing socket
        }
    }

    public void back(View view) {
        finish();
    }

    private void connectedWithOpponent(BluetoothSocket opponent) {

        state = ((ParseActivity) getApplicationContext());
        int error = 0;

        try {
            state.setOut(opponent.getOutputStream());
            try {
                state.getOut().write(playerID.getBytes("UTF-8"));
            }
            catch (IOException e) {
                error++;
                AlertDialog dialog = alertDialog.create();
                dialog.setTitle("Failed.");
                dialog.setMessage("Failed to send player information to opponent.");
                dialog.show();
            }
        } catch (IOException e) {
            error++;
            AlertDialog dialog = alertDialog.create();
            dialog.setTitle("Failed.");
            dialog.setMessage("Failed to open output stream with opponent.");
            dialog.show();
        }

        if (error == 0) try {
            state.setIn(opponent.getInputStream());
            try {
                byte[] buffer = new byte[playerID.length()];
                state.getIn().read(buffer);
                opponentID = new String(buffer, "UTF-8");
            }
            catch (IOException e) {
                error++;
                AlertDialog dialog = alertDialog.create();
                dialog.setTitle("Failed.");
                dialog.setMessage("Failed to read player information from opponent.");
                dialog.show();
            }
        } catch (IOException e) {
            error++;
            AlertDialog dialog = alertDialog.create();
            dialog.setTitle("Failed.");
            dialog.setMessage("Failed to open input stream with opponent.");
            dialog.show();
        }


        if (error == 0){
            humanOpponent = true;
            ParseActivity state = ((ParseActivity) getApplicationContext());
            startGame();
        }
    }

    public void hostGame(View view) {

        AlertDialog dialog = alertDialog.create();
        dialog.setTitle("Host a game?");
        dialog.setMessage("Would you like to host a game between you and another opponent? This will require a Bluetooth connection between the two devices, and your opponent will need to press the join game button on his device.");
        dialog.setButton(dialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Enable Bluetooth
                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (!mBluetoothAdapter.isEnabled()) {
                    Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableIntent, 0);
                } else {

                    final AlertDialog dialog2 = alertDialog.create();
                    dialog2.setTitle("Hosting...");
                    dialog2.setMessage("Waiting for an opponent to join this game.");
                    dialog2.setCancelable(false);
                    dialog2.setCanceledOnTouchOutside(false);
                    dialog2.show();

                    final Handler h = new Handler();
                    final int delay = 200;

                    h.postDelayed(new Runnable() {
                        public void run() {
                            // Set up host connection
                            try {
                                serverSocket = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("RPS Empire", APP_UUID);
                                try {
                                    host = true;
                                    BluetoothSocket returnedSocket = null;
                                    if (dialog2.isShowing())
                                        returnedSocket = serverSocket.accept(6000);
                                    dialog2.dismiss();
                                    connectedWithOpponent(returnedSocket);
                                } catch (IOException e) {
                                    dialog2.dismiss();
                                    AlertDialog dialog3 = alertDialog.create();
                                    dialog3.setTitle("Host Timeout.");
                                    dialog3.setMessage("The window for your opponent to join your game has timed out. Please try again.");
                                    dialog3.show();
                                    serverSocket.close();
                                    serverSocket = null;
                                }
                            } catch (IOException e) {
                                dialog2.dismiss();
                                AlertDialog dialog3 = alertDialog.create();
                                dialog3.setTitle("Failed.");
                                dialog3.setMessage("Failed to connect with the server to open host game.");
                                dialog3.show();
                            }
                        }
                    }, delay);
                }
            }
        });
        dialog.setButton(dialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // If player does not want to host a game, do nothing
            }
        });


        dialog.show();
    }

    public void joinGame(View view) {

        AlertDialog dialog = alertDialog.create();
        dialog.setTitle("Join a game?");
        dialog.setMessage("Would you like to search for a hosted game created by another opponent? This will require a Bluetooth connection between the two devices, and your opponent will need to press the host game button on his device.");
        dialog.setButton(dialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Enable Bluetooth
                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (!mBluetoothAdapter.isEnabled()) {
                    Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableIntent, 0);
                } else {

                    final AlertDialog dialog2 = alertDialog.create();
                    dialog2.setTitle("Joining...");
                    dialog2.setMessage("Searching for a game to join.");
                    dialog2.setCancelable(false);
                    dialog2.setCanceledOnTouchOutside(false);
                    dialog2.show();

                    final Handler h = new Handler();
                    final int delay = 200;

                    h.postDelayed(new Runnable() {
                        public void run() {
                            // Look to join host connection

                            Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();
                            for (BluetoothDevice device : devices) {
                                try {
                                    clientSocket = device.createRfcommSocketToServiceRecord(APP_UUID);
                                    try {
                                        clientSocket.connect();
                                        host = false;
                                        dialog2.dismiss();
                                        connectedWithOpponent(clientSocket);
                                    } catch (IOException e) {
                                        Log.d("GameSetupActivity", " Failed to connect");
                                    }
                                } catch (IOException e) {
                                    Log.d("GameSetupActivity", " Failed to connect");
                                }
                            }
                            dialog2.dismiss();
                            // either no bounded devices, OR failed to connect to all devices bounded
                            mBluetoothAdapter.startDiscovery();

                        }
                    }, delay);
                }
            }
        });
        dialog.setButton(dialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // If player does not want to join a game, do nothing
            }
        });


        dialog.show();
    }

    public void singlePlayerGame(View view) {
        humanOpponent = false;
        opponentID = "3euXx89GXZ"; // Hardcoded ID in database for the CPU opponent
        startGame();
    }

    private void startGame(){
        if (!humanOpponent || host) {
            TextView bestOfField = (TextView) findViewById(R.id.best_of);
            String bestOfString = bestOfField.getText().toString();

            try {
                bestOfNumber = Integer.parseInt(bestOfString);
            } catch (NumberFormatException nfe) {
                bestOfNumber = -1;
            }

            if (humanOpponent) {
                try {
                    state.getOut().write(bestOfNumber);
                } catch (IOException e) {
                    // Error sending bestOfNumber
                }
            }
            if (bestOfNumber < 1 || bestOfNumber > 16) {
                AlertDialog dialog = alertDialog.create();
                dialog.setTitle("Error Starting Game");
                dialog.setMessage("Error: please enter an integer number between 1 and 16 for the best-of number.");
                dialog.show();
                if (serverSocket != null) try {
                    serverSocket.close();
                    serverSocket = null;
                } catch (IOException e) {
                    // Unable to close socket
                }
                if (serverSocket != null) try {
                    clientSocket.close();
                    clientSocket = null;
                } catch (IOException e) {
                    // Unable to close socket
                }
                bestOfNumber = 0;
            } else {
                Intent i = new Intent(this, GameActivity.class);
                i.putExtra("bestOfNumber", bestOfNumber);
                i.putExtra("humanOpponent", humanOpponent);
                i.putExtra("player1Id", playerID);
                i.putExtra("player2Id", opponentID);
                i.putExtra("isHost", host);
                startActivity(i);
            }
        }
        else if (humanOpponent && !host) {
            try {
                bestOfNumber = state.getIn().read();
                if (bestOfNumber < 1 || bestOfNumber > 16) {
                    AlertDialog dialog = alertDialog.create();
                    dialog.setTitle("Error Starting Game");
                    dialog.setMessage("Error: game rules of game you were trying to join were invalid. Try again.");
                    dialog.show();
                    if (serverSocket != null) try {
                        serverSocket.close();
                        serverSocket = null;
                    } catch (IOException e) {
                        // Unable to close socket
                    }
                    if (serverSocket != null) try {
                        clientSocket.close();
                        clientSocket = null;
                    } catch (IOException e) {
                        // Unable to close socket
                    }
                } else {
                    Intent i = new Intent(this, GameActivity.class);
                    i.putExtra("bestOfNumber", bestOfNumber);
                    i.putExtra("humanOpponent", humanOpponent);
                    i.putExtra("player1Id", playerID);
                    i.putExtra("player2Id", opponentID);
                    i.putExtra("isHost", host);
                    startActivity(i);
                }
            }
            catch (IOException e) {
                // Error recieving bestOfNumber
            }
        }
    }
}
