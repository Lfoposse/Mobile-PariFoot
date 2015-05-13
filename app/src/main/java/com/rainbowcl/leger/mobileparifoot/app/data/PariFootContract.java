package com.rainbowcl.leger.mobileparifoot.app.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import java.util.ArrayList;

/**
  * Defines table and column names for the Mobile PariFoot database.
  */
public class PariFootContract {

    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "com.rainbowcl.leger.mobileparifoot.app";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    // For instance, content://com.rainbowcl.leger.mobileparifoot.app/teams/ is a valid path for
    // looking at teams data. content://com.rainbowcl.leger.mobileparifoot.app/givemeroot/ will fail,
    // as the ContentProvider hasn't been given any information on what to do with "givemeroot".
    // At least, let's hope not.  Don't be that dev, reader.  Don't be that dev.
    public static final String PATH_TEAMS = "teams";
    public static final String PATH_FIXTURES = "fixtures";

   // Json Url to get Parifoot data to server
    public static final String LEAGUE_BASE_URL = "http://api.football-data.org/alpha/soccerseasons";
    public static final String built_url (String base_url){return base_url+"/?";};
    public static final String TOKEN_PARAM = "Auth-Token";
    public static final String KEY = "bc6848640d5a4e69a80758edac1dc841";
    public static ArrayList teams_soccer_url = null;
    public static  ArrayList soccer_seasons = null;



    /* Inner class that defines the table contents of the Teams table */
    public static final class TeamsEntry implements BaseColumns {

        // URI
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TEAMS).build();

        // MIME Type
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_TEAMS;

        // table name
        public static final String TABLE_NAME = "teams_table";

        /* Attributs of table*/

        // Column with the caption into the teams table.
        public static final String COLUMN_LEAGUE = "league";

        // get team name information on server
        public static final String COLUMN_TEAM = "team";

        // get logo path on local
        public static final String COLUMN_LOGO_PATH = "logo_path";

        //  get matchday  information on server
        public static final String COLUMN_MATCHDAY = "matchday";

        //  get team position information on server
        public static final String COLUMN_POSITION = "position";

        //  get team points information on server
        public static final String COLUMN_POINTS = "points";

        //  get team difference Goals points information on server
        public static final String COLUMN_GOALDIFF = "goalDifference";


        public static Uri buildTeamsUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    /* Inner class that defines the table contents of the Fixtures table*/
    public static final class FixturesEntry implements BaseColumns {

        // URI
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FIXTURES).build();

        // MIME Type
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_FIXTURES;

        //table name
        public static final String TABLE_NAME = "fixtures_table";

        /* Attributs of table*/
        //  get matchday  information on server
        public static final String COLUMN_MATCHDAY = "matchday";

        // Column with the caption into the teams table.
        public static final String COLUMN_LEAGUE = "league";

        // get homeTeamName on server
        public static final String COLUMN_HOME_TEAM = "homeTeamName";

        // get awayTeamName on server
        public static final String COLUMN_AWAY_TEAM = "awayTeamName";

        // get goalsHomeTeam on server
        public static final String COLUMN_GOAL_HOME = "goalsHomeTeam";

        // get goalsAwayTeam on server
        public static final String COLUMN_GOAL_AWAY = "goalsAwayTeam";

        //  get fixtures status information on server
        public static final String COLUMN_STATUS = "status";


        public static Uri buildFixturesUri(long id) {
               return ContentUris.withAppendedId(CONTENT_URI, id);
          }

        }

    }


