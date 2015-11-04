package edu.osu.RPSEmpire.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import edu.osu.RPSEmpire.R;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
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
        Intent i = new Intent(this, AchievementsActivity.class);
        startActivity(i);
    }


}
