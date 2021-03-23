package me.jacobrr.whatsappanalyzer.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import me.jacobrr.whatsappanalyzer.Constants
import me.jacobrr.whatsappanalyzer.databinding.ActivityPersonBinding
import java.util.*

class PersonActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPersonBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.i("Persona", Constants.person.toString())
        binding.apply {
            personViewAggressiveness.text = Constants.person?.aggressiveness?.let { getFormattedString(it) }
            personViewHappiness.text = Constants.person?.happiness?.let { getFormattedString(it) }
            personViewFear.text = Constants.person?.fear?.let { getFormattedString(it) }
            personViewLove.text = Constants.person?.love?.let { getFormattedString(it) }
            personViewSadness.text = Constants.person?.sadness?.let { getFormattedString(it) }
        }
    }

    private fun getFormattedString(param: Float): String {
        return String.format(Locale.getDefault(), "%.0f%%", param * 100)
    }
}
