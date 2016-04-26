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

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
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
        assert fab != null;
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

                // To store text output
                StringBuilder output = new StringBuilder();

                // To store output as Entrys for MPAndroidChart
                ArrayList<String> datetimeLabels = new ArrayList<String>();
                ArrayList<Entry> valsTotalBodyWeight = new ArrayList<Entry>();
                ArrayList<Entry> valsTotalFatWeight = new ArrayList<Entry>();
                ArrayList<Entry> valsTotalWaterWeight = new ArrayList<Entry>();
                ArrayList<Entry> valsTotalMuscleWeight = new ArrayList<Entry>();
                ArrayList<Entry> valsTotalBoneWeight = new ArrayList<Entry>();

                // Query data
                try (Cursor c = mReadableDb.query(CompoDbContract.CompoWeightEntry.TABLE_NAME
                        ,projection
                        ,null
                        ,null
                        ,null
                        ,null
                        ,sortOrder);) {
                    // Keep track of index for MPAndroid Entrys
                    int index = 0;
                    while (c.moveToNext()) {
                        
                        long timeStamp = c.getLong (c.getColumnIndexOrThrow(CompoDbContract.COLUMN_NAME_TIMESTAMP));
                        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                        cal.setTimeInMillis(timeStamp);
                        String date = DateFormat.format("dd-MM-yyyy HH:mm", cal).toString();

                        int total10 = c.getInt (c.getColumnIndexOrThrow(CompoDbContract.CompoWeightEntry.COLUMN_NAME_TOTAL));
                        float total = ((float) total10) / 10;
                        
                        int fat10 = c.getInt (c.getColumnIndexOrThrow(CompoDbContract.CompoWeightEntry.COLUMN_NAME_FAT));
                        float fat = ((float) fat10) / 10;

                        int water10 = c.getInt (c.getColumnIndexOrThrow(CompoDbContract.CompoWeightEntry.COLUMN_NAME_WATER));
                        float water = ((float) water10) / 10;

                        int muscle10 = c.getInt (c.getColumnIndexOrThrow(CompoDbContract.CompoWeightEntry.COLUMN_NAME_MUSCLE));
                        float muscle = ((float) muscle10) / 10;

                        int bone10 = c.getInt (c.getColumnIndexOrThrow(CompoDbContract.CompoWeightEntry.COLUMN_NAME_BONE));
                        float bone = ((float) bone10) / 10;

                        // Save string output
                        output.append(String.format(
                                getString(R.string.weight_entry_plaintext_short)
                                , date
                                , total
                                , fat
                                , water
                                , muscle
                                , bone));
                        
                        // Save Entry output
                        datetimeLabels.add(date);
                        valsTotalBodyWeight.add(new Entry(total, index));
                        valsTotalFatWeight.add(new Entry(fat, index));
                        valsTotalWaterWeight.add(new Entry(water, index));
                        valsTotalMuscleWeight.add(new Entry(muscle, index));
                        valsTotalBoneWeight.add(new Entry(bone, index));
                        ++index;
                    }
                }


                // Prepare data for chart
                LineDataSet setTotal = new LineDataSet(valsTotalBodyWeight, getString(R.string.body_weight));
                setTotal.setAxisDependency(YAxis.AxisDependency.LEFT);
                LineDataSet setFat = new LineDataSet(valsTotalFatWeight, getString(R.string.fat_mass));
                setFat.setAxisDependency(YAxis.AxisDependency.LEFT);
                LineDataSet setWater = new LineDataSet(valsTotalWaterWeight, getString(R.string.water_weight));
                setWater.setAxisDependency(YAxis.AxisDependency.LEFT);
                LineDataSet setMuscle = new LineDataSet(valsTotalMuscleWeight, getString(R.string.muscle_mass));
                setMuscle.setAxisDependency(YAxis.AxisDependency.LEFT);
                LineDataSet setBone = new LineDataSet(valsTotalBoneWeight, getString(R.string.bone_mass));
                setBone.setAxisDependency(YAxis.AxisDependency.LEFT);

                ArrayList<ILineDataSet> weightDataSets = new ArrayList<ILineDataSet>();
                weightDataSets.add(setTotal);
                weightDataSets.add(setFat);
                weightDataSets.add(setWater);
                weightDataSets.add(setMuscle);
                weightDataSets.add(setBone);

                LineData weightData = new LineData(datetimeLabels, weightDataSets);
                LineChart chart = (LineChart) findViewById(R.id.weight_chart);
                chart.setData(weightData);
                chart.invalidate();

                // Display text results
                EditText textView = (EditText)findViewById(R.id.outputText);
                textView.setText(output.toString());

                Snackbar.make(view, "Data has been refreshed", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();


    }
}
