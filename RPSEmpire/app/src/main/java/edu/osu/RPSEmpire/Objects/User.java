package edu.osu.RPSEmpire.Objects;

import com.parse.ParseClassName;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.parse.ParseException;

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

    public User( String first_name,
                 String last_name,
                 String user_name,
                 String password,
                 String email,
                 int points,
                 String player_id) {

        setUsername(user_name);
        setPassword(password);
        setEmail(email);
        put(FIRST_NAME, first_name);
        put(LAST_NAME, last_name);
        put(POINTS, points);
        put(PLAYER_ID, player_id);
    }

    public void saveToServer () {
        saveInBackground();
    }

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
