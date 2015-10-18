package edu.osu.RPSEmpire.Activities;

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

import edu.osu.RPSEmpire.Objects.Player;
import edu.osu.RPSEmpire.Objects.User;
import edu.osu.RPSEmpire.R;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
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

    public void signUp(View view){

        Log.d("Sign Up", "Sign up button pressed");
        TextView firstNameField = (TextView) findViewById(R.id.first_name);
        TextView lastNameField = (TextView) findViewById(R.id.last_name);
        TextView nicknameField = (TextView) findViewById(R.id.nickname);
        TextView emailField = (TextView) findViewById(R.id.email);
        TextView passwordField = (TextView) findViewById(R.id.password);
        TextView confirmPasswordField = (TextView) findViewById(R.id.confirm_password);
        ToggleButton isRightHandedField = (ToggleButton) findViewById(R.id.is_right_handed);
        final String firstName = firstNameField.getText().toString();
        final String lastName = lastNameField.getText().toString();
        final String password = passwordField.getText().toString();
        final String email = emailField.getText().toString();
        final String userName = email;
        final int points = 0;
        final Player newPlayer = new Player(nicknameField.getText().toString(), true, isRightHandedField.isChecked());
        newPlayer.saveToServer(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Log.d("Sign Up", "Saved Player object to Server");
                User newUser = new User(firstName, lastName, userName, password, email, points, newPlayer);
                newUser.saveToServer(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        Log.d("Sign up", "Signed up user");
                    }
                });
            }
        });

    }
}
