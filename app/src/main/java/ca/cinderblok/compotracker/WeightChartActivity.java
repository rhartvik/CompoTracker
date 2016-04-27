package ca.cinderblok.compotracker;

import android.support.v4.util.Pair;
import java.util.HashMap;

public class WeightChartActivity extends ChartActivity {

    @Override
    protected int getChartTitleId(){
        return R.string.weight_chart;
    }

    @Override
    protected String getTableName(){
        return CompoDbContract.CompoWeightEntry.TABLE_NAME;
    }

    @Override
    protected HashMap<String, Pair<Integer, Integer>> getColumns(){
        return new HashMap<String, Pair<Integer, Integer>>()
        {{
            put(CompoDbContract.CompoWeightEntry.COLUMN_NAME_TOTAL, new Pair<Integer, Integer> (R.color.colorTotal, R.string.body_weight));
            put(CompoDbContract.CompoWeightEntry.COLUMN_NAME_FAT, new Pair<Integer, Integer> (R.color.colorFat, R.string.fat_mass));
            put(CompoDbContract.CompoWeightEntry.COLUMN_NAME_MUSCLE, new Pair<Integer, Integer> (R.color.colorMuscle, R.string.muscle_mass));
            put(CompoDbContract.CompoWeightEntry.COLUMN_NAME_WATER, new Pair<Integer, Integer> (R.color.colorWater, R.string.water_weight));
            put(CompoDbContract.CompoWeightEntry.COLUMN_NAME_BONE, new Pair<Integer, Integer> (R.color.colorBone, R.string.bone_mass));
        }};
    }

}
