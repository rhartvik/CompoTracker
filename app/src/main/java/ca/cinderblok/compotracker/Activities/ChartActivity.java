package ca.cinderblok.compotracker.Activities;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;

import ca.cinderblok.compotracker.R;
import ca.cinderblok.compotracker.DAL.CompoDbContract;
import ca.cinderblok.compotracker.DAL.CompoDbHelper;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Vector;

public abstract class ChartActivity extends AppCompatActivity {

    private SQLiteDatabase mReadableDb;
    private CompoDbHelper mDbHelper;

    protected abstract int getChartTitleId();
    protected abstract String getTableName();
    protected abstract String getIdColumnName();
    protected abstract HashMap<String, Pair<Integer, Integer>> getColumns();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getChartTitleId()); // TO DO: Does this work?

        mDbHelper = new CompoDbHelper(this, CompoDbContract.DATABASE_NAME, null, CompoDbContract.DATABASE_VERSION);

        //Make this a refresh button?
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sortOrder = CompoDbContract.COLUMN_NAME_TIMESTAMP + " DESC";
                HashMap<String, Pair<Integer, Integer>> columns = getColumns();
                String tableName = getTableName();
                // To store text output
                StringBuilder output = new StringBuilder();

                // To store output as Entrys for MPAndroidChart
                ArrayList<String> datetimeLabels = new ArrayList<String>();
                HashMap<String, ArrayList<Entry>> arrayLists = new HashMap<String, ArrayList<Entry>>();
                for (String columnName : columns.keySet()) {
                    arrayLists.put(columnName, new ArrayList<Entry>());
                }

                int numberOfColumns = columns.size();

                String[] projection = new String[numberOfColumns + 2];
                projection[0] = CompoDbContract.COLUMN_NAME_TIMESTAMP;
                projection[1] = getIdColumnName();
                int i = 1;
                for (String columnName : columns.keySet()) {
                    projection[++i] = columnName;
                }

                // Query data
                try (Cursor c = mReadableDb.query(tableName
                        ,projection
                        ,null
                        ,null
                        ,null
                        ,null
                        ,sortOrder);) {
                    if (c != null) {
                        // Keep track of index for MPAndroid Entrys
                        int index = 0;
                        while (c.moveToNext()) {
                            long timeStamp = c.getLong (c.getColumnIndexOrThrow(CompoDbContract.COLUMN_NAME_TIMESTAMP));
                            Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                            cal.setTimeInMillis(timeStamp);
                            String date = DateFormat.format("dd-MM-yyyy HH:mm", cal).toString();
                            Vector<Float> values = new Vector<Float>();
                            for (String columnName : columns.keySet()) {
                                int value10 = c.getInt (c.getColumnIndexOrThrow(columnName));
                                values.add(((float) value10) / 10);
                                arrayLists.get(columnName).add(new Entry(((float) value10) / 10, index));
                            }
                            ++index;
                            // Save string output
                            output.append(String.format(
                                    getString(R.string.weight_entry_plaintext_short)
                                    , date
                                    ,values.elementAt(0)
                                    ,values.elementAt(1)
                                    ,values.elementAt(2)
                                    ,values.elementAt(3)
                                    ,values.elementAt(4)));

                            // Save Entry output
                            datetimeLabels.add(date);
                        }
                    }
                }

                ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
                for (String columnName : columns.keySet()) {
                    int lineColor = getResources().getColor(columns.get(columnName).first);
                    LineDataSet lineDataSet = new LineDataSet(
                            arrayLists.get(columnName)
                            , getString(columns.get(columnName).second));
                    lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
                    lineDataSet.setColor(lineColor);
                    lineDataSet.setCircleColor(lineColor);
                    dataSets.add(lineDataSet);
                }


                LineData lineData = new LineData(datetimeLabels, dataSets);
                LineChart chart = (LineChart) findViewById(R.id.line_chart);
                chart.setData(lineData);
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
        mReadableDb = mDbHelper.getReadableDatabase();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mReadableDb.close();
    }
}
