package edu.osu.RPSEmpire.Activities;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
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

import edu.osu.RPSEmpire.Objects.Player;
import edu.osu.RPSEmpire.Objects.User;
import edu.osu.RPSEmpire.R;

public class GameSetupActivity extends AppCompatActivity {

    AlertDialog.Builder alertDialog;
    private int bestOfNumber;
    private boolean humanOpponent;
    private UUID APP_UUID = UUID.fromString("361fe410-80d5-11e5-8bcf-feff819cdc9f");
    private BluetoothAdapter mBluetoothAdapter;
    private InputStream inStream;
    private OutputStream outStream;
    private String opponentID;
    private String playerID;
    private boolean host;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_setup);
    }

    @Override
    public void onStart(){
        super.onStart();

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
    public void onDestroy() {
        super.onDestroy();
        resetStreams();
    }

    public void back(View view) {
        finish();
    }

    private void resetStreams() {
        try {
            mBluetoothAdapter = null;
            humanOpponent = false;
            inStream.close();
            outStream.close();
            ParseActivity state = ((ParseActivity) getApplicationContext());
            state.setIn(null);
            state.setOut(null);
            host = false;
        } catch (IOException e) {
            // Failed to close streams
        }
    }

    private void connectedWithOpponent(BluetoothSocket opponent) {

        int error = 0;
        outStream = null;
        inStream = null;

        try {
            outStream = opponent.getOutputStream();
            try {
                outStream.write(playerID.getBytes("UTF-8"));
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
            inStream = opponent.getInputStream();
            try {
                byte[] buffer = new byte[playerID.length()];
                inStream.read(buffer);
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
            state.setIn(inStream);
            state.setOut(outStream);
            startGame();
        }
    }

    public void hostGame(View view) {

        // Enable Bluetooth
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, 0);
        }

        // Set up host connection
        try {
            BluetoothServerSocket serverSocket = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("RPS Empire", APP_UUID);
            AlertDialog dialog = alertDialog.create();
            dialog.setTitle("Waiting...");
            dialog.setMessage("Waiting for another device to connect...");
            dialog.show();
            try {
                host = true;
                connectedWithOpponent(serverSocket.accept());
            }
            catch (IOException e) {
                dialog = alertDialog.create();
                dialog.setTitle("Failed.");
                dialog.setMessage("Failed to connect with another device.");
                dialog.show();
            }
        }
        catch (IOException e) {
            AlertDialog dialog = alertDialog.create();
            dialog.setTitle("Failed.");
            dialog.setMessage("Failed to connect with the server to open host game.");
            dialog.show();
        }

    }

    public void joinGame(View view) {
        // Set up bluetooth
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, 0);
        }

        // Look to join host connection
        while (mBluetoothAdapter.getBondedDevices().size() == 0) {
            mBluetoothAdapter.startDiscovery();
        }
        Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();
        for (BluetoothDevice device : devices) {
            try {
                BluetoothSocket socket = device.createRfcommSocketToServiceRecord(APP_UUID);
                try {
                    mBluetoothAdapter.cancelDiscovery();
                    socket.connect();
                    host = false;
                    connectedWithOpponent(socket);
                } catch (IOException e) {
                    AlertDialog dialog = alertDialog.create();
                    dialog.setTitle("Connection Failed");
                    dialog.setMessage("Failed to join game.");
                    dialog.show();
                }
               }
            catch (IOException e) {
                AlertDialog dialog = alertDialog.create();
                dialog.setTitle("Connection Failed");
                dialog.setMessage("Failed to connect to host device.");
                dialog.show();
            }
        }
    }

    public void singlePlayerGame(View view) {
        // TODO: set up different AI player connections
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

            if (bestOfNumber < 1 || bestOfNumber > 16) {
                AlertDialog dialog = alertDialog.create();
                dialog.setTitle("Error Starting Game");
                dialog.setMessage("Error: please enter an integer number between 1 and 16 for the best-of number.");
                dialog.show();
                bestOfNumber = -1;
                resetStreams();
            } else {
                if (humanOpponent) {
                    try {
                        outStream.write(bestOfNumber);
                    } catch (IOException e) {
                        // Error sending bestOfNumber
                    }
                }
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
                bestOfNumber = inStream.read();
                Intent i = new Intent(this, GameActivity.class);
                i.putExtra("bestOfNumber", bestOfNumber);
                i.putExtra("humanOpponent", humanOpponent);
                i.putExtra("player1Id", playerID);
                i.putExtra("player2Id", opponentID);
                i.putExtra("isHost", host);
                startActivity(i);
            }
            catch (IOException e) {
                // Error recieving bestOfNumber
            }
        }
    }
}
