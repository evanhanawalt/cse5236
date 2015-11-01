package edu.osu.RPSEmpire.Activities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.parse.Parse;
import com.parse.ParseUser;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import edu.osu.RPSEmpire.Objects.Player;
import edu.osu.RPSEmpire.Objects.User;
import edu.osu.RPSEmpire.R;

public class GameSetupActivity extends AppCompatActivity {

    private int bestOfNumber;
    private Player opponent;
    private boolean humanOpponent;
    private int REQUEST_ENABLE_BT = 1;
    private UUID APP_UUID = UUID.fromString("361fe410-80d5-11e5-8bcf-feff819cdc9f");
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothSocket mSocket;
    private InputStream inStream;
    private OutputStream outStream;
    private String opponentID;


    private final BroadcastReceiver mBluetoothReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action){
                case BluetoothDevice.ACTION_FOUND:
                    // Attempt to create a socket on App UUID
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    BluetoothSocket tmp;
                    try {
                        tmp = device.createRfcommSocketToServiceRecord(APP_UUID);
                        tmp.connect();
                        Log.e("Socket", "Socket connected: " + Boolean.toString(tmp.isConnected()));
                        mBluetoothAdapter.cancelDiscovery();
                    } catch (IOException e) {
                    // Could not connect TODO: Handle this exception
                        Log.e("Socket", e.getMessage());
                        return;
                    }
                    mSocket = tmp;
                    new HostConnectionThread(mSocket).start();
                    break;
            }
        }
    };





    private class ClientConnectionThread extends Thread {
        private final BluetoothServerSocket mmServerSocket;

        public ClientConnectionThread() {
            // Use a temporary object that is later assigned to mmServerSocket,
            // because mmServerSocket is final
            BluetoothServerSocket tmp = null;
            try {
                // MY_UUID is the app's UUID string, also used by the client code
                tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("RPS EMPIRE", APP_UUID);
            } catch (IOException e) { }
            mmServerSocket = tmp;
        }

        public void run() {
            BluetoothSocket socket = null;
            final InputStream inStream;
            final OutputStream outStream;

            // Keep listening until exception occurs or a socket is returned
            while (true) {
                byte[] buffer = new byte[1024];  // buffer store for the stream
                int bytes; // bytes returned from read()

                try {
                    socket = mmServerSocket.accept();
                } catch (IOException e) {
                    break;
                }
                // If a connection was accepted
                if (socket != null) {
                    // Do work to manage the connection (in a separate thread)
                    try {
                        mmServerSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        outStream = socket.getOutputStream();
                        inStream = socket.getInputStream();
                        outStream.write(User.getCurrentUser().getPlayerID().getBytes());
                        bytes = inStream.read(buffer);
                        String str = buffer.toString();
                        opponentID = str;
                        // when opponent id is read from client, that is signal the game should start
                        Log.d("Client Connection", "opponent read as  " + opponentID);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }

        /** Will cancel the listening socket, and cause the thread to finish */
        public void cancel() {
            try {
                mmServerSocket.close();
            } catch (IOException e) { }
        }
    }


    private class HostConnectionThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public HostConnectionThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);
                    String str = new String(buffer);
                    // Set Opponent ID to message from client
                    opponentID = str;
                    Log.d("Host Connection", "opponent read as  " + opponentID);
                    // send player id from host, (signal to start the game
                    write(User.getCurrentUser().getPlayerID().getBytes());
                } catch (IOException e) {
                    break;
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) { }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_setup);
    }

    @Override
    public void onStart(){
        super.onStart();
        Log.d("GameSetupActivity", "onStart Called");
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d("GameSetupActivity", "onResume Called");
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mBluetoothReceiver, filter);
    }
    @Override
    public void onPause(){
        super.onPause();
        Log.d("GameSetupActivity", "onPause Called");
        unregisterReceiver(mBluetoothReceiver);
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.d("GameSetupActivity", "onStop Called");
    }

    @Override
    public void onRestart(){
        super.onRestart();
        Log.d("GameSetupActivity", "onRestart Called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("GameSetupActivity", "onDestroy Called");
    }
    public void back(View view) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void multiPlayerGame(View view) {
        // TODO: launch multiplayer connection activity
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }
        mBluetoothAdapter.startDiscovery();
    }

    public void singlePlayerGame(View view) {
        // TODO: set up different AI player connections
        humanOpponent = false;
        startGame(view);
    }

    private void startGame(View view){
        TextView bestOfField = (TextView) findViewById(R.id.best_of);
        String bestOfString = bestOfField.getText().toString();

        try {
            bestOfNumber = Integer.parseInt(bestOfString);
        }
        catch(NumberFormatException nfe) {
            // TODO: Diplay error message
            bestOfNumber = -1;
        }

        if (bestOfNumber < 1 || bestOfNumber > 16) {
            // TODO: Diplay beyond min/max number of games error message
            bestOfNumber = -1;
        }
        else {
            Intent i = new Intent(this, GameActivity.class);
            i.putExtra("bestOfNumber", bestOfNumber);
            i.putExtra("humanOpponent", humanOpponent);
            startActivity(i);
        }
    }
}
