package me.jacobrr.whatsappanalyzer.logic

import java.text.SimpleDateFormat

/**
 * Created by jacob on 20/11/2015.
 */
class LineAnalyzer {
    private var sdf = SimpleDateFormat("dd/MM/yy")

    fun getTimeOfTheDay(hour: Int): String {

        return when (hour) {
            in 1..5 -> "Early morning"
            in 6..11 -> "Morning"
            in 12..18 -> "Afternoon"
            else -> "Night"
        }
    }

    fun analyzeLine(line: String): AnalyzedLine? {
        try {

            val firstDivision = line.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val dateDivision = firstDivision[0].split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val textDivision = firstDivision[1].split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val name = textDivision[0].substring(1, textDivision[0].length)

            val message = textDivision[1]
            val date = sdf.parse(dateDivision[0])
            val hour = Integer.parseInt(dateDivision[1].split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0])

            return AnalyzedLine(name, message, date, getTimeOfTheDay(hour))

        } catch (e: Exception) {
            println(e)
            println("Error: $line")
            return null
        }

    }
}
