package ca.cinderblok.compotracker.Models;

import android.content.ContentValues;

import ca.cinderblok.compotracker.DAL.CompoDbContract;

/**
 * Created by rachelhartviksen on 16-04-28.
 */
public class WeightEntry {

    private Integer bodyWeightInt10;

    private Integer fatWeightInt10;

    private Integer waterWeightInt10;

    private Integer muscleWeightInt10;

    private Integer boneWeightInt10;
    
    public Long TimeStamp;

    public Float BodyWeight() { return bodyWeightInt10 / 10F; }

    public Float FatWeight() { return fatWeightInt10 / 10F; }

    public Float WaterWeight() { return waterWeightInt10 / 10F; }

    public Float MuscleWeight() { return muscleWeightInt10 / 10F; }

    public Float BoneWeight() { return boneWeightInt10 / 10F; }

    public ContentValues GetContent10Values() {
        ContentValues percent10Values = new ContentValues();
        percent10Values.put(CompoDbContract.COLUMN_NAME_TIMESTAMP, this.TimeStamp);
        percent10Values.put(CompoDbContract.CompoWeightEntry.COLUMN_NAME_TOTAL, bodyWeightInt10);
        percent10Values.put(CompoDbContract.CompoWeightEntry.COLUMN_NAME_FAT, fatWeightInt10);
        percent10Values.put(CompoDbContract.CompoWeightEntry.COLUMN_NAME_WATER, waterWeightInt10);
        percent10Values.put(CompoDbContract.CompoWeightEntry.COLUMN_NAME_MUSCLE, muscleWeightInt10);
        percent10Values.put(CompoDbContract.CompoWeightEntry.COLUMN_NAME_BONE, boneWeightInt10);
        return percent10Values;
    }
    
    public WeightEntry(InputEntry entry) {
        this.TimeStamp = entry.TimeStamp;
        this.bodyWeightInt10 = Math.round(entry.BodyWeight * 10);
        this.fatWeightInt10 = Math.round(entry.FatPercent * entry.BodyWeight / 10);
        this.waterWeightInt10 = Math.round(entry.WaterPercent * entry.BodyWeight / 10);
        this.muscleWeightInt10 = Math.round(entry.MusclePercent * entry.BodyWeight / 10);
        this.boneWeightInt10 = Math.round(entry.BoneWeight * 10);
    }

}
