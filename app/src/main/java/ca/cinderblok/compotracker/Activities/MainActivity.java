package ca.cinderblok.compotracker.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import ca.cinderblok.compotracker.Models.InputEntry;
import ca.cinderblok.compotracker.Models.PercentEntry;
import ca.cinderblok.compotracker.Models.WeightEntry;
import ca.cinderblok.compotracker.R;
import ca.cinderblok.compotracker.DAL.CompoDbContract;
import ca.cinderblok.compotracker.DAL.CompoDbHelper;

import java.util.ArrayList;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase mDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

                View bodyWeightPickerFrame = (View) findViewById(R.id.body_weight_picker);
                View fatPercentPickerFrame = (View) findViewById(R.id.fat_percent_picker);
                View waterPercentPickerFrame = (View) findViewById(R.id.water_percent_picker);
                View musclePercentPickerFrame = (View) findViewById(R.id.muscle_percent_picker);
                View boneWeightPickerFrame = (View) findViewById(R.id.bone_mass_picker);

                EditText bodyWeightEditText = (EditText) bodyWeightPickerFrame.findViewById(R.id.number_picker_field);
                EditText fatPercentEditText = (EditText) fatPercentPickerFrame.findViewById(R.id.number_picker_field);
                EditText waterPercentEditText = (EditText) waterPercentPickerFrame.findViewById(R.id.number_picker_field);
                EditText musclePercentEditText = (EditText) musclePercentPickerFrame.findViewById(R.id.number_picker_field);
                EditText boneWeightEditText = (EditText) boneWeightPickerFrame.findViewById(R.id.number_picker_field);

                InputEntry entry = new InputEntry();
                entry.TimeStamp = currentTimestamp;
                try {
                    entry.BodyWeight = Float.parseFloat(bodyWeightEditText.getText().toString());
                    entry.FatPercent = Float.parseFloat(fatPercentEditText.getText().toString());
                    entry.WaterPercent = Float.parseFloat(waterPercentEditText.getText().toString());
                    entry.MusclePercent = Float.parseFloat(musclePercentEditText.getText().toString());
                    entry.BoneWeight = Float.parseFloat(boneWeightEditText.getText().toString());
                } catch (final NumberFormatException e) {

                    Snackbar.make(view, "Number format exception. Please ensure your data is entered correctly."
                            , Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }

                WeightEntry weightEntry = new WeightEntry(entry);
                PercentEntry percentEntry = new PercentEntry(entry);

                // Add to Percent Table
                long newPercentRowId;
                newPercentRowId = mDb.insert(
                        CompoDbContract.CompoPercentEntry.TABLE_NAME
                        , null
                        , percentEntry.GetContent10Values());

                // Add to Weight Table
                long newWeightRowId;
                newWeightRowId = mDb.insert(
                        CompoDbContract.CompoWeightEntry.TABLE_NAME
                        , null
                        , weightEntry.GetContent10Values());

                // Confirm the Addition of Data
                Snackbar.make(view, "Added new weight entry with id = " + newWeightRowId
                                + " and new percent entry with id = " + newPercentRowId
                        , Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        CompoDbHelper mDbHelper = new CompoDbHelper(this, CompoDbContract.DATABASE_NAME, null, CompoDbContract.DATABASE_VERSION);
        mDb = mDbHelper.getWritableDatabase();

        ArrayList<Pair<String,Integer>> columnNameAndPickerId = new ArrayList<>();
        columnNameAndPickerId.add(new Pair<>(CompoDbContract.CompoWeightEntry.COLUMN_NAME_TOTAL,R.id.body_weight_picker));
        columnNameAndPickerId.add(new Pair<>(CompoDbContract.CompoPercentEntry.COLUMN_NAME_FAT,R.id.fat_percent_picker));
        columnNameAndPickerId.add(new Pair<>(CompoDbContract.CompoPercentEntry.COLUMN_NAME_WATER,R.id.water_percent_picker));
        columnNameAndPickerId.add(new Pair<>(CompoDbContract.CompoPercentEntry.COLUMN_NAME_MUSCLE,R.id.muscle_percent_picker));
        columnNameAndPickerId.add(new Pair<>(CompoDbContract.CompoWeightEntry.COLUMN_NAME_BONE,R.id.bone_mass_picker));

        try (Cursor c = mDb.rawQuery(CompoDbContract.LAST_INPUT_QUERY, new String[]{})) {
            if (c != null) {
                if (c.moveToFirst()) {
                    // Load the last value
                    for (Pair<String, Integer> col : columnNameAndPickerId) {
                        int value10 = c.getInt(c.getColumnIndexOrThrow(col.first));
                        Float value = (((float) value10) / 10);
                        View view = findViewById(col.second);
                        EditText editText = (EditText) view.findViewById(R.id.number_picker_field);
                        editText.setText(value.toString());

                    }

                } else {
                    // TODO: Disable the weight button?
                }
            }
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        mDb.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Class c;
        switch (item.getItemId()) {
            case R.id.weight_chart_page:
                c = WeightChartActivity.class;
                break;
            case R.id.percent_chart_page:
                c = PercentChartActivity.class;
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        Intent toToActivity =
                new Intent(this.getBaseContext(), c);
        startActivity(toToActivity);

        return true;
    }

    public void incrementPicker(View view) {
        View v = (View) view.getParent();
        EditText pickerText = (EditText) v.findViewById(R.id.number_picker_field);
        Float currentValue = 0F;
        try {
            currentValue = Float.parseFloat(pickerText.getText().toString());
        } catch (final NumberFormatException e) {
            Snackbar.make(view, "Error: please ensure input is properly formatted"
                    , Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
        }
        Float newValue = (float) ((int) (currentValue * 10) + 1) / 10F;
        pickerText.setText(newValue.toString());
    }

    public void decrementPicker(View view) {
        View v = (View) view.getParent();
        EditText pickerText = (EditText) v.findViewById(R.id.number_picker_field);
        Float currentValue = 0F;
        try {
            currentValue = Float.parseFloat(pickerText.getText().toString());
        } catch (final NumberFormatException e) {
            Snackbar.make(view, "Error: please ensure input is properly formatted"
                    , Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
        }
        Float newValue = (float) ((int) (currentValue * 10) - 1) / 10F;
        if (newValue > 0F) {
            pickerText.setText(newValue.toString());
        }
    }
}
