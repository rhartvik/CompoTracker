package ca.cinderblok.compotracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by rachelhartviksen on 16-04-20.
 */
public class CompoDbHelper extends SQLiteOpenHelper{

    static final String TAG = CompoDbHelper.class.getSimpleName();

    public static String DATABASE_NAME = "compo.db";
    public static int DATABASE_VERSION = 1;

    public CompoDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(CompoDbContract.CompoPercentEntry.SQL_CREATE);
        Log.d(TAG, "onCreated sql: " + CompoDbContract.CompoPercentEntry.SQL_CREATE);

        sqLiteDatabase.execSQL(CompoDbContract.CompoWeightEntry.SQL_CREATE);
        Log.d(TAG, "onCreated sql: " + CompoDbContract.CompoWeightEntry.SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL(CompoDbContract.CompoPercentEntry.SQL_DELETE);
        Log.d(TAG, "onCreated sql: " + CompoDbContract.CompoPercentEntry.SQL_DELETE);

        sqLiteDatabase.execSQL(CompoDbContract.CompoWeightEntry.SQL_DELETE);
        Log.d(TAG, "onCreated sql: " + CompoDbContract.CompoWeightEntry.SQL_DELETE);

        onCreate(sqLiteDatabase);
    }
}
