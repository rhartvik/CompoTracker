package ca.cinderblok.compotracker.DAL;

import android.provider.BaseColumns;

/**
 * Created by rachelhartviksen on 16-04-21.
 */
public final class CompoDbContract {
    
    public CompoDbContract() {}

    static final String TAG = CompoDbHelper.class.getSimpleName();

    public static String DATABASE_NAME = "compo.db";
    public static int DATABASE_VERSION = 1;

    public static String COLUMN_NAME_TIMESTAMP = "COLUMN_MEASUREMENT_TIMESTAMP";

    public static String LAST_INPUT_QUERY = "SELECT "
            + CompoWeightEntry.TABLE_NAME + "." + CompoDbContract.CompoWeightEntry.COLUMN_NAME_TOTAL + ", "
            + CompoPercentEntry.TABLE_NAME + "." + CompoDbContract.CompoPercentEntry.COLUMN_NAME_FAT + ", "
            + CompoPercentEntry.TABLE_NAME + "." + CompoDbContract.CompoPercentEntry.COLUMN_NAME_WATER + ", "
            + CompoPercentEntry.TABLE_NAME + "." + CompoDbContract.CompoPercentEntry.COLUMN_NAME_MUSCLE + ", "
            + CompoWeightEntry.TABLE_NAME + "." + CompoDbContract.CompoWeightEntry.COLUMN_NAME_BONE + " "
            + "FROM " + CompoPercentEntry.TABLE_NAME + " "
            + "LEFT JOIN " + CompoWeightEntry.TABLE_NAME + " ON "
            + CompoWeightEntry.TABLE_NAME + "." + CompoDbContract.COLUMN_NAME_TIMESTAMP + " = "
            + CompoPercentEntry.TABLE_NAME + "." + CompoDbContract.COLUMN_NAME_TIMESTAMP + " "
            + "ORDER BY " + CompoWeightEntry.TABLE_NAME + "." + CompoDbContract.COLUMN_NAME_TIMESTAMP + " DESC "
            + "LIMIT 1";
    
    public static abstract class CompoWeightEntry implements BaseColumns {

        public static final String SQL_CREATE = "CREATE TABLE " + CompoWeightEntry.TABLE_NAME + " ("
                + CompoWeightEntry._ID + " int primary key, "
                + COLUMN_NAME_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                + CompoWeightEntry.COLUMN_NAME_TOTAL + " int, "
                + CompoWeightEntry.COLUMN_NAME_FAT + " int, "
                + CompoWeightEntry.COLUMN_NAME_WATER + " int, "
                + CompoWeightEntry.COLUMN_NAME_MUSCLE + " int, "
                + CompoWeightEntry.COLUMN_NAME_BONE + " int)";

        public static final String SQL_DELETE = "DROP TABLE IF EXISTS " + CompoWeightEntry.TABLE_NAME;

        public static final String TABLE_NAME = "weight";
        public static final String COLUMN_NAME_TOTAL = "total";
        public static final String COLUMN_NAME_FAT = "fat";
        public static final String COLUMN_NAME_WATER = "water";
        public static final String COLUMN_NAME_MUSCLE = "muscle";
        public static final String COLUMN_NAME_BONE = "bone";

        public static final String[] FULL_PROJECTION = new String[] {
                COLUMN_NAME_TIMESTAMP
                ,_ID
                , COLUMN_NAME_TOTAL
                , COLUMN_NAME_FAT
                , COLUMN_NAME_WATER
                , COLUMN_NAME_MUSCLE
                , COLUMN_NAME_BONE
        };

    }
    public static abstract class CompoPercentEntry implements BaseColumns {

        public static final String SQL_CREATE = "CREATE TABLE " + CompoPercentEntry.TABLE_NAME + " ("
                + CompoPercentEntry._ID + " int primary key, "
                + COLUMN_NAME_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                + CompoPercentEntry.COLUMN_NAME_FAT + " int, "
                + CompoPercentEntry.COLUMN_NAME_WATER + " int, "
                + CompoPercentEntry.COLUMN_NAME_MUSCLE + " int, "
                + CompoPercentEntry.COLUMN_NAME_BONE + " int)";

        public static final String SQL_DELETE = "DROP TABLE IF EXISTS " + CompoPercentEntry.TABLE_NAME;

        public static final String TABLE_NAME = "percent";
        public static final String COLUMN_NAME_FAT = "fat";
        public static final String COLUMN_NAME_WATER = "water";
        public static final String COLUMN_NAME_MUSCLE = "muscle";
        public static final String COLUMN_NAME_BONE = "bone";

        public static final String[] FULL_PROJECTION = new String[] {
                COLUMN_NAME_TIMESTAMP
                ,_ID
                , COLUMN_NAME_FAT
                , COLUMN_NAME_WATER
                , COLUMN_NAME_MUSCLE
                , COLUMN_NAME_BONE
        };

    }
}
