package me.jacobrr.whatsappanalyzer.logic

import java.text.DateFormat
import java.text.DateFormat.getDateInstance
import java.text.DateFormat.getTimeInstance
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId


/**
 * Created by jacob on 20/11/2015.
 */
class LineAnalyzer {
    private val dateFormatter = getDateInstance(DateFormat.SHORT)
    private val timeFormatter = getTimeInstance(DateFormat.SHORT)
    private val regex = Regex("(.+)\\s(\\d{1,2}:\\d{2}.*?)\\s+-\\s+(.+?):\\s+(.+)")

    fun getTimeOfTheDay(time: LocalTime): String {
        return when (time.hour) {
            in 1..5 -> "Early morning"
            in 6..11 -> "Morning"
            in 12..18 -> "Afternoon"
            else -> "Night"
        }
    }

    fun analyzeLine(line: String): AnalyzedLine? {
        return try {
            val match = regex.matchEntire(line) ?: throw MatchExpection()

            val name = match.groupValues[3]
            val message = match.groupValues[4]

            val date = Instant.ofEpochMilli(dateFormatter.parse(match.groupValues[1])!!.time).atZone(ZoneId.systemDefault()).toLocalDate()
            val hour = Instant.ofEpochMilli(timeFormatter.parse(match.groupValues[2])!!.time).atZone(ZoneId.systemDefault()).toLocalTime()
            AnalyzedLine(name, message, date, getTimeOfTheDay(hour))

        } catch (e: Exception) {
            println(e)
            println("Line with error: \"$line\"")
            null
        }
    }

    class MatchExpection : Exception("Line does not match regex, is a continuation of the last message")
}
