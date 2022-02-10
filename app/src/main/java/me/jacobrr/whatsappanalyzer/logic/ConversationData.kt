package me.jacobrr.whatsappanalyzer.logic

import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import me.jacobrr.whatsappanalyzer.analysis.Person
import me.jacobrr.whatsappanalyzer.analysis.WordAnalyzer
import java.text.DateFormat
import java.text.DateFormat.getDateInstance
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * Created by jacob on 20/11/2015.s
 */
class ConversationData(override val conversationName: String) : IConversationData {
    private val participantsMap = HashMap<String, Int>()
    private val participantsWords = HashMap<String, Int>()
    private val messages = HashMap<String, List<String>>()
    private val days = TreeMap<LocalDate, Int>()
    private val totalDays = TreeMap<LocalDate, Int>()
    private val months = HashMap<String, Int>()
    private val timeofDay = HashMap<String, Int>()
    private val personData: MutableList<Person> = ArrayList()

    override val participants: List<String>
        get() = participantsMap.keys.toList()
    override val mostTalkedDay: LocalDate
        get() = days.maxByOrNull { it.value }!!.key
    override val totalDaysTalked: Int
        get() = totalDays.size
    override val mostTalkedMonth: String
        get() = months.maxByOrNull { it.value }!!.key
    override val totalMessages: Int
        get() = participantsMap.map { it.value }.sum()
    override val dailyAvg: Float
        get() = totalMessages.toFloat() / days.size
    override val realDailyAvg: Float
        get() = totalMessages.toFloat() / totalDays.size

    private val dateFormatter = getDateInstance(DateFormat.SHORT)
    private val monthFormatter = DateTimeFormatter.ofPattern("MM/yyyy")

    fun addData(participant: String, message: String, date: LocalDate, time: String) {
        val numMess = participantsMap[participant] ?: 0
        participantsMap[participant] = numMess + 1

        val numDay = days[date] ?: 0
        days[date] = numDay + 1

        val wordsAcc = participantsWords[participant] ?: 0
        val words = message.split(" ").dropLastWhile { it.isEmpty() }.size
        participantsWords[participant] = wordsAcc + words

        val timeCount = timeofDay[time] ?: 0
        timeofDay[time] = timeCount + 1

        val msgsList = messages[participant]?.toMutableList() ?: mutableListOf()
        msgsList.add(message)
        messages[participant] = msgsList
    }

    fun addData(a: AnalyzedLine?) {
        if (a != null) {
            addData(a.participant, a.message, a.date, a.hours)
        }
    }

    fun createMonthsData() {
        for (iterator in days.keys) {
            months.merge(
                monthFormatter.format(iterator),
                days.getValue(iterator)
            ) { currentValue, daysValue -> currentValue + daysValue }
        }
    }

    fun createTotalDaysData() {
        var iterator = days.firstKey()
        val now = LocalDate.now()
        while (iterator.isBefore(now)) {
            iterator = iterator.plusDays(1)
            totalDays[iterator] = if (days.containsKey(iterator)) days.getValue(iterator) else 0
        }
    }

    fun createPeopleData() {
        val wa = WordAnalyzer()
        participantsMap.keys.map { personData.add(wa.analyze(it, messages.getValue(it))) }
    }

    fun getPersonData(i: Int): Person {
        return personData[i]
    }

    override fun getDayData(date: LocalDate): Int {
        return days.getValue(date)
    }

    override fun getMonthData(month: String): Int {
        return months.getValue(month)
    }

    override fun getParticipantCount(pt: String): Int {
        return participantsMap.getValue(pt)
    }

    override fun getParticipantShare(pt: String): Float {
        return (participantsMap.getValue(pt) * 100.0).toFloat() / totalMessages
    }

    override fun getWordsAvg(pt: String): Float {
        return participantsWords.getValue(pt) / participantsMap.getValue(pt).toFloat()
    }

    val chartDaysData: Pair<LineData, List<String>>
        get() {
            var i = 0
            val vals = ArrayList<Entry>()
            val axis = ArrayList<String>()


            days.map {
                vals.add(Entry(i++.toFloat(), it.value.toFloat()))
                val instant = it.key.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()
                axis.add(dateFormatter.format(Date.from(instant)))
            }

            val dt = LineDataSet(vals, "Messages")
            return Pair(LineData(dt), axis)
        }

    val allDaysChartData: Pair<LineData, List<String>>
        get() {
            var i = 0
            val vals = ArrayList<Entry>()
            val axis = ArrayList<String>()

            totalDays.map {
                vals.add(Entry((i++).toFloat(), it.value.toFloat()))
                val instant = it.key.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()
                axis.add(dateFormatter.format(Date.from(instant)))
            }
            val dt = LineDataSet(vals, "Messages")

            dt.axisDependency = YAxis.AxisDependency.LEFT
            return Pair(LineData(dt), axis)
        }

    val timeChartData: PieData
        get() {
            val vals = ArrayList<PieEntry>()

            timeofDay.map {
                vals.add(PieEntry(it.value.toFloat(), it.key))
            }

            val dt = PieDataSet(vals, "Time of the day")
            dt.axisDependency = YAxis.AxisDependency.LEFT
            return PieData(dt)
        }
}
