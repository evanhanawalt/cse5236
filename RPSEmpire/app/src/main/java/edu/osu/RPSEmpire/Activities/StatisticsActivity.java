package edu.osu.RPSEmpire.Activities;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.osu.RPSEmpire.Objects.Game;
import edu.osu.RPSEmpire.Objects.Round;
import edu.osu.RPSEmpire.Objects.Turn;
import edu.osu.RPSEmpire.Objects.User;
import edu.osu.RPSEmpire.R;

public class StatisticsActivity extends AppCompatActivity {

    private ProgressBar spinner;
    private class StatisticsTask extends AsyncTask<String, Void, String>{
        List<Game> player1Games;
        List<Game> player2Games;
        List<Round> player1Rounds;
        List<Round> player2Rounds;
        List<Turn> player1Turns;
        List<Turn> player2Turns;

        int mWonGames = 0;
        int mLostGames = 0;

        int mWonTurns = 0;
        int mLostTurns = 0;
        int mTiedTurns = 0;

        int mWonRock = 0;
        int mLostRock = 0;
        int mTiedRock = 0;

        int mWonPaper = 0;
        int mLostPaper = 0;
        int mTiedPaper = 0;

        int mWonScissors = 0;
        int mLostScissors = 0;
        int mTiedScissors = 0;

