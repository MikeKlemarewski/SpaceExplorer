package com.mikeklem.spaceexplorer;

/**
 * Created by juan_laramoreno on 14-11-08.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public final class DatabaseContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public DatabaseContract() {}

    /* Inner class that defines the table contents */
    public static class StarEntryColumns implements BaseColumns {
        static final String TABLE_NAME = "Stars";
        static final String COLUMN_NAME_StarID = "StarID";
        static final String COLUMN_NAME_HIP = "HIP";
        static final String COLUMN_NAME_HD = "HD";
        static final String COLUMN_NAME_HR = "HR";
        static final String COLUMN_NAME_Gliese = "Gliese";
        static final String COLUMN_NAME_BayerFlamsteed = "BayerFlamsteed";
        static final String COLUMN_NAME_ProperName = "ProperName";
        static final String COLUMN_NAME_RA = "RA";
        static final String COLUMN_NAME_Dec = "Dec";
        static final String COLUMN_NAME_Distance = "Distance";
        static final String COLUMN_NAME_PMRA = "PMRA";
        static final String COLUMN_NAME_PMDec = "PMDec";
        static final String COLUMN_NAME_RV = "RV";
        static final String COLUMN_NAME_Mag = "Mag";
        static final String COLUMN_NAME_AbsMag = "AbsMag";
        static final String COLUMN_NAME_Spectrum = "Spectrum";
        static final String COLUMN_NAME_ColorIndex = "ColorIndex";
        static final String COLUMN_NAME_X = "X";
        static final String COLUMN_NAME_Y = "Y";
        static final String COLUMN_NAME_Z = "Z";
        static final String COLUMN_NAME_VX = "VX";
        static final String COLUMN_NAME_VY = "VY";
        static final String COLUMN_NAME_VZ = "VZ";
        static final String COLUMN_NAME_quadrant = "quadrant";
    }
}
