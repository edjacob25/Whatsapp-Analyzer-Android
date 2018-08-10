package com.example.jacob.myapplication.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import com.example.jacob.myapplication.Constants
import com.example.jacob.myapplication.R

import java.util.Locale

import kotlinx.android.synthetic.main.activity_person.*

class PersonActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person)

        Log.i("Persona", Constants.person.toString())
        person_view_aggressiveness.text = getFormattedString(Constants.person.aggressiveness)
        person_view_happiness.text = getFormattedString(Constants.person.happiness)
        person_view_fear.text = getFormattedString(Constants.person.fear)
        person_view_love.text = getFormattedString(Constants.person.love)
        person_view_sadness.text = getFormattedString(Constants.person.sadness)
    }

    private fun getFormattedString(param: Float): String {
        return String.format(Locale.getDefault(), "%.0f%%", param * 100)
    }
}
