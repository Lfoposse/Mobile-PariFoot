package com.rainbowcl.leger.mobileparifoot.app.model;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.rainbowcl.leger.mobileparifoot.app.data.PariFootContract;

public class Team {
    private String league;
    private String name;
    private String logo_path;
    private int matchday;
    private int position;
    private int points;
    private int goalDifference;
    private Context mContext;

    private final String LOG_TAG = Team.class.getSimpleName();

    public Team(Context mContext) {
        this.mContext = mContext;
        this.league = "";
        this.name = "";
        this.logo_path = "";
        this.matchday = 0;
        this.position = 0;
        this.points = 0;
    }

    public Team() {

    }

    public Team(String league, String name, String logo_path, int matchday, int position, int points, Context mContext) {
        this.league = league;
        this.name = name;
        this.logo_path = logo_path;
        this.matchday = matchday;
        this.position = position;
        this.points = points;
        this.mContext = mContext;
    }

    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo_path() {
        return logo_path;
    }

    public void setLogo_path(String logo_path) {
        this.logo_path = logo_path;
    }

    public int getMatchday() {
        return matchday;
    }

    public void setMatchday(int matchday) {
        this.matchday = matchday;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getGoalDifference() {
        return goalDifference;
    }

    public void setGoalDifference(int goalDifference) {
        this.goalDifference = goalDifference;
    }

    public ContentValues createTeamValues() {
        ContentValues teamsvalues = new ContentValues();
        teamsvalues.put(PariFootContract.TeamsEntry.COLUMN_LEAGUE, this.league);
        teamsvalues.put(PariFootContract.TeamsEntry.COLUMN_TEAM, this.name);
        teamsvalues.put(PariFootContract.TeamsEntry.COLUMN_LOGO_PATH, this.logo_path);
        teamsvalues.put(PariFootContract.TeamsEntry.COLUMN_MATCHDAY, this.matchday);
        teamsvalues.put(PariFootContract.TeamsEntry.COLUMN_POSITION, this.position);
        teamsvalues.put(PariFootContract.TeamsEntry.COLUMN_POINTS, this.points);
        teamsvalues.put(PariFootContract.TeamsEntry.COLUMN_GOALDIFF, this.goalDifference);

        return teamsvalues;
    }

    /**
     * Helper method to handle insertion of a new team in the parifoot database.
     *
     * @return the row ID of the added team.
     */
    public long add() {

        Log.v(LOG_TAG, "inserting " + this.name + ", with matchday: " + this.matchday +
                ", position: " + this.position + "and points: " + this.points);

        // First, check if the team with this name exists in the db
       /* Cursor cursor = mContext.getContentResolver().query(
                PariFootContract.TeamsEntry.CONTENT_URI,
                new String[]{PariFootContract.TeamsEntry._ID},
                PariFootContract.TeamsEntry.COLUMN_TEAM + " = ?"+ PariFootContract.TeamsEntry.COLUMN_MATCHDAY + " = ?",
                new String[]{this.name, String.valueOf(this.matchday)},
                null);

        if (cursor.moveToFirst()) {
            Log.v(LOG_TAG, "Found it in the database!");
            int teamsIdIndex = cursor.getColumnIndex(PariFootContract.TeamsEntry._ID);
            return cursor.getLong(teamsIdIndex);
        } else {*/
            Log.v(LOG_TAG, "Didn't find it in the database, inserting now!");
            ContentValues teamValues = createTeamValues();

            Uri teamsInsertUri = mContext.getContentResolver()
                    .insert(PariFootContract.TeamsEntry.CONTENT_URI, teamValues);

            return ContentUris.parseId(teamsInsertUri);
        //}
    }


}
