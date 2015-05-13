package com.rainbowcl.leger.mobileparifoot.app;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.rainbowcl.leger.mobileparifoot.app.data.PariFootContract;
import com.rainbowcl.leger.mobileparifoot.app.data.Utility;
import com.rainbowcl.leger.mobileparifoot.app.model.Team;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import static junit.framework.Assert.assertTrue;

public class FetchTeamsTask extends AsyncTask<String, Void,  String[]> {

    private final String LOG_TAG = FetchLeagueTask.class.getSimpleName();

    private ArrayAdapter<String> mTeamsAdapter;
    private final Context mContext;
    public static String[] array;

    // These are the names of the JSON objects that need to be extracted.
    final String STANDING = "standing";
    final String TEAM_NAME = "teamName";
    final String LEAGUE_CAPTION = "leagueCaption";
    final String MATCHDAY = "matchday";
    final String POSITION = "position";
    final String POINTS = "points";
    final String GOALDIFF = "goalDifference";

    public FetchTeamsTask( Context mContext, ArrayAdapter<String> mTeamsAdapter) {
        this.mTeamsAdapter = mTeamsAdapter;
        this.mContext = mContext;

    }

    private String[] getLeagueTableDataFromJson(String leagueTableJsonStr)
            throws JSONException {

        JSONObject jsonObject = new JSONObject(leagueTableJsonStr);
        JSONArray standingArray = jsonObject.getJSONArray(STANDING);

       // JSONArray standingArray = new JSONArray(leagueTableJsonStr);

        String[] resultStrs = new String[standingArray.length()];
                      array = new String[standingArray.length()];
        for(int i = 0; i < standingArray.length(); i++) {

            JSONObject teamJson = standingArray.getJSONObject(i);

            Team team = new Team(mContext);

            team.setLeague("Liga");//team.setLeague(jsonObject.getString(LEAGUE_CAPTION));
            team.setName(teamJson.getString(TEAM_NAME));
            team.setLogo_path(" ");
            team.setMatchday(jsonObject.getInt(MATCHDAY));
            team.setPosition(teamJson.getInt(POSITION));
            team.setPoints(teamJson.getInt(POINTS));
            team.setGoalDifference(teamJson.getInt(GOALDIFF));

            long teamRowId = team.add();

            resultStrs[i] = team.getName();



            // Verify we got a row back.
            assertTrue(teamRowId != -1);
            Log.d(LOG_TAG, "New row teeemmmmmm id: " + teamRowId);

            for (String s : resultStrs) {
                Log.v(LOG_TAG, "team entry: " + s);
            }
        }
            return resultStrs;


    }


    @Override
    protected String[] doInBackground(String... params) {

        // If there's no zip code, there's nothing to look up.  Verify size of params.
        if (params.length == 0) {
            return null;
        }

        String leagueJsonStr = null;
        try {
            leagueJsonStr = Utility.sendGetRequest("http://api.football-data.org/alpha/soccerseasons/351/leagueTable");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            return getLeagueTableDataFromJson(leagueJsonStr);
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
            mTeamsAdapter.clear();
            for(String teamStr : result) {
                mTeamsAdapter.add(teamStr);
            }
            // New data is back from the server.  Hooray!
        }
    }
}
