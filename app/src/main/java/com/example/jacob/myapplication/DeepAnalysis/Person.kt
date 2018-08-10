package com.example.jacob.myapplication.DeepAnalysis

/**
 * Created by jacob on 25/11/2015.
 */
class Person(name: String) : SentimentData(0f, 0f, 0f, 0f, 0f) {
    var wordsAnalyzed = 0
        internal set
    var emoticons = 0
        internal set
    var swears = 0
        internal set
    var name: String
        internal set

    init {
        this.wordsAnalyzed = 0
        this.name = name
    }

    fun addNewWord(w: Word) {
        val aggressiveness = this.aggressiveness * wordsAnalyzed
        val happiness = this.happiness * wordsAnalyzed
        val fear = this.fear * wordsAnalyzed
        val love = this.love * wordsAnalyzed
        val sadness = this.sadness * wordsAnalyzed
        wordsAnalyzed += 1
        this.aggressiveness = ((aggressiveness + w.aggressiveness) / wordsAnalyzed)
        this.happiness = ((happiness + w.happiness) / wordsAnalyzed)
        this.fear = ((fear + w.fear) / wordsAnalyzed)
        this.love = ((love + w.love) / wordsAnalyzed)
        this.sadness = ((sadness + w.sadness) / wordsAnalyzed)


        if (w.swearWord)
            swears += 1
        if (w.emoticon)
            emoticons += 1
    }
}
