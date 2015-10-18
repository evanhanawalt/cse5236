package edu.osu.RPSEmpire.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
        // External Database connection
        ParseObject.registerSubclass(Game.class);
        ParseObject.registerSubclass(Round.class);
        ParseObject.registerSubclass(Turn.class);
        ParseObject.registerSubclass(Player.class);
        ParseObject.registerSubclass(User.class);
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "xKh6HwE6AZr7lUz9Aq9rAuSdlSa2HhtNgnpZZDXn", "dWf4dOcqTAqXyPs4waIJOVFIYZQJQWzXDRA7kReo");
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


    public void login(){

    }

    public void signUp(){

    }
}
