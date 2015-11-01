package edu.osu.RPSEmpire.Objects;

import com.parse.ParseObject;
import com.parse.ParseClassName;
import com.parse.SaveCallback;

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

    // player variables
    private String nickname;
    private boolean isHuman;
    private boolean isRightHanded;


    public Player () {
        // necessary empty constructor for subclassing parse objects
    }

    public Player( String givenNickname,
                   boolean givenIsHuman,
                   boolean givenIsRightHanded) {

        nickname = givenNickname;
        isHuman = givenIsHuman;
        isRightHanded = givenIsRightHanded;

        put(NICKNAME, nickname);
        put(IS_HUMAN, isHuman);
        put(IS_RIGHT_HANDED, isRightHanded);

    }

    public void saveToServer () {
        this.saveInBackground();
    }
    public void saveToServer (SaveCallback saveCallback) {
        this.saveInBackground(saveCallback);
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
