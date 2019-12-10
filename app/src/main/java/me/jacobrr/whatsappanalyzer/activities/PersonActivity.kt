package me.jacobrr.whatsappanalyzer.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_person.*
import me.jacobrr.whatsappanalyzer.Constants
import me.jacobrr.whatsappanalyzer.R
import java.util.*

class PersonActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person)

        Log.i("Persona", Constants.person.toString())
        person_view_aggressiveness.text = Constants.person?.aggressiveness?.let { getFormattedString(it) }
        person_view_happiness.text = Constants.person?.happiness?.let { getFormattedString(it) }
        person_view_fear.text = Constants.person?.fear?.let { getFormattedString(it) }
        person_view_love.text = Constants.person?.love?.let { getFormattedString(it) }
        person_view_sadness.text = Constants.person?.sadness?.let { getFormattedString(it) }
    }

    private fun getFormattedString(param: Float): String {
        return String.format(Locale.getDefault(), "%.0f%%", param * 100)
    }
}
