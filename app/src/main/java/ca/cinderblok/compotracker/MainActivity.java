package ca.cinderblok.compotracker;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase mDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CompoDbHelper mDbHelper = new CompoDbHelper(this, CompoDbContract.DATABASE_NAME, null, CompoDbContract.DATABASE_VERSION);
        mDb =  mDbHelper.getWritableDatabase(); // ????

        // Hook up the button for the Weight Chart Activity
        Button goToWeightChartActivityButton = (Button) findViewById(R.id.weight_chart_button);
        goToWeightChartActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToWeightChartActivityIntent =
                        new Intent(view.getContext(), WeightChartActivity.class);
                startActivity(goToWeightChartActivityIntent);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Gather the data
                Date currentDate = new Date();
                Long currentTimestamp = currentDate.getTime();

                EditText bodyWeightEditText = (EditText) findViewById(R.id.bodyweight);
                Double bodyWeight = Double.parseDouble(bodyWeightEditText.getText().toString());
                int bodyWeight10 = (int) (bodyWeight * 10);

                EditText fatPercentEditText = (EditText)findViewById(R.id.bodyfat);
                Double bodyFatPercent = Double.parseDouble(fatPercentEditText.getText().toString());
                int bodyFatPercent10 = (int) (bodyFatPercent * 10);
                int bodyFatWeight10 = (int)Math.round(bodyFatPercent * bodyWeight / 10);

                EditText waterPercentEditText = (EditText)findViewById(R.id.totalbodywater);
                Double bodyWaterPercent = Double.parseDouble(waterPercentEditText.getText().toString());
                int bodyWaterPercent10 = (int) (bodyWaterPercent * 10);
                int bodyWaterWeight10 = (int)Math.round(bodyWaterPercent * bodyWeight / 10);

                EditText musclePercentEditText = (EditText)findViewById(R.id.musclemass);
                Double bodyMusclePercent = Double.parseDouble(musclePercentEditText.getText().toString());
                int bodyMusclePercent10 = (int) (bodyMusclePercent * 10);
                int bodyMuscleWeight10 = (int)Math.round(bodyMusclePercent * bodyWeight / 10);

                EditText boneWeightEditText = (EditText)findViewById(R.id.bonemass);
                Double boneWeight = Double.parseDouble(boneWeightEditText.getText().toString());
                int boneWeight10 = (int) (boneWeight * 10);
                int bonePercent10 = (int)Math.round(boneWeight * 1000 / bodyWeight);

                // Add to Percent Table
                ContentValues percentValues = new ContentValues();
                percentValues.put(CompoDbContract.COLUMN_NAME_TIMESTAMP, currentTimestamp);
                percentValues.put(CompoDbContract.CompoPercentEntry.COLUMN_NAME_FAT, bodyFatPercent10);
                percentValues.put(CompoDbContract.CompoPercentEntry.COLUMN_NAME_WATER, bodyWaterPercent10);
                percentValues.put(CompoDbContract.CompoPercentEntry.COLUMN_NAME_MUSCLE, bodyMusclePercent10);
                percentValues.put(CompoDbContract.CompoPercentEntry.COLUMN_NAME_BONE, bonePercent10);

                long newPercentRowId;
                newPercentRowId = mDb.insert(
                        CompoDbContract.CompoPercentEntry.TABLE_NAME
                        , null
                        , percentValues);

                // Add to Weight Table
                ContentValues weightValues = new ContentValues();
                weightValues.put(CompoDbContract.COLUMN_NAME_TIMESTAMP, currentTimestamp);
                weightValues.put(CompoDbContract.CompoWeightEntry.COLUMN_NAME_TOTAL, bodyWeight10);
                weightValues.put(CompoDbContract.CompoWeightEntry.COLUMN_NAME_FAT, bodyFatWeight10);
                weightValues.put(CompoDbContract.CompoWeightEntry.COLUMN_NAME_WATER, bodyWaterWeight10);
                weightValues.put(CompoDbContract.CompoWeightEntry.COLUMN_NAME_MUSCLE, bodyMuscleWeight10);
                weightValues.put(CompoDbContract.CompoWeightEntry.COLUMN_NAME_BONE, boneWeight10);

                long newWeightRowId;
                newWeightRowId = mDb.insert(
                        CompoDbContract.CompoWeightEntry.TABLE_NAME
                        , null
                        , weightValues);

                // Confirm the Addition of Data
                Snackbar.make(view, "Added new weight entry with id = " + newWeightRowId
                        + " and new percent entry with id = " + newPercentRowId
                        , Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