        private void calculatedFromLocalData() {
            int wonGames = 0;
            int lostGames = 0;
            int wonTurns = 0;
            int lostTurns = 0;
            int tiedTurns = 0;

            int wonRock = 0;
            int lostRock = 0;
            int tiedRock = 0;

            int wonPaper = 0;
            int lostPaper = 0;
            int tiedPaper = 0;

            int wonScissors = 0;
            int lostScissors = 0;
            int tiedScissors = 0;
            Log.d("StatisticsActivity", "Calculating Statistics");
            // Stats from games where player is player 1
            for (Game player1Game : player1Games) {
                Log.d("StatisticsActivity", "Calculating Statistics for game " + player1Game.getObjectId());
                int bestOf = player1Game.getBestOfNumber();
                int numWon = 0;
                boolean wonOnQuit = false;
                ParseQuery<Round> rounds = new ParseQuery<Round>(Round.class);
                rounds.whereEqualTo("game_id", player1Game.getObjectId());

                try {
                    for (Round round : rounds.fromLocalDatastore().find()) {
                        boolean roundWon = false;
                        Log.d("StatisticsActivity", "Calculating Statistics for round " + round.getObjectId());
                        ParseQuery<Turn> turns = new ParseQuery<Turn>(Turn.class);
                        turns.whereEqualTo("round_id", round.getObjectId());

                        for (Turn turn : turns.fromLocalDatastore().find()) {
                            Log.d("StatisticsActivity", "Calculating Statistics for turn " + turn.getObjectId());
                            int oppMove = turn.getPlayer2Move();
                            int yourMove = turn.getPlayer1Move();

                            if (yourMove==-1 || oppMove ==-1) {
                                if (yourMove != -1){
                                    roundWon = true;
                                    wonOnQuit = true;
                                }
                            } else if (oppMove == yourMove){
                                //tie
                                switch(yourMove){
                                    case 0:
                                        tiedRock++;
                                        break;
                                    case 1:
                                        tiedPaper++;
                                        break;
                                    case 2:
                                        tiedScissors++;
                                        break;
                                }
                            } else  {
                                switch(yourMove){
                                    case 0:
                                        if(oppMove == 2){
                                            wonRock++;
                                            roundWon = true;
                                        } else {
                                            lostRock++;
                                        }
                                        break;
                                    case 1:
                                        if(oppMove == 0){
                                            wonPaper++;
                                            roundWon = true;
                                        } else {
                                            lostPaper++;
                                        }
                                        break;
                                    case 2:

                                        if(oppMove == 1){
                                            wonScissors++;
                                            roundWon = true;
                                        } else {
                                            lostScissors++;
                                        }
                                        break;
                                }
                            }
                        }
                        if (roundWon){
                            numWon++;
                        }

                    }
                    Log.d("StatisticsActivity", "game " + player1Game.getObjectId() + " won= "+  Boolean.toString(bestOf == numWon || wonOnQuit));
                    if(bestOf <= numWon || wonOnQuit){
                        wonGames++;
                    } else {
                        lostGames++;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            // Stats from games where player is player 1
            for (Game player2Game : player2Games) {
                Log.d("StatisticsActivity", "Calculating Statistics for game " + player2Game.getObjectId());
                int bestOf = player2Game.getBestOfNumber();
                int numWon = 0;
                boolean wonOnQuit = false;
                ParseQuery<Round> rounds = new ParseQuery<Round>(Round.class);
                rounds.whereEqualTo("game_id", player2Game.getObjectId());

                try {
                    for (Round round : rounds.fromLocalDatastore().find()) {
                        Log.d("StatisticsActivity", "Calculating Statistics for round " + round.getObjectId());
                        boolean roundWon = false;

                        ParseQuery<Turn> turns = new ParseQuery<Turn>(Turn.class);
                        turns.whereEqualTo("round_id", round.getObjectId());

                        for (Turn turn : turns.fromLocalDatastore().find()) {
                            Log.d("StatisticsActivity", "Calculating Statistics for turn " + turn.getObjectId());
                            int oppMove = turn.getPlayer2Move();
                            int yourMove = turn.getPlayer1Move();

                            if (yourMove==-1 || oppMove ==-1) {
                                if (yourMove != -1){
                                    roundWon = true;
                                    wonOnQuit = true;
                                }
                            } else if (oppMove == yourMove){
                                //tie
                                switch(yourMove){
                                    case 0:
                                        tiedRock++;
                                        break;
                                    case 1:
                                        tiedPaper++;
                                        break;
                                    case 2:
                                        tiedScissors++;
                                        break;
                                }
                            } else  {
                                switch(yourMove){
                                    case 0:
                                        if(oppMove == 2){
                                            wonRock++;
                                            roundWon = true;
                                        } else {
                                            lostRock++;
                                        }
                                        break;
                                    case 1:
                                        if(oppMove == 0){
                                            wonPaper++;
                                            roundWon = true;
                                        } else {
                                            lostPaper++;
                                        }
                                        break;
                                    case 2:

                                        if(oppMove == 1){
                                            wonScissors++;
                                            roundWon = true;
                                        } else {
                                            lostScissors++;
                                        }
                                        break;
                                }
                            }
                        }
                        if (roundWon){
                            numWon++;
                        }

                    }
                    Log.d("StatisticsActivity", "game " + player2Game.getObjectId() + " won= "+ Boolean.toString(bestOf == numWon || wonOnQuit));
                    if(bestOf <= numWon || wonOnQuit){
                        wonGames++;
                    } else {
                        lostGames++;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            mWonGames = wonGames;
            mLostGames = lostGames;
            mWonRock = wonRock;
            mLostRock = lostRock;
            mTiedRock = tiedRock;
            mWonPaper = wonPaper;
            mLostPaper = lostPaper;
            mTiedPaper = tiedPaper;
            mWonScissors = wonScissors;
            mLostScissors = lostScissors;
            mTiedScissors = tiedScissors;

        }



        private String buildStringOutput() {
            String output = "Games Played: " + Integer.toString(mLostGames + mWonGames) + "\n" +
                    "Games Won: " + mWonGames + "\n" +
                    "Games Lost: " + mLostGames + "\n" +
                    "Rounds\n" +
                    "Rock Wins: " + mWonRock + "\n" +
                    "Rock Losses: " + mLostRock + "\n" +
                    "Rock Ties: " + mTiedRock + "\n" +
                    "Paper Wins: " + mWonPaper + "\n" +
                    "Paper Losses: " + mLostPaper + "\n" +
                    "Paper Ties: " + mTiedPaper + "\n" +
                    "Scissors Wins: " + mWonScissors + "\n" +
                    "Scissors Losses: " + mLostScissors + "\n" +
                    "Scissors Ties: " + mTiedScissors + "\n";
            Log.d("Statistical Output", output);
            return output;
        }

        private Set<String> getGameIds(List<Game> games){
            Set<String> ids = new HashSet<String>();
            for(int i = 0 ; i < games.size() ; i ++ ){
                ids.add(games.get(i).getObjectId());
            }
            return ids;
        }


        private Set<String> getRoundIds(List<Round> rounds){
            Set<String> ids = new HashSet<String>();
            for(int i = 0 ; i < rounds.size() ; i ++ ){
                ids.add(rounds.get(i).getObjectId());
            }
            return ids;
        }


        @Override
        protected String doInBackground(String... params) {
            ParseQuery<Game> player1GamesQuery = ParseQuery.getQuery(Game.class);
            ParseQuery<Game> player2GamesQuery = ParseQuery.getQuery("Game");

            // get games
            player1GamesQuery.whereEqualTo("player_1_id", User.getCurrentUser().get("player_id"));
            player2GamesQuery.whereEqualTo("player_2_id", User.getCurrentUser().get("player_id"));
            try {
                Log.d("StatisticsActivity", "Querying for games");
                player1Games = player1GamesQuery.find();
                player2Games = player2GamesQuery.find();
            } catch (ParseException e) {
                e.printStackTrace();
            }


            // get rounds
            ParseQuery<Round> player1RoundsQuery = ParseQuery.getQuery(Round.class);
            ParseQuery<Round> player2RoundsQuery = ParseQuery.getQuery(Round.class);

            player1RoundsQuery.whereContainedIn("game_id", getGameIds(player1Games));
            player2RoundsQuery.whereContainedIn("game_id", getGameIds(player2Games));

            try {
                Log.d("StatisticsActivity", "Querying for rounds");
                player1Rounds = player1RoundsQuery.find();
                player2Rounds = player2RoundsQuery.find();
            } catch (ParseException e){
                e.printStackTrace();
            }

            // get turns
            ParseQuery<Turn> player1TurnsQuery = ParseQuery.getQuery(Turn.class);
            ParseQuery<Turn> player2TurnsQuery = ParseQuery.getQuery(Turn.class);
            player1TurnsQuery.whereContainedIn("round_id", getRoundIds(player1Rounds));
            player2TurnsQuery.whereContainedIn("round_id", getRoundIds(player2Rounds));

            try {
                Log.d("StatisticsActivity", "Querying for turns");
                player1Turns = player1TurnsQuery.find();
                player2Turns = player2TurnsQuery.find();
            } catch (ParseException e){
                e.printStackTrace();
            }
            // make sure all are saved to local datastore
            try {
                ParseObject.pinAll(player1Games);
                ParseObject.pinAll(player2Games);
                ParseObject.pinAll(player1Rounds);
                ParseObject.pinAll(player2Rounds);
                ParseObject.pinAll(player1Turns);
                ParseObject.pinAll(player2Turns);

            } catch (ParseException e) {
                e.printStackTrace();
            }


            calculatedFromLocalData();

            return buildStringOutput();

        }



        @Override
        protected void onPostExecute(String result) {
            Log.d("StatisticsActivity", "onPostExecute called");

            TextView textField = (TextView) findViewById(R.id.stat_text);
            textField.setText(result);
            spinner.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        spinner.setVisibility(View.VISIBLE);
        new StatisticsTask().execute(User.getCurrentUser().getPlayerID());

    }

    private int getTotalGames() {

        int player1Games = 0;
        int player2Games = 0;

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Game");
        query.whereEqualTo("player_1_id", User.getCurrentUser().get("player_id"));
        try {
            player1Games = query.find().size();
        }
        catch (ParseException e){
            // error connecting to server
        }
        query = ParseQuery.getQuery("Game");
        query.whereEqualTo("player_2_id", User.getCurrentUser().get("player_id"));
        try {
            player2Games = query.find().size();
        }
        catch (ParseException e){
            // error connecting to server
        }

        return player1Games+player2Games;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_statistics, menu);
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
}
