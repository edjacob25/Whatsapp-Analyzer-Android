package me.jacobrr.whatsappanalyzer

import me.jacobrr.whatsappanalyzer.analysis.Person
import me.jacobrr.whatsappanalyzer.db.DataDBHandler
import me.jacobrr.whatsappanalyzer.logic.IConversationData

/**
 * Created by jacob on 21/11/2015.
 */
object Constants {
    var conversationData: IConversationData? = null
    var dbHandler: DataDBHandler? = null
    var person: Person? = null
}
