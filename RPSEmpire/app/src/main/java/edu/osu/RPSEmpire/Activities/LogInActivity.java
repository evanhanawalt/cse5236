package edu.osu.RPSEmpire.Activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.List;

import edu.osu.RPSEmpire.Objects.Player;
import edu.osu.RPSEmpire.Objects.User;
import edu.osu.RPSEmpire.R;

public class LogInActivity extends AppCompatActivity {


    AlertDialog.Builder alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        // Setup Alert Dialogues
        alertDialog = new AlertDialog.Builder(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
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

    public void logIn(View view){

        TextView userNameField = (TextView) findViewById(R.id.user_name);
        TextView passwordField = (TextView) findViewById(R.id.password);
        final String password = passwordField.getText().toString();
        final String userName = userNameField.getText().toString();

        try {
            ParseUser.logIn(userName, password);
            finish();
        } catch (ParseException e) {
            AlertDialog dialog = alertDialog.create();
            dialog.setTitle("Error Logging In");
            dialog.setMessage("Error: "+e.getMessage()+".");
            dialog.show();
        }

    }
}
