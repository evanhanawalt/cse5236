package edu.osu.RPSEmpire.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.parse.Parse;
import com.parse.ParseObject;

import edu.osu.RPSEmpire.Objects.*;
import edu.osu.RPSEmpire.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("MainActivity","onCreate Called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    public void onStart(){
        super.onStart();
        Log.d("MainActivity", "onStart Called");
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
        // TODO: implement login
        // JUST FOR DEBUGGING:
        Intent i = new Intent(this, GameSetupActivity.class);
        startActivity(i);
    }

    public void signUp(View view){
        Intent i = new Intent(this, SignUpActivity.class);
        startActivity(i);
    }
}
