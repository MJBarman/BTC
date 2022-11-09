package com.amtron.btc.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.amtron.btc.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class EnterDetailsActivity : AppCompatActivity() {

    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter_details)
        val button: Button = findViewById(R.id.button)
        val name: TextInputEditText = findViewById(R.id.name)
        val rg_gender: RadioGroup = findViewById(R.id.rg_gender)
        val rbMale: RadioButton = findViewById(R.id.rb_male)
        val rbFemale: RadioButton = findViewById(R.id.rb_female)
        val age: TextInputEditText = findViewById(R.id.age)
        val rgNationality: RadioGroup = findViewById(R.id.rg_nationality)
        val rbIndian: RadioButton = findViewById(R.id.rb_indian)
        val rbForeign: RadioButton = findViewById(R.id.rb_foreign)
        val residenSpinner: AutoCompleteTextView = findViewById(R.id.spinner_state_dropdown)
        val state: TextInputLayout = findViewById(R.id.spinner_state)

        button.setOnClickListener {
            //on click
        }

        val listResidency = ArrayList<String>()
        listResidency.add("Hatigaon")
        listResidency.add("Beltola")
        listResidency.add("Six Mile")
        listResidency.add("Ganeshguri")

        val numberAdapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_dropdown_item, listResidency
        )

        residenSpinner.setAdapter(numberAdapter)
        residenSpinner.setOnItemClickListener { adapterView, view, i, l ->
            Toast.makeText(
                this, adapterView.getItemAtPosition(i).toString(), Toast.LENGTH_LONG
            ).show()
        }

        fun onRadioButtonClicked(view: View) {
            if (view is RadioButton) {
                val checked = view.isChecked
                when (view.getId()) {
                    R.id.rb_indian -> if (checked) {
                        Toast.makeText(
                            this, "Indian", Toast.LENGTH_LONG
                        ).show()
                    }
                    R.id.rb_foreign -> if (checked) {
                        Toast.makeText(
                            this, "Indian", Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }

    }
}