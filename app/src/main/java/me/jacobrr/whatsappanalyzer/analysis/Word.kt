package me.jacobrr.whatsappanalyzer.analysis

/**
 * Created by jacob on 25/11/2015.
 */
class Word(var name: String, aggressiveness: Float, fear: Float, happiness: Float, love: Float, sadness: Float, internal var emoticon: Boolean, internal var swearWord: Boolean) : SentimentData(aggressiveness, fear, happiness, love, sadness) {

    override fun equals(other: Any?): Boolean {
        if (other!!.javaClass == String::class.java)
            return name == other

        if (other.javaClass == Word::class.java)
            return (other as Word).name == this.name

        return false
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}
