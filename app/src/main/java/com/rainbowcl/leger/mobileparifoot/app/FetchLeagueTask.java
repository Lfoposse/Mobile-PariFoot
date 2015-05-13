package com.rainbowcl.leger.mobileparifoot.app;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import com.rainbowcl.leger.mobileparifoot.app.data.PariFootContract;
import com.rainbowcl.leger.mobileparifoot.app.model.Team;
import com.rainbowcl.leger.mobileparifoot.app.data.Utility;


import java.io.IOException;
import java.util.ArrayList;

import static junit.framework.Assert.assertTrue;

public class FetchLeagueTask extends AsyncTask<String, Void,  String[]> {

    private final String LOG_TAG = FetchLeagueTask.class.getSimpleName();

    private ArrayAdapter<String> mSoccerAdapter;
    private final Context mContext;

    public FetchLeagueTask(Context context, ArrayAdapter<String> soccerAdapter) {
              mContext = context;
              mSoccerAdapter = soccerAdapter;
    }

    // These are the names of the JSON objects that need to be extracted.
    final String LINKS = "_links";
    final String TEAMS_LINKS = "teams";
    final String FIXTURES_LINKS = "fixtures";
    final String TABLE_LINKS = "leagueTable";
    final String HREF = "href";
    final String STANDING = "standing";
    final String TEAM_NAME = "teamName";
    final String LEAGUE_CAPTION = "leagueCaption";
    final String CAPTION = "caption";
    final String GOALDIFF = "goalDifference";

    // used for db
    final String LEAGUE = "league";
    final String TEAMS = "teams";
    final String MATCHDAY = "matchday";
    final String POSITION = "position";
    final String POINTS = "points";
    final String LOGO_LINKS = "logo";

    final String HOME_TEAM = "homeTeam";
    final String AWAY_TEAM = "awayTeam";
    final String STATUS = "status";
    final String HOME_GOALS = "homeGoals";
    final String AWAY_GOALS = "awayGoals";







    private void getFixturesDataFromJson(String fixturesJsonStr)
            throws JSONException {

    }

    private String[] getSoccerDataFromJson(String soccerJsonStr)
            throws JSONException {

        JSONArray soccerArray = new JSONArray(soccerJsonStr);


        String[] resultStrs = new String[soccerArray.length()];
        PariFootContract.teams_soccer_url = new ArrayList(soccerArray.length());
        PariFootContract.soccer_seasons = new ArrayList(soccerArray.length());
        for(int i = 0; i < soccerArray.length(); i++) {

            JSONObject seasonJson = soccerArray.getJSONObject(i);

            String caption =  seasonJson.getString(CAPTION);

            resultStrs[i] = caption;

           /* JSONObject links =  seasonJson.getJSONObject(LINKS);

*
            JSONObject fixturesLinks=  links.getJSONObject(FIXTURES_LINKS);
            String fixturesLinksStr =  fixturesLinks.getString(HREF);

            JSONObject leagueTableLinks=  links.getJSONObject(TABLE_LINKS);
            String leagueTableLinksStr =  leagueTableLinks.getString(HREF);
            PariFootContract.teams_soccer_url.add(leagueTableLinksStr);
            PariFootContract.soccer_seasons.add(seasonJson.getString(CAPTION));

           // getLeagueTableDataFromJson(leagueTableLinksStr);
           // getFixturesDataFromJson(fixturesLinksStr);
           */
        }


        for (String s : resultStrs) {
            Log.v(LOG_TAG, "League entry: " + s);
        }
        return resultStrs;

    }
    private void getLeagueTableDataFromJson(String leagueTableJsonStr)
            throws JSONException {

        JSONObject jsonObject = new JSONObject(Utility.getJsonStringWithUrl(leagueTableJsonStr));
        JSONArray standingArray = jsonObject.getJSONArray("ranking");

        // JSONArray standingArray = new JSONArray(leagueTableJsonStr);

        String[] resultStrs = new String[standingArray.length()];
        for(int i = 0; i < standingArray.length(); i++) {

            JSONObject teamJson = standingArray.getJSONObject(i);

            Team team = new Team(mContext);

            team.setLeague(jsonObject.getString(LEAGUE_CAPTION));
            team.setName(teamJson.getString(TEAM_NAME));
            team.setLogo_path(" ");
            team.setMatchday(jsonObject.getInt(MATCHDAY));
            team.setPosition(teamJson.getInt(POSITION));
            team.setPoints(teamJson.getInt(POINTS));
            team.setGoalDifference(teamJson.getInt(GOALDIFF));

            long teamRowId = team.add();

            resultStrs[i] = team.getName();

             resultStrs[i] = teamJson.getString("team");

           /*// Verify we got a row back.
            assertTrue(teamRowId != -1);
            Log.d(LOG_TAG, "New row teeemmmmmm id: " + teamRowId);*/

            for (String s : resultStrs) {
                Log.v(LOG_TAG, "team entry: " + s);
            }
        }
        //return resultStrs;


    }
    @Override
    protected String[] doInBackground(String... params) {

        // If there's no zip code, there's nothing to look up.  Verify size of params.
        if (params.length == 0) {
            return null;
        }

        /*/////
        String leagueJsonStr = null;
        try {
            leagueJsonStr = Utility.sendGetRequest("http://api.football-data.org/alpha/soccerseasons");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            getLeagueTableDataFromJson(leagueJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        /////////*/

        String soccerJsonStr = Utility.getJsonStringWithUrl("http://api.football-data.org/alpha/soccerseasons");

        try {
            return getSoccerDataFromJson(soccerJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        // This will only happen if there was an error getting or parsing the forecast.
        return null;
    }

    @Override
    protected void onPostExecute(String[] result) {
        if (result != null) {
            mSoccerAdapter.clear();
            for(String teamStr : result) {
                mSoccerAdapter.add(teamStr);
            }
            // New data is back from the server.  Hooray!
        }
    }
}
