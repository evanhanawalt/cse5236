package com.tylor.evan.gamename123.Objects;

import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.parse.ParseException;

/**
 * User
 *      models User information
 */
public class User extends ParseUser {

    public User( String user_name,
                 String password,
                 String email,
                 String first_name,
                 String last_name,
                 String nickname,
                 int points,
                 boolean is_right_handed,
                 boolean is_human) {
        super();

        this.setUsername(user_name);
        this.setPassword(password);
        this.setEmail(email);
        this.put("first_name", first_name);
        this.put("last_name", last_name);
        this.put("nickname", nickname);
        this.put("point", points);
        this.put("is_right_handed", is_right_handed);
        this.put("is_human", is_human);


        this.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    // Hooray! Let them use the app now.
                } else {
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                }
            }
        });

    }
}
