package edu.osu.RPSEmpire.Activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.parse.Parse;
import com.parse.ParseObject;

import edu.osu.RPSEmpire.LoginFragment;
import edu.osu.RPSEmpire.Objects.*;
import edu.osu.RPSEmpire.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("MainActivity","onCreate Called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // External Database connection
        ParseObject.registerSubclass(Game.class);
        ParseObject.registerSubclass(Round.class);
        ParseObject.registerSubclass(Turn.class);
        ParseObject.registerSubclass(Player.class);
        ParseObject.registerSubclass(User.class);
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "zJSMNb6JcF0qGmNZbOGfQFdbtEEY6VlW5WTxfJ02", "dg6OntuzxLWsj2TyFL2B1ERg0fCrA7EuaUnMsOHE");
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
        LoginFragment nextFrag= new LoginFragment();
        this.getFragmentManager().beginTransaction()
                .replace(R.layout.fragment_login, nextFrag)
                .addToBackStack(null)
                .commit();    }

    public void signUp(View view){
        Intent i = new Intent(this, SignUpActivity.class);
        startActivity(i);
    }

    public void onFragmentInteractionListener(Uri uri){

    }
}
