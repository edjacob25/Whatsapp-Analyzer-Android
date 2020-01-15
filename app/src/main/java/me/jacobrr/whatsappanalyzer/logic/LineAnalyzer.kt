package me.jacobrr.whatsappanalyzer.logic

import android.util.Log
import java.text.DateFormat.SHORT
import java.text.DateFormat.getTimeInstance
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.format.FormatStyle
import java.util.*


/**
 * Created by jacob on 20/11/2015.
 */
class LineAnalyzer {
    private val dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)
    private val timeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
    private val timeFormatter2 = getTimeInstance(SHORT)
    private val regex = Regex("(.+)\\s(\\d{1,2}:\\d{2}.*?)\\s+-\\s+(.+?):\\s+(.+)")
    private var useOldFormatter = false
    private val zone = ZoneId.systemDefault()

    fun getTimeOfTheDay(time: LocalTime): String {
        return when (time.hour) {
            in 1..5 -> "Early morning"
            in 6..11 -> "Morning"
            in 12..18 -> "Afternoon"
            else -> "Night"
        }
    }

    fun analyzeLine(line: String): AnalyzedLine? {
        val lineCleaned = line.replace('\u00A0',' ')
        return try {
            val match = regex.matchEntire(lineCleaned) ?: throw MatchException()

            val name = match.groupValues[3]
            val message = match.groupValues[4]

            val date = parseDate(match.groupValues[1]) ?: throw MyDateTimeException()
            val hour = parseTime(match.groupValues[2]) ?: throw MyDateTimeException()
            AnalyzedLine(name, message, date, getTimeOfTheDay(hour))

        } catch (e: MatchException) {
            Log.e("LINE", "Line does not fit the pattern for a message")
            Log.e("LINE", "Line with error: \"$line\"")
            null
        } catch (e: MyDateTimeException) {
            Log.e("DATE", "Could not parse Date in line $line")
            null
        }
    }

    private fun parseDate(dateStr: String): LocalDate? {
        return try {
            LocalDate.parse(dateStr, dateFormatter)
        } catch (e: DateTimeParseException) {
            Log.e("DATE", "Could not parse Date", e)
            null
        }
    }

    private fun parseTime(dateStr: String): LocalTime? {
        return if (!useOldFormatter) {
            try {
                LocalTime.parse(dateStr, timeFormatter)
            } catch (e: DateTimeParseException) {
                Log.e("TIME", "Could not parse Time with formatter from java.time, format is " +
                        "${LocalTime.now().format(timeFormatter)} and date passed is $dateStr")
                try {
                    useOldFormatter = true
                    Log.d("TIME", "Parsing with old formatter")
                    Instant.ofEpochMilli(timeFormatter2.parse(dateStr)!!.time).atZone(zone).toLocalTime()
                } catch (e: Exception) {
                    Log.e("TIME", "Could not parse time with any formatter, old formatter is " +
                            "${timeFormatter2.format(Date())} and date passed is $dateStr")
                    useOldFormatter = false
                    null
                }
            }
        } else {
            try {
                Instant.ofEpochMilli(timeFormatter2.parse(dateStr)!!.time).atZone(zone).toLocalTime()
            } catch (e: Exception) {
                Log.e("TIME", "Could not parse time with any formatter, old formatter is " +
                        "${timeFormatter2.format(Date())} and date passed is $dateStr")
                null
            }
        }
    }

    class MatchException : Exception("Line does not match regex, is a continuation of the last message")

    class MyDateTimeException : Exception("Could not parse date or time in message")
}
