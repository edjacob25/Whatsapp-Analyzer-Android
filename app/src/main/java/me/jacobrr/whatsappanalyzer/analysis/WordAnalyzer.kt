package me.jacobrr.whatsappanalyzer.analysis

/**
 * Created by jacob on 25/11/2015.
 */
class WordAnalyzer {
    internal var definedWords = HashMap<String, Word>()

    init {
        //new Word       (name,               agre,fear,happ,love,sadn,emoji,swear)
        definedWords.put("hola", Word("hola", 0.0f, 0.0f, 0.2f, 0.1f, 0.0f, false, false))
        definedWords.put("adios", Word("adios", 0.1f, 0.0f, 0.0f, 0.0f, 0.5f, false, false))
        definedWords.put("amor", Word("amor", 0.0f, 0.2f, 0.5f, 1.0f, 0.0f, false, false))
        definedWords.put("quiero", Word("quiero", 0.2f, 0.0f, 0.0f, 0.5f, 0.2f, false, false))
        definedWords.put("puto", Word("puto", 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, false, true))
        definedWords.put("pinche", Word("pinche", 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, false, true))
        definedWords.put("wey", Word("wey", 0.2f, 0.0f, 0.0f, 0.0f, 0.0f, false, true))
        definedWords.put("pendejo", Word("pendejo", 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, false, true))
        definedWords.put("estupido", Word("estupido", 0.7f, 0.0f, 0.0f, 0.0f, 0.0f, false, true))
        definedWords.put("verga", Word("verga", 0.3f, 0.0f, 0.0f, 0.0f, 0.5f, false, true))
        definedWords.put("mañana", Word("mañana", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, false, false))
        definedWords.put("salir", Word("salir", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, false, false))
        definedWords.put("noche", Word("anoche", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, false, false))
    }

    fun analyze(name: String, messages: List<String>): Person {
        val p = Person(name)
        val step = if (messages.size < 200) 1 else Math.floor((messages.size / 200).toDouble()).toInt()

        var i = 0
        while (i < messages.size) {
            val words = messages[i].split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            words.filter { it.lowercase() in definedWords }.map { p.addNewWord(definedWords[it.lowercase()]!!) }
            i += step
        }
        return p
    }

}
