package com.example.jacob.myapplication.logic

import com.example.jacob.myapplication.analysis.Person
import com.example.jacob.myapplication.analysis.WordAnalyzer
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by jacob on 20/11/2015.s
 */
class ConversationData(private val conversationName: String) : IConversationData {
    internal var sdf = SimpleDateFormat("dd/MM/yyyy")
    private val participants = HashMap<String, Int>()
    private val participantsWords = HashMap<String, Int>()
    private val messages = HashMap<String, List<String>>()
    private val days = TreeMap<Date, Int>()
    private val totalDays = TreeMap<Date, Int>()
    private val months = HashMap<String, Int>()
    private val timeofDay = HashMap<String, Int>()
    private val personData: MutableList<Person> = ArrayList()

    fun addData(participant: String, message: String, date: Date, time: String) {
        val numMess = participants[participant]
        participants.put(participant, if (numMess == null) 1 else numMess + 1)

        val numDay = days[date]
        days.put(date, if (numDay == null) 1 else numDay + 1)

        /* -1 added for the first space at the start */
        val wordsAcc = participantsWords[participant]
        val words = message.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size - 1
        participantsWords.put(participant, if (wordsAcc == null) words else wordsAcc + words)

        /*if (!message.equals(" <Archivo omitido>"))
            messages.add(message);*/

        val timeCount = timeofDay[time]
        timeofDay.put(time, if (timeCount == null) 1 else timeCount + 1)

        var msgsList: ArrayList<String>? = messages[participant] as ArrayList<String>?
        if (msgsList == null)
            msgsList = ArrayList<String>()
        msgsList.add(message)
        messages.put(participant, msgsList)
    }

    fun addData(a: AnalyzedLine?) {
        if (a != null) {
            addData(a.participant, a.message, a.date, a.hours)
        }

    }

    fun createMonthsData() {
        val sdf = SimpleDateFormat("MM-yyyy")
        for (iterator in days.keys) {
            months.merge(sdf.format(iterator), days.getValue(iterator), { currentValue, daysValue -> currentValue + daysValue })
        }
    }

    fun createTotalDaysData() {
        var iterator = Date(days.firstKey().time)
        val last = Date(System.currentTimeMillis())
        val calendar = Calendar.getInstance()
        calendar.time = iterator
        while (iterator.before(last)) {
            iterator = calendar.time
            totalDays.put(iterator, if (days.containsKey(iterator)) days.getValue(iterator) else 0)
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }
    }

    fun createPeopleData() {
        val wa = WordAnalyzer()
        participants.keys.map { personData.add(wa.analyze(it, messages.getValue(it))) }
    }

    fun getPersonData(i: Int): Person {
        return personData[i]
    }

    override fun getParticipants(): List<String> {
        return ArrayList(participants.keys)
    }

    override fun getMostTalkedDay(): Date {
        return days.maxBy { it.value }?.key ?: Date(0)
    }

    override fun getMostTalkedMonth(): String {
        return months.maxBy { it.value }!!.key
    }

    override fun getConversationName(): String {
        return conversationName
    }

    override fun getDayData(date: Date): Int {
        return days.getValue(date)
    }

    override fun getMonthData(month: String): Int {
        return months.getValue(month)
    }

    override fun getTotalMessages(): Int {
        return participants.map { it.value }.sum()
    }

    override fun getTotalDaysTalked(): Int {
        return totalDays.size
    }

    override fun getParticipantCount(pt: String): Int {
        return participants.getValue(pt)
    }

    override fun getParticipantShare(pt: String): Float {
        return (participants.getValue(pt) * 100.0).toFloat() / totalMessages
    }

    override fun getWordsAvg(pt: String): Float {
        return participantsWords.getValue(pt) / participants.getValue(pt).toFloat()
    }

    override fun getDailyAvg(): Float {
        return totalMessages.toFloat() / days.size
    }

    override fun getRealDailyAvg(): Float {
        return totalMessages.toFloat() / totalDays.size
    }

    val chartDaysData: Pair<LineData, List<String>>
        get() {
            var i = 0
            val vals = ArrayList<Entry>()
            val axis = ArrayList<String>()


            days.map {
                vals.add(Entry(i++.toFloat(), it.value.toFloat()))
                axis.add(sdf.format(it.key))
            }

            val dt = LineDataSet(vals, "Messages")
            //dt.axisDependency = YAxis.AxisDependency.LEFT
            return Pair(LineData(dt), axis)
        }

    val allDaysChartData: Pair<LineData, List<String>>
        get() {
            var i = 0
            val vals = ArrayList<Entry>()
            val axis = ArrayList<String>()

            totalDays.map {
                vals.add(Entry((i++).toFloat(), it.value.toFloat()))
                axis.add(sdf.format(it.key))
            }
            val dt = LineDataSet(vals, "Messages")

            dt.axisDependency = YAxis.AxisDependency.LEFT
            return Pair(LineData(dt), axis)
        }
    /*
    public List<PieChart.Data> getChartParticipantsData(){
        List<PieChart.Data> data = new ArrayList<>();
        for (String s : participants.keySet()) {
            data.add(new PieChart.Data(s, getParticipantShare(s)));
        }
        return data;
    }*/

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
    /*
    public Dataset getDaysDataSet() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        for (Date iterator : days.keySet()) {
            dataset.addValue(days.get(iterator), "Mensajes por dia", sdf.format(iterator));
        }
        return dataset;
    }

    public Dataset getTotalDaysDataSet() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        for (Date iterator : totalDays.keySet()) {
            dataset.addValue(totalDays.get(iterator),"Mensajes por dia",sdf.format(iterator));
        }
        return dataset;
    }*/
}
