package edu.osu.RPSEmpire.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;

import edu.osu.RPSEmpire.Objects.User;
import edu.osu.RPSEmpire.R;

public class HomeActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {


    private GoogleApiClient mGoogleApiClient;
    private final int REQUEST_ACHIEVEMENTS = 1;

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
        startActivityForResult(Games.Achievements.getAchievementsIntent(mGoogleApiClient),
                REQUEST_ACHIEVEMENTS);
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
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


    @Override
    public void onStart(){
        super.onStart();
        Log.d("HomeActivity", "onStart Called");
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d("HomeActivity", "onResume Called");
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.d("HomeActivity", "onPause Called");
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.d("HomeActivity", "onStop Called");
    }

    @Override
    public void onRestart(){
        super.onRestart();
        Log.d("HomeActivity", "onRestart Called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("HomeActivity", "onDestroy Called");
    }

    // onclick functions
    public void play(View view) {
        Intent i = new Intent(this, GameSetupActivity.class);
        startActivity(i);
    }

    public void about(View view){
        Intent i = new Intent(this, AboutActivity.class);
        startActivity(i);
    }

    public void options(View view){
        Intent i = new Intent(this, OptionsActivity.class);
        startActivity(i);
    }

    public void statistics(View view){
        Intent i = new Intent(this, StatisticsActivity.class);
        startActivity(i);
    }

    public void achievements(View view){
        if (mGoogleApiClient.isConnected()){
            startActivityForResult(Games.Achievements.getAchievementsIntent(mGoogleApiClient),
                    REQUEST_ACHIEVEMENTS);
        }
        mGoogleApiClient.connect();

    }

    public void logOut(View view){
        User.logOut();
        finish();
    }
}
