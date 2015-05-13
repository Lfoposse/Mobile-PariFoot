package com.rainbowcl.leger.mobileparifoot.app.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.rainbowcl.leger.mobileparifoot.app.data.PariFootContract.TeamsEntry;
import com.rainbowcl.leger.mobileparifoot.app.data.PariFootContract.FixturesEntry;

/**
 * Manages a local database for PariFoot data.
  */
public class PariFootDbHelper extends SQLiteOpenHelper {


    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "parifoot.db";

    public PariFootDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_TEAMS_TABLE = "CREATE TABLE " + TeamsEntry.TABLE_NAME + " (" +

                TeamsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                TeamsEntry.COLUMN_LEAGUE + " TEXT NOT NULL, " +
                TeamsEntry.COLUMN_TEAM + " TEXT NOT NULL, " +
                TeamsEntry.COLUMN_LOGO_PATH + " TEXT NOT NULL, " +
                TeamsEntry.COLUMN_MATCHDAY + " INTEGER NOT NULL,"+
                TeamsEntry.COLUMN_POSITION + " INTEGER NOT NULL, " +
                TeamsEntry.COLUMN_POINTS + " INTEGER NOT NULL, " +
                TeamsEntry.COLUMN_GOALDIFF + " INTEGER NOT NULL );" ;

        final String SQL_CREATE_FIXTURES_TABLE = "CREATE TABLE " + FixturesEntry.TABLE_NAME + " (" +

                FixturesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                FixturesEntry.COLUMN_LEAGUE + " TEXT NOT NULL, " +
                FixturesEntry.COLUMN_HOME_TEAM + " TEXT NOT NULL, " +
                FixturesEntry.COLUMN_AWAY_TEAM + " TEXT NOT NULL, " +

                FixturesEntry.COLUMN_GOAL_HOME + " INTEGER NOT NULL, " +
                FixturesEntry.COLUMN_GOAL_AWAY + " INTEGER NOT NULL, " +

                FixturesEntry.COLUMN_STATUS + " TEXT NOT NULL, " +
                FixturesEntry.COLUMN_MATCHDAY + " INTEGER NOT NULL); " ;


                sqLiteDatabase.execSQL(SQL_CREATE_TEAMS_TABLE);
                sqLiteDatabase.execSQL(SQL_CREATE_FIXTURES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TeamsEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FixturesEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
