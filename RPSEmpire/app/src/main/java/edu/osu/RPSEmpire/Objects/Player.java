package edu.osu.RPSEmpire.Objects;

import com.parse.ParseObject;
import com.parse.ParseClassName;

@ParseClassName("Player")

/**
 * Created by e on 10/1/15.
 */
public class Player extends ParseObject{

    // parse database keys
    private final String NICKNAME = "nickname";
    private final String IS_HUMAN = "is_human";

    public Player (){
        // necessary empty constructor for subclassing parse objects
    }

    public Player( String nickname,
                   boolean is_human ) {
        put(NICKNAME, nickname);
        put(IS_HUMAN, is_human);
    }

    public void saveToServer () {
        saveInBackground();
    }

    public void setNickname (String nickname) {
        put(NICKNAME, nickname);
    }
    // getter methods
    public String getNickname () {
        return getString(NICKNAME);
    }
    public boolean isHuman () {
        return getBoolean(IS_HUMAN);
    }
}
