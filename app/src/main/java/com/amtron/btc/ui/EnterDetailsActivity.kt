package com.amtron.btc.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Spinner
import android.widget.Toast
import com.amtron.btc.R

class EnterDetailsActivity : AppCompatActivity() {

    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter_details)


        val residenSpinner: AutoCompleteTextView = findViewById(R.id.spinner_residency_indian)
        val listResidency = ArrayList<String>()
        listResidency.add("Hatigaon")
        listResidency.add("Beltola")
        listResidency.add("Six Nile")
        listResidency.add("Ganeshguri")

        val numberAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listResidency)

        residenSpinner.setAdapter(numberAdapter)
        residenSpinner.setOnItemClickListener { adapterView, view, i, l ->
            Toast.makeText(this, adapterView.getItemAtPosition(i).toString(), Toast.LENGTH_LONG).show()
        }

    }
}