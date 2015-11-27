package com.example.jacob.myapplication.DeepAnalysis;

import java.util.Objects;

/**
 * Created by jacob on 25/11/2015.
 */
public class Word extends SentimentData {
    public String name;
    boolean swearWord;
    boolean emoticon;

    public Word(String name, float aggresiveness, float fear, float happiness, float love, float sadness, boolean emoticon,  boolean swearWord) {
        super(aggresiveness, fear, happiness, love, sadness);
        this.emoticon = emoticon;
        this.name = name;
        this.swearWord = swearWord;
    }

    @Override
    public boolean equals(Object o) {
        if (o.getClass() == String.class)
            return name.equals(o);

        if (o.getClass() == Word.class)
            return ((Word) o).name.equals(this.name);

        return false;
    }
}
