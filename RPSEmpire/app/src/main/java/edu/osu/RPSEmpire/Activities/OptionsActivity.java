package edu.osu.RPSEmpire.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ToggleButton;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.List;

import edu.osu.RPSEmpire.Objects.Player;
import edu.osu.RPSEmpire.Objects.User;
import edu.osu.RPSEmpire.R;

public class OptionsActivity extends AppCompatActivity {
    private Player mPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Player");
        query.whereEqualTo("objectId", User.getCurrentUser().get("player_id"));
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                mPlayer = (Player) list.get(0);
                Log.d("OptionsActivity", "player receieved:" + mPlayer.getObjectId());
                ToggleButton toggle = (ToggleButton) findViewById(R.id.is_right_handed);
                Log.d("OptionsActivity", "player is right handed: " + mPlayer.isRightHanded());
                toggle.setChecked(mPlayer.isRightHanded());
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_options, menu);
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

    public void update(View view){
        Log.d("OptionsActivity", "update clicked");
        ToggleButton toggle = (ToggleButton) findViewById(R.id.is_right_handed);

        mPlayer.setIsRightHanded(toggle.isChecked());

        mPlayer.saveToServer(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Log.d("OptionsActivity", "update done");
                finish();
            }
        });
    }
}
