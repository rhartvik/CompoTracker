package ca.cinderblok.compotracker.Models;

import android.content.ContentValues;

import ca.cinderblok.compotracker.DAL.CompoDbContract;

/**
 * Created by rachelhartviksen on 16-04-29.
 */
public class PercentEntry {


    private Integer fatPercentInt10;

    private Integer waterPercentInt10;

    private Integer musclePercentInt10;

    private Integer bonePercentInt10;

    public Long TimeStamp;

    public Float FatPercent() { return fatPercentInt10 / 10F; }

    public Float WaterPercent() { return waterPercentInt10 / 10F; }

    public Float MusclePercent() { return musclePercentInt10 / 10F; }

    public Float BonePercent() { return bonePercentInt10 / 10F; }

    public ContentValues GetContent10Values() {
        ContentValues percent10Values = new ContentValues();
        percent10Values.put(CompoDbContract.COLUMN_NAME_TIMESTAMP, this.TimeStamp);
        percent10Values.put(CompoDbContract.CompoPercentEntry.COLUMN_NAME_FAT, fatPercentInt10);
        percent10Values.put(CompoDbContract.CompoPercentEntry.COLUMN_NAME_WATER, waterPercentInt10);
        percent10Values.put(CompoDbContract.CompoPercentEntry.COLUMN_NAME_MUSCLE, musclePercentInt10);
        percent10Values.put(CompoDbContract.CompoPercentEntry.COLUMN_NAME_BONE, bonePercentInt10);
        return percent10Values;
    }
    public PercentEntry(InputEntry entry) {
        this.TimeStamp = entry.TimeStamp;
        this.fatPercentInt10 = Math.round(entry.FatPercent * 10);
        this.waterPercentInt10 = Math.round(entry.WaterPercent * 10);
        this.musclePercentInt10 = Math.round(entry.MusclePercent * 10);
        this.bonePercentInt10 = Math.round(entry.BoneWeight * 1000F / entry.BodyWeight);
    }

}
