package com.mikeklem.spaceexplorer;

/**
 * Created by juan_laramoreno on 14-11-08.
 */

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.io.IOException;

public class StarsDataSource {

    // Database fields
    private DatabaseHelper dbHelper;

    public StarsDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
        try {
            dbHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }
        try {
            dbHelper.openDataBase();
        }catch(SQLException sqle){
            throw sqle;
        }catch (java.sql.SQLException sql) {
            //throw sql;
        }
    }

    public void close() {
        dbHelper.close();
    }

    /*public Star createStar(int StarID) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_COMMENT, comment);
        long insertId = database.insert(MySQLiteHelper.TABLE_COMMENTS, null,
                values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_COMMENTS,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Star newStar = cursorToStar(cursor);
        cursor.close();
        return newStar;
    }*/

    public void deleteStar(Star star) {
        long id = star.getStarID();
        System.out.println("Star deleted with id: " + id);
        dbHelper.myDataBase.delete(DatabaseContract.StarEntryColumns.TABLE_NAME, DatabaseContract.StarEntryColumns.COLUMN_NAME_StarID
                + " = " + id, null);
    }

    public List<Star> getAllStars() {
        List<Star> stars = new ArrayList<Star>();

        Cursor cursor = dbHelper.myDataBase.query(DatabaseContract.StarEntryColumns.TABLE_NAME,
                null, "StarID<?", new String[] { Integer.toString(10) }, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Star star = cursorToStar(cursor);
            stars.add(star);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return stars;
    }

    public List<Star> getStarInQuadrant(float x, float y, float z) {
        List<Star> stars = new ArrayList<Star>();

        Cursor cursor = dbHelper.myDataBase.query(DatabaseContract.StarEntryColumns.TABLE_NAME,
                new String[] { "Stars.*", "ABS(X) + ABS(Y) + ABS(Z) AS Closeness"}, "(x BETWEEN ?-20 AND ?+20) AND (y BETWEEN ?-20 AND ?+20) AND (z BETWEEN ?-20 AND ?+20)",
                new String[] { Float.toString(x),Float.toString(x), Float.toString(y),Float.toString(y),
                Float.toString(z),Float.toString(z)}, null, null, "Closeness ASC", "100");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Star star = cursorToStar(cursor);
            stars.add(star);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return stars;
    }

    private Star cursorToStar(Cursor cursor) {
        Star star = new Star();
        star.setStarID(cursor.getInt(0));
        star.setHIP(cursor.getString(1));
        star.setHD(cursor.getString(2));
        star.setHR(cursor.getString(3));
        star.setGliese(cursor.getString(4));
        star.setBayerFlamsteed(cursor.getString(5));
        star.setProperName(cursor.getString(6));
        star.setRA(cursor.getString(7));
        star.setDec(cursor.getString(8));
        star.setDistance(cursor.getString(9));
        star.setPMRA(cursor.getString(10));
        star.setPMDec(cursor.getString(11));
        star.setRV(cursor.getString(12));
        star.setMag(cursor.getFloat(13));
        star.setAbsMag(cursor.getFloat(14));
        star.setSpectrum(cursor.getString(15));
        star.setColorIndex(cursor.getString(16));
        star.setX(cursor.getString(17));
        star.setY(cursor.getString(18));
        star.setZ(cursor.getString(19));
        star.setVX(cursor.getString(20));
        star.setVY(cursor.getString(21));
        star.setVZ(cursor.getString(22));
        star.setQuadrant(cursor.getInt(23));
        return star;
    }
}