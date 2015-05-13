package com.rainbowcl.leger.mobileparifoot.app.test;


import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.util.Log;

import com.rainbowcl.leger.mobileparifoot.app.data.PariFootContract.TeamsEntry;
import com.rainbowcl.leger.mobileparifoot.app.data.PariFootContract.FixturesEntry;


public class TestProvider extends AndroidTestCase {

    public static final String LOG_TAG = TestProvider.class.getSimpleName();

    // brings our database to an empty state
    public void deleteAllRecords() {
        mContext.getContentResolver().delete(
                TeamsEntry.CONTENT_URI,
                null,
                null
        );
        mContext.getContentResolver().delete(
                FixturesEntry.CONTENT_URI,
                null,
                null
        );

        Cursor cursor = mContext.getContentResolver().query(
                TeamsEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals(0, cursor.getCount());
        cursor.close();

        cursor = mContext.getContentResolver().query(
                FixturesEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals(0, cursor.getCount());
        cursor.close();
    }

    // Since we want each test to start with a clean slate, run deleteAllRecords
    // in setUp (called by the test runner before each test).
    public void setUp() {
        deleteAllRecords();
    }


    public void testInsertReadProvider() {


        ContentValues teamValues = TestDb.createTeamsValues();

        Uri teamUri = mContext.getContentResolver().insert(TeamsEntry.CONTENT_URI, teamValues);
                long teamRowId = ContentUris.parseId(teamUri);

        // Verify we got a row back.
        assertTrue(teamRowId != -1);
       // Log.d(LOG_TAG, "New row id: " + teamRowId);

        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
        // the round trip.

        // A cursor is your primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
                             TeamsEntry.CONTENT_URI,  // Table to Query
                null, // all columns
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null // columns to group by
        );

        TestDb.validateCursor(cursor, teamValues);

        // Fantastic.  Now that we have a location, add some weather!
        ContentValues fixtureValues = TestDb.createFixturesValues();

        Uri fixtureUri = mContext.getContentResolver().insert(FixturesEntry.CONTENT_URI, fixtureValues);
        long fixtureRowId = ContentUris.parseId(fixtureUri);
        assertTrue(fixtureRowId != -1);

        // A cursor is your primary interface to the query results.
         cursor = mContext.getContentResolver().query(
                FixturesEntry.CONTENT_URI,  // Table to Query
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null // columns to group by

        );

        TestDb.validateCursor(cursor, fixtureValues);

    }

    public void testGetType() {
        // content://com.rainbowcl.leger.mobileparifoot.app/teams/liga
        String type = mContext.getContentResolver().getType(TeamsEntry.CONTENT_URI);
        // vnd.android.cursor.dir/com.rainbowcl.leger.mobileparifoot.app/teams
        assertEquals(TeamsEntry.CONTENT_TYPE, type);

        // content://com.rainbowcl.leger.mobileparifoot.app/fixtures/liga/31
         type = mContext.getContentResolver().getType(FixturesEntry.CONTENT_URI);
        // vnd.android.cursor.dir/com.rainbowcl.leger.mobileparifoot.app/fixtures
        assertEquals(FixturesEntry.CONTENT_TYPE, type);

    }
    public void testUpdateTeams() {
        // Create a new map of values, where column names are the keys
        ContentValues values = TestDb.createTeamsValues();

        Uri teamUri = mContext.getContentResolver().
                insert(TeamsEntry.CONTENT_URI, values);
        long teamRowId = ContentUris.parseId(teamUri);

        // Verify we got a row back.
        assertTrue(teamRowId != -1);
        Log.d(LOG_TAG, "New row good id: " + teamUri);

        ContentValues updatedValues = new ContentValues(values);
        updatedValues.put(TeamsEntry.COLUMN_MATCHDAY, teamRowId);
        updatedValues.put(TeamsEntry.COLUMN_POSITION, 2);

        int count = mContext.getContentResolver().update(
                TeamsEntry.CONTENT_URI, updatedValues, TeamsEntry.COLUMN_LEAGUE + "= ?",
                new String[] { "Liga"});

        assertEquals(count, 1);

        // A cursor is your primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
                TeamsEntry.CONTENT_URI,
                null,
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null // sort order
        );

        TestDb.validateCursor(cursor, updatedValues);
    }

    // Make sure we can still delete after adding/updating stuff
    public void testDeleteRecordsAtEnd() {
        deleteAllRecords();
    }
}
