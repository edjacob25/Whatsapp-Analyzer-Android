package me.jacobrr.whatsappanalyzer.logic

import java.util.Date

/**
 * Created by jacob on 22/11/2015.
 */
interface IConversationData {
    val participants: List<String>

    val mostTalkedDay: Date

    val totalDaysTalked: Int

    val mostTalkedMonth: String

    val totalMessages: Int

    val dailyAvg: Float

    val realDailyAvg: Float

    val conversationName: String

    fun getParticipantCount(pt: String): Int

    fun getParticipantShare(pt: String): Float

    fun getWordsAvg(pt: String): Float

    fun getDayData(date: Date): Int

    fun getMonthData(month: String): Int
}
