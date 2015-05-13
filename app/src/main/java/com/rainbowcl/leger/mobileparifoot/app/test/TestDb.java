package com.rainbowcl.leger.mobileparifoot.app.test;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

import com.rainbowcl.leger.mobileparifoot.app.data.PariFootContract.TeamsEntry;
import com.rainbowcl.leger.mobileparifoot.app.data.PariFootContract.FixturesEntry;
import com.rainbowcl.leger.mobileparifoot.app.data.PariFootDbHelper;

import java.util.Map;
import java.util.Set;

public class TestDb extends AndroidTestCase {

    public static final String LOG_TAG = TestDb.class.getSimpleName();

    public void testCreateDb() throws Throwable {
        mContext.deleteDatabase(PariFootDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new PariFootDbHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());
        db.close();
    }

    public void testInsertReadDb() {

        // If there's an error in those massive SQL table creation Strings,
        // errors will be thrown here when you try to get a writable database.
        PariFootDbHelper dbHelper = new PariFootDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues teamsValues = createTeamsValues();

        long teamsRowId;
        teamsRowId = db.insert(TeamsEntry.TABLE_NAME, null, teamsValues);

        // Verify we got a row back.
        assertTrue(teamsRowId != -1);
        Log.d(LOG_TAG, "New row teams id: " + teamsRowId);

        // A cursor is your primary interface to the query results.
        Cursor cursor = db.query(
                TeamsEntry.TABLE_NAME,  // Table to Query
                null,
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );

        validateCursor(cursor, teamsValues);

        ContentValues fixturesValues = createFixturesValues();

        long fixturesRowId = db.insert(FixturesEntry.TABLE_NAME, null, fixturesValues);
        assertTrue(fixturesRowId != -1);
        Log.d(LOG_TAG, "New row fixtures id: " + fixturesRowId);

        // A cursor is your primary interface to the query results.
        Cursor fixturesCursor = db.query(
                FixturesEntry.TABLE_NAME,  // Table to Query
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null  // sort order
        );

        validateCursor(fixturesCursor, fixturesValues);

        dbHelper.close();
    }

    static ContentValues createFixturesValues() {
        ContentValues fixturesValues = new ContentValues();
        fixturesValues.put(FixturesEntry.COLUMN_MATCHDAY, 20);
        fixturesValues.put(FixturesEntry.COLUMN_LEAGUE, "Liga");
        fixturesValues.put(FixturesEntry.COLUMN_HOME_TEAM, "Barcelone");
        fixturesValues.put(FixturesEntry.COLUMN_AWAY_TEAM, "Real");
        fixturesValues.put(FixturesEntry.COLUMN_GOAL_HOME, 1);
        fixturesValues.put(FixturesEntry.COLUMN_GOAL_AWAY, 2);
        fixturesValues.put(FixturesEntry.COLUMN_STATUS, "FINISHED");

        return fixturesValues;
    }

    static ContentValues createTeamsValues() {
        ContentValues teamsvalues = new ContentValues();
        teamsvalues.put(TeamsEntry.COLUMN_MATCHDAY,20);
        teamsvalues.put(TeamsEntry.COLUMN_LEAGUE, "Liga");
        teamsvalues.put(TeamsEntry.COLUMN_TEAM, "Barcelone");
        teamsvalues.put(TeamsEntry.COLUMN_LOGO_PATH, " ");
        teamsvalues.put(TeamsEntry.COLUMN_POSITION, 1);
        teamsvalues.put(TeamsEntry.COLUMN_POINTS,45);
        teamsvalues.put(TeamsEntry.COLUMN_GOALDIFF,45);

        return teamsvalues;
    }

    static void validateCursor(Cursor valueCursor, ContentValues expectedValues) {

        assertTrue(valueCursor.moveToFirst());

        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse(idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals(expectedValue, valueCursor.getString(idx));
        }
        valueCursor.close();
    }
}
