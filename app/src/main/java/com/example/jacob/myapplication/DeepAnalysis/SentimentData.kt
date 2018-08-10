package com.example.jacob.myapplication.DeepAnalysis

/**
 * Created by jacob on 25/11/2015.
 */
abstract class SentimentData(aggressiveness: Float, fear: Float, happiness: Float, love: Float, sadness: Float) {

    var aggressiveness: Float = 0.toFloat()
        internal set
    var love: Float = 0.toFloat()
        internal set
    var happiness: Float = 0.toFloat()
        internal set
    var fear: Float = 0.toFloat()
        internal set
    var sadness: Float = 0.toFloat()
        internal set

    init {
        this.aggressiveness = aggressiveness
        this.fear = fear
        this.happiness = happiness
        this.love = love
        this.sadness = sadness
    }
}
