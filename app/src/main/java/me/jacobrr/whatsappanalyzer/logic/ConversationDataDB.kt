package me.jacobrr.whatsappanalyzer.logic

import java.io.Serializable
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

/**
 * Created by jacob on 22/11/2015.
 * Sister class of ConversationData, it's used to store part of the data to the local DB
 */
class ConversationDataDB : IConversationData {
    //<Name, <messages, MessageShare, WordsAvg>>
    private var id: Int? = null
    private var participantsMap: Map<String, Triplet> = HashMap()
    private var mostTalkedDayT: TupleB
    private var mostTalkedMonthT: TupleA
    override var totalMessages: Int = 0
        private set
    override val participants: List<String>
        get() = participantsMap.keys.toList()
    override val mostTalkedDay: Date
        get() = mostTalkedDayT.x
    override var totalDaysTalked: Int = 0
        private set
    override val mostTalkedMonth: String
        get() = mostTalkedMonthT.x
    override var dailyAvg: Float = 0.toFloat()
        private set
    override var realDailyAvg: Float = 0.toFloat()
        private set
    override var conversationName: String = ""
        private set

    val monthDB: String
        get() = mostTalkedMonth

    val dayDB: String
        get() = mostTalkedDay.toString()

    val participantsDB: String
        get() {
            var res = ""
            for (s in participantsMap.keys) {
                res = res + "|" + s + "-" + participantsMap[s].toString()
            }
            return res
        }

    constructor(id: Int, dailyAvg: Float, mostTalkedDay: String, mostTalkedMonth: String, participants: String, realDailyAvg: Float, totalDaysTalked: Int, totalMessages: Int, conversationName: String) {
        this.id = id
        this.dailyAvg = dailyAvg
        this.mostTalkedDayT = TupleB(mostTalkedDay)
        this.mostTalkedMonthT = TupleA(mostTalkedMonth)
        val a = HashMap<String, Triplet>()
        val parts = participants.split("\\|".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for (part in parts) {
            if (part != "") {
                val minipart = part.split("\\-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                a[minipart[0]] = Triplet(minipart[1])
            }
        }
        this.participantsMap = a
        this.conversationName = conversationName
        this.realDailyAvg = realDailyAvg
        this.totalDaysTalked = totalDaysTalked
        this.totalMessages = totalMessages
    }

    constructor(cv: IConversationData) {
        this.mostTalkedDayT = TupleB(cv.mostTalkedDay, cv.getDayData(cv.mostTalkedDay))
        this.mostTalkedMonthT = TupleA(cv.mostTalkedMonth, cv.getMonthData(cv.mostTalkedMonth))
        val n = HashMap<String, Triplet>()
        for (s in cv.participants) {
            n[s] = Triplet(cv.getParticipantCount(s), cv.getParticipantShare(s), cv.getWordsAvg(s))
        }
        this.participantsMap = n
        this.totalMessages = cv.totalMessages
        this.totalDaysTalked = cv.totalDaysTalked
        this.dailyAvg = cv.dailyAvg
        this.realDailyAvg = cv.realDailyAvg
        this.conversationName = cv.conversationName
    }

    override fun getParticipantCount(pt: String): Int {
        return participantsMap[pt]!!.x
    }

    override fun getParticipantShare(pt: String): Float {
        return participantsMap[pt]!!.y
    }

    override fun getWordsAvg(pt: String): Float {
        return participantsMap[pt]!!.z
    }


    override fun getMonthData(month: String): Int {
        return mostTalkedMonthT.y
    }

    override fun getDayData(date: Date): Int {
        return mostTalkedDayT.y
    }
}

internal class TupleA : Serializable {
    var x: String
    var y: Int = 0

    constructor(s: String) {
        val parts = s.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        this.x = parts[0]
        this.y = Integer.parseInt(parts[1])
    }

    constructor(x: String, y: Int) {
        this.x = x
        this.y = y
    }

    override fun toString(): String {
        return "$x,$y"
    }
}

internal class TupleB : Serializable {
    var x: Date
    var y: Int

    constructor(s: String) {
        val parts = s.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        try {
            this.x = sdf.parse(parts[0])!!
        } catch (e: ParseException) {
            e.printStackTrace()
            this.x = Date()
        }

        this.y = Integer.parseInt(parts[1])
    }

    constructor(x: Date, y: Int) {
        this.x = x
        this.y = y
    }

    override fun toString(): String {
        return sdf.format(x) + "," + y
    }

    companion object {
        var sdf = SimpleDateFormat("dd/MM/yyyy")
    }
}

internal class Triplet : Serializable {
    val x: Int
    val y: Float
    val z: Float

    constructor(x: Int, y: Float, z: Float) {
        this.x = x
        this.y = y
        this.z = z
    }

    constructor(s: String) {
        val parts = s.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        this.x = Integer.parseInt(parts[0])
        this.y = java.lang.Float.parseFloat(parts[1])
        this.z = java.lang.Float.parseFloat(parts[2])
    }

    override fun toString(): String {
        return "$x,$y,$z"
    }
}