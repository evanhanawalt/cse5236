package edu.osu.RPSEmpire.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.FileDescriptor;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.BaseGameUtils;
import com.parse.ParseUser;

import edu.osu.RPSEmpire.Objects.*;
import edu.osu.RPSEmpire.R;

public class MainActivity extends AppCompatActivity  implements
        View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {


    private static int RC_SIGN_IN = 9001;

    private boolean mResolvingConnectionFailure = false;
    private boolean mAutoStartSignInFlow = true;
    private boolean mSignInClicked = false;
    private GoogleApiClient mGoogleApiClient;
    // Google play services sign in stuff

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("MainActivity", "onConnectionFailed Called");

        CharSequence text = "Google Play connection Failed";
        Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
        toast.show();

        if (mResolvingConnectionFailure) {
            // Already resolving
            Log.d("MainActivity", "Already resolving");
            return;
        }
        Log.d("MainActivity", connectionResult.toString());
        // If the sign in button was clicked or if auto sign-in is enabled,
        // launch the sign-in flow
        if (mSignInClicked || mAutoStartSignInFlow) {
            mAutoStartSignInFlow = false;
            mSignInClicked = false;
            mResolvingConnectionFailure = true;
            Log.d("MainActivity", "launch the sign-in flow");
            if (connectionResult.hasResolution()) {

                Log.d("MainActivity", "Starting resolution for result of missed connection");
                try {
                    // !!!
                    connectionResult.startResolutionForResult(this, connectionResult.getErrorCode());
                } catch (IntentSender.SendIntentException e) {
                    mResolvingConnectionFailure = false;
                    mGoogleApiClient.connect();
                }
            }
            // Attempt to resolve the connection failure using BaseGameUtils.
            // "There was an issue with sign in, please try again later."
        }



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
        setContentView(R.layout.activity_main);
        if(User.getCurrentUser() != null){
            startHomeActivity();
        }
        // The player is signed in. Hide the sign-in button and allow the
        // player to proceed.
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("MainActivity", "onCreate Called");
        super.onCreate(savedInstanceState);

        // Create the Google Api Client with access to the Play Games services
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .build();

        setContentView(R.layout.google_sign_in);
        findViewById(R.id.sign_in_button).setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.sign_in_button) {
            // start the asynchronous sign in flow
            mSignInClicked = true;
            mGoogleApiClient.connect();
        }
    }



    @Override
    public void onStart(){
        super.onStart();
        Log.d("MainActivity", "onStart Called");
        mGoogleApiClient.connect();

    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d("MainActivity", "onResume Called");
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.d("MainActivity", "onPause Called");
    }

    @Override
    public void onStop(){
        super.onStop();
        mGoogleApiClient.disconnect();
        Log.d("MainActivity", "onStop Called");
    }

    @Override
    public void onRestart(){
        super.onRestart();
        Log.d("MainActivity", "onRestart Called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("MainActivity", "onDestroy Called");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    public void login(View view){
        Intent i = new Intent(this, LogInActivity.class);
        startActivityForResult(i, User.LOGIN);
    }
    public void signUp(View view){
        Intent i = new Intent(this, SignUpActivity.class);
        startActivityForResult(i, User.SIGN_UP);
    }



    public void startHomeActivity(){
        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String activity="Log IN";
        switch ( requestCode ) {
            case User.SIGN_UP:
                activity="Sign Up";
            case User.LOGIN:

                if (resultCode == RESULT_OK ){
                    startHomeActivity();
                 //   Log.d("MainActivity", activity +" Returned with RESULT_OK");
                } else {
                    Log.d("MainActivity", activity +" Returned with RESULT_CANCELED");
                   // Toast.makeText(MainActivity.this, "Sign Up Failed", Toast.LENGTH_SHORT).show();
                }
                break;
        }


        if (requestCode == RC_SIGN_IN) {
            mSignInClicked = false;
            mResolvingConnectionFailure = false;
            if (resultCode == RESULT_OK) {
                mGoogleApiClient.connect();
            } else {
                // Bring up an error dialog to alert the user that sign-in
                // failed. The R.string.signin_failure should reference an error
                // string in your strings.xml file that tells the user they
                Log.d("Main Activity", "Request code RC_SIGN_IN recieved");
            }
        }


    }

}
