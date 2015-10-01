package edu.osu.RPSEmpire.Objects;

import com.parse.ParseObject;
import com.parse.ParseClassName;

@ParseClassName("Player")

/**
 * Player
 *      Models either a computer of user controlled player
 */
public class Player extends ParseObject{

    // parse database keys
    private final String NICKNAME = "nickname";
    private final String IS_HUMAN = "is_human";
    private final String IS_RIGHT_HANDED = "is_right_handed";

    public Player (){
        // necessary empty constructor for subclassing parse objects
    }

    public Player( String nickname,
                   boolean isHuman,
                   boolean isRightHanded) {
        put(NICKNAME, nickname);
        put(IS_HUMAN, isHuman);
        put(IS_RIGHT_HANDED, isRightHanded);

    }

    public void saveToServer () {
        saveInBackground();
    }

    // getters/setters
    public void setNickname (String nickname) {
        put(NICKNAME, nickname);
    }
    public String getNickname () {
        return getString(NICKNAME);
    }
    public boolean isHuman () {
        return getBoolean(IS_HUMAN);
    }
    public boolean isRightHanded () {
        return getBoolean(IS_RIGHT_HANDED);
    }
}
