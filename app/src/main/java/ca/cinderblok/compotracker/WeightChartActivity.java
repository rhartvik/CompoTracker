package ca.cinderblok.compotracker;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;

import java.util.Calendar;
import java.util.Locale;

public class WeightChartActivity extends AppCompatActivity {

    private SQLiteDatabase mReadableDb;
    private CompoDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_chart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDbHelper = new CompoDbHelper(this, CompoDbContract.DATABASE_NAME, null, CompoDbContract.DATABASE_VERSION);
        mReadableDb = mDbHelper.getReadableDatabase();

        //Make this a refresh button?
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] projection = {
                        CompoDbContract.CompoWeightEntry._ID,
                        CompoDbContract.COLUMN_NAME_TIMESTAMP,
                        CompoDbContract.CompoWeightEntry.COLUMN_NAME_TOTAL,
                        CompoDbContract.CompoWeightEntry.COLUMN_NAME_FAT,
                        CompoDbContract.CompoWeightEntry.COLUMN_NAME_MUSCLE,
                        CompoDbContract.CompoWeightEntry.COLUMN_NAME_WATER,
                        CompoDbContract.CompoWeightEntry.COLUMN_NAME_BONE
                };
                String sortOrder = CompoDbContract.COLUMN_NAME_TIMESTAMP + " DESC";

                StringBuilder output = new StringBuilder();
                try (Cursor c = mReadableDb.query(CompoDbContract.CompoWeightEntry.TABLE_NAME
                        ,projection
                        ,null
                        ,null
                        ,null
                        ,null
                        ,sortOrder);) {
                    while (c.moveToNext()) {
                        long timeStamp = c.getLong (
                                c.getColumnIndexOrThrow(CompoDbContract.COLUMN_NAME_TIMESTAMP)
                        );

                        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                        cal.setTimeInMillis(timeStamp);
                        String date = DateFormat.format("dd-MM-yyyy HH:mm", cal).toString();

                        int total = c.getInt (
                                c.getColumnIndexOrThrow(CompoDbContract.CompoWeightEntry.COLUMN_NAME_TOTAL)
                        );
                        int fat = c.getInt (
                                c.getColumnIndexOrThrow(CompoDbContract.CompoWeightEntry.COLUMN_NAME_FAT)
                        );
                        int water = c.getInt (
                                c.getColumnIndexOrThrow(CompoDbContract.CompoWeightEntry.COLUMN_NAME_WATER)
                        );
                        int muscle = c.getInt (
                                c.getColumnIndexOrThrow(CompoDbContract.CompoWeightEntry.COLUMN_NAME_MUSCLE)
                        );
                        int bone = c.getInt (
                                c.getColumnIndexOrThrow(CompoDbContract.CompoWeightEntry.COLUMN_NAME_BONE)
                        );

                        output.append(String.format(
                                getString(R.string.weight_entry_plaintext_short)
                                , date
                                , new Double(total / 10)
                                , (double) (fat/10)
                                , (double) (water/10)
                                , (double) (muscle/10)
                                , (double) (bone/10)));
                    }
                }

                EditText textView = (EditText)findViewById(R.id.outputText);
                textView.setText(output.toString());
                Snackbar.make(view, "Data has been refreshed", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void refreshData() {
        String[] projection = {
            CompoDbContract.CompoWeightEntry._ID,
            CompoDbContract.COLUMN_NAME_TIMESTAMP,
            CompoDbContract.CompoWeightEntry.COLUMN_NAME_TOTAL,
            CompoDbContract.CompoWeightEntry.COLUMN_NAME_FAT,
            CompoDbContract.CompoWeightEntry.COLUMN_NAME_MUSCLE,
            CompoDbContract.CompoWeightEntry.COLUMN_NAME_WATER,
            CompoDbContract.CompoWeightEntry.COLUMN_NAME_BONE
        };
        String sortOrder = CompoDbContract.COLUMN_NAME_TIMESTAMP + " DESC";

        StringBuilder output = new StringBuilder();
        try (Cursor c = mReadableDb.query(CompoDbContract.CompoWeightEntry.TABLE_NAME
                ,projection
                ,null
                ,null
                ,null
                ,null
                ,sortOrder);) {
            while (c.moveToNext()) {
                long timeStamp = c.getLong (
                    c.getColumnIndexOrThrow(CompoDbContract.COLUMN_NAME_TIMESTAMP)
                );

                Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                cal.setTimeInMillis(timeStamp);
                String date = DateFormat.format("dd-MM-yyyy", cal).toString();

                int total = c.getInt (
                    c.getColumnIndexOrThrow(CompoDbContract.CompoWeightEntry.COLUMN_NAME_TOTAL)
                );
                int fat = c.getInt (
                    c.getColumnIndexOrThrow(CompoDbContract.CompoWeightEntry.COLUMN_NAME_FAT)
                );
                int water = c.getInt (
                    c.getColumnIndexOrThrow(CompoDbContract.CompoWeightEntry.COLUMN_NAME_WATER)
                );
                int muscle = c.getInt (
                    c.getColumnIndexOrThrow(CompoDbContract.CompoWeightEntry.COLUMN_NAME_MUSCLE)
                );
                int bone = c.getInt (
                    c.getColumnIndexOrThrow(CompoDbContract.CompoWeightEntry.COLUMN_NAME_BONE)
                );

                output.append(String.format(
                        getString(R.string.weight_entry_plaintext)
                        , date
                        , total
                        , fat
                        , water
                        , muscle
                        , bone));
            }
        }

        EditText textView = (EditText)findViewById(R.id.outputText);
        textView.setText(output.toString());

    }

    @Override
    protected void onStart() {
        super.onStart();


    }
}
