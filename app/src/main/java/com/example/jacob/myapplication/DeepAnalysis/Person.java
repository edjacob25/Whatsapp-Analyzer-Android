package com.example.jacob.myapplication.DeepAnalysis;

/**
 * Created by jacob on 25/11/2015.
 */
public class Person extends SentimentData {
    int wordsAnalyzed = 0;
    int emoticons = 0;
    int swears = 0;
    String name;

    public Person(String name) {
        super(0, 0, 0, 0, 0);
        this.wordsAnalyzed = 0;
        this.name = name;
    }

    public void addNewWord(Word w){
        float aggressiveness = this.aggresiveness * wordsAnalyzed;
        float hapiness = this.happiness * wordsAnalyzed;
        float fear = this.fear * wordsAnalyzed;
        float love = this.love * wordsAnalyzed;
        float sadness = this.sadness * wordsAnalyzed;
        wordsAnalyzed += 1;
        this.aggresiveness = (aggressiveness + w.aggresiveness) / wordsAnalyzed;
        this.happiness = (hapiness + w.happiness) / wordsAnalyzed;
        this.fear = (fear + w.fear) / wordsAnalyzed;
        this.love = (love + w.love) / wordsAnalyzed;
        this.sadness = (sadness + w.sadness) / wordsAnalyzed;


        if (w.swearWord)
            swears += 1;
        if (w.emoticon)
            emoticons += 1;
    }

    public int getWordsAnalyzed() {
        return wordsAnalyzed;
    }

    public int getSwears() {
        return swears;
    }

    public String getName() {
        return name;
    }

    public int getEmoticons() {
        return emoticons;
    }
    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", swears=" + swears +
                ", wordsAnalyzed=" + wordsAnalyzed +
                ", emoticons=" + emoticons +
                '}';
    }
}
