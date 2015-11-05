package edu.osu.RPSEmpire.Activities;

import android.app.Application;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;

import com.parse.Parse;
import com.parse.ParseObject;

import java.io.InputStream;
import java.io.OutputStream;

import edu.osu.RPSEmpire.Objects.Game;
import edu.osu.RPSEmpire.Objects.Player;
import edu.osu.RPSEmpire.Objects.Round;
import edu.osu.RPSEmpire.Objects.Turn;
import edu.osu.RPSEmpire.Objects.User;

/**
 * Created by e on 10/21/15.
 */
public class ParseActivity extends Application {

    public String test = "Test";
    private InputStream in;
    private OutputStream out;

    public void setIn(InputStream input) { in = input; }
    public void setOut(OutputStream output) { out = output; }
    public InputStream getIn() { return in; }
    public OutputStream getOut() { return out; }

    @Override
    public void onCreate() {
        super.onCreate();
        // External Database connection
        ParseObject.registerSubclass(Game.class);
        ParseObject.registerSubclass(Round.class);
        ParseObject.registerSubclass(Turn.class);
        ParseObject.registerSubclass(Player.class);
        ParseObject.registerSubclass(User.class);
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "zJSMNb6JcF0qGmNZbOGfQFdbtEEY6VlW5WTxfJ02", "dg6OntuzxLWsj2TyFL2B1ERg0fCrA7EuaUnMsOHE");
    }
}
