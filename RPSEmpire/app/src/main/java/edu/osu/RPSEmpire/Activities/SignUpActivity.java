package edu.osu.RPSEmpire.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import org.w3c.dom.Text;

import java.util.List;

import edu.osu.RPSEmpire.Objects.Player;
import edu.osu.RPSEmpire.Objects.User;
import edu.osu.RPSEmpire.R;

public class SignUpActivity extends AppCompatActivity {

    AlertDialog.Builder alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
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

    public void signUp(View view){

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
        final String confirmPassword = confirmPasswordField.getText().toString();
        final String email = emailField.getText().toString();
        final String userName = nicknameField.getText().toString();
        final boolean isRightHanded = isRightHandedField.isChecked();
        final int points = 0;

        if (password.compareTo("") == 0) {
            // password is a required field
            AlertDialog dialog = alertDialog.create();
            dialog.setTitle("Error Signing Up");
            dialog.setMessage("Error: the password field cannot be left empty.");
            dialog.show();
        }
        else if (confirmPassword.compareTo("") == 0) {
            // confirm password is a required field
            AlertDialog dialog = alertDialog.create();
            dialog.setTitle("Error Signing Up");
            dialog.setMessage("Error: the confirm password field cannot be left empty.");
            dialog.show();
        }
        else if (password.compareTo(confirmPassword) != 0) {
            // password fields must match
            AlertDialog dialog = alertDialog.create();
            dialog.setTitle("Error Signing Up");
            dialog.setMessage("Error: the password field's content much match the confirm password field's content.");
            dialog.show();
        }
        else if (userName.compareTo("") == 0) {
            // userName is a required field
            AlertDialog dialog = alertDialog.create();
            dialog.setTitle("Error Signing Up");
            dialog.setMessage("Error: the user name field cannot be left empty.");
            dialog.show();
        }
        else if (email.compareTo("") == 0) {
            // userName is a required field
            AlertDialog dialog = alertDialog.create();
            dialog.setTitle("Error Signing Up");
            dialog.setMessage("Error: the email name field cannot be left empty.");
            dialog.show();
        }
        else {
            final User newUser = new User(firstName, lastName, userName, password, email, points);
            newUser.saveToServer(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        // Create new player object and provide user with information
                        final Player newPlayer = new Player(userName, true, isRightHanded);
                        newPlayer.saveToServer(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    newUser.setPlayer(newPlayer.getObjectId());
                                    try {
                                        ParseUser.logIn(userName, password);
                                        finish();
                                    }
                                    catch (ParseException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                            }
                        });
                    } else {
                        // Display error message explaining to user why sign up failed
                        AlertDialog dialog = alertDialog.create();
                        dialog.setTitle("Error Signing Up");
                        dialog.setMessage("Error: " + e.getMessage() + ".");
                        dialog.show();
                    }
                }
            });
        }
    }
}
