package edu.osu.RPSEmpire.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.parse.ParseException;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import org.w3c.dom.Text;

import edu.osu.RPSEmpire.Objects.Game;
import edu.osu.RPSEmpire.Objects.Player;
import edu.osu.RPSEmpire.Objects.User;
import edu.osu.RPSEmpire.R;

public class GameSetupActivity extends AppCompatActivity {

    private int bestOfNumber;
    private Player opponent;
    private boolean humanOpponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_setup);
    }

    public void back(View view) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void multiPlayerGame(View view) {
        // TODO: launch multiplayer connection activity
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
