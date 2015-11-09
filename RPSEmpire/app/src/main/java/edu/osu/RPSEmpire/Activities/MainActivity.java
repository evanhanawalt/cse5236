package edu.osu.RPSEmpire.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.InputStream;

import com.parse.ParseUser;

import edu.osu.RPSEmpire.Objects.*;
import edu.osu.RPSEmpire.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("MainActivity","onCreate Called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    public void onStart(){
        super.onStart();
        Log.d("MainActivity", "onStart Called");
        if(User.getCurrentUser() != null){
            startHomeActivity();
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d("MainActivity", "onResume Called");
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.d("MainActivity", "onPause Called");
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.d("MainActivity", "onStop Called");
    }

    @Override
    public void onRestart(){
        super.onRestart();
        Log.d("MainActivity", "onRestart Called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("MainActivity", "onDestroy Called");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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



    public void login(View view){
        Intent i = new Intent(this, LogInActivity.class);
        startActivityForResult(i, User.LOGIN);
    }
    public void signUp(View view){
        Intent i = new Intent(this, SignUpActivity.class);
        startActivityForResult(i, User.SIGN_UP);
    }



    public void startHomeActivity(){
        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String activity="Log IN";
        switch ( requestCode ) {
            case User.SIGN_UP:
                activity="Sign Up";
            case User.LOGIN:

                if (resultCode == RESULT_OK ){
                    startHomeActivity();
                    Log.d("MainActivity", activity +" Returned with RESULT_OK");
                } else {
                    Log.d("MainActivity", activity +" Returned with RESULT_CANCELED");
                    Toast.makeText(MainActivity.this, "Sign Up Failed", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

}
