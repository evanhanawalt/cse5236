package edu.osu.RPSEmpire.Objects;

import com.parse.ParseClassName;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.parse.ParseException;

import edu.osu.RPSEmpire.Activities.SignUpActivity;

@ParseClassName("_User")
/**
 * User
 *      models User information
 */
public class User extends ParseUser {

    // parse database keys
    private final String FIRST_NAME = "first_name";
    private final String LAST_NAME = "last_name";
    private final String POINTS = "points";
    private final String PLAYER_ID = "player_id";
    public static final int SIGN_UP = 1;
    public User () {
        // necessary empty constructor for subclassing parse objects
    }
    public User ( String firstName,
                  String lastName,
                  String userName,
                  String password,
                  String email,
                  int points) {

        setUsername(userName);
        setPassword(password);
        setEmail(email);
        put(FIRST_NAME, firstName);
        put(LAST_NAME, lastName);
        put(POINTS, points);
    }
    public User ( String firstName,
                  String lastName,
                  String userName,
                  String password,
                  String email,
                  int points,
                  String player) {

        setUsername(userName);
        setPassword(password);
        setEmail(email);
        put(FIRST_NAME, firstName);
        put(LAST_NAME, lastName);
        put(POINTS, points);
        put(PLAYER_ID, player);
    }

    public void setPlayer(String playerID) { put(PLAYER_ID, playerID); saveInBackground(); }
    public static User getCurrentUser(){
        return (User) ParseUser.getCurrentUser();
    }

    public void saveToServer () {
        this.saveInBackground();
    }
    public void saveToServer (SignUpCallback signupCallback) {
        this.signUpInBackground(signupCallback);
    }
    // getters/setters
    public String getFirstName () {
        return getString(FIRST_NAME);
    }
    public String getLastName () {
        return getString(LAST_NAME);
    }
    public int getPoints () {
        return getInt(POINTS);
    }
    public String getPlayerID () {
        return getString(PLAYER_ID);
    }
}
