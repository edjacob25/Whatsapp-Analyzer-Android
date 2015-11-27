package com.example.jacob.myapplication.DeepAnalysis;

/**
 * Created by jacob on 25/11/2015.
 */
public abstract class SentimentData {

    float aggresiveness;
    float love;
    float happiness;
    float fear;
    float sadness;

    public SentimentData(float aggresiveness, float fear, float happiness, float love, float sadness) {
        this.aggresiveness = aggresiveness;
        this.fear = fear;
        this.happiness = happiness;
        this.love = love;
        this.sadness = sadness;
    }

    @Override
    public String toString() {
        return "SentimentData{" +
                "aggresiveness=" + aggresiveness +
                ", love=" + love +
                ", happiness=" + happiness +
                ", fear=" + fear +
                ", sadness=" + sadness +
                '}';
    }

    public float getAggresiveness() {
        return aggresiveness;
    }

    public float getFear() {
        return fear;
    }

    public float getHappiness() {
        return happiness;
    }

    public float getLove() {
        return love;
    }

    public float getSadness() {
        return sadness;
    }
}
