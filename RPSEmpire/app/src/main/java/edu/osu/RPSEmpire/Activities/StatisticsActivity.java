package edu.osu.RPSEmpire.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import edu.osu.RPSEmpire.Objects.User;
import edu.osu.RPSEmpire.R;

public class StatisticsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        // EXAMPLE STATISTICS PULL

        final int totalGames = 0;/*

        ParseQuery<ParseObject> query = ParseObject.getQuery("Game");
        query.whereEqualTo("player_1_id", User.getCurrentUser().get("player_id"));
        query.getInBackground(User.getCurrentUser().getObjectId(), new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null) {

                } else {
                    // Problem fetching user's player object
                }
            }
        });

        TextView textField = (TextView) findViewById(R.id.stat_text);
        textField.setText("Number of games played: " + Integer.ToString(totalGames));*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_statistics, menu);
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
}
