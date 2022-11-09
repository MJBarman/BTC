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
        val country: TextInputLayout = findViewById(R.id.spinner_country)

        button.setOnClickListener {
            //on click
        }

        val listResidency = ArrayList<String>()
        listResidency.add("Assam")
        listResidency.add("Mizoram")
        listResidency.add("Arunachal")
        listResidency.add("Tripura")

        val numberAdapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_dropdown_item, listResidency
        )

        residenSpinner.setAdapter(numberAdapter)
        residenSpinner.setOnItemClickListener { adapterView, view, i, l ->
            Toast.makeText(
                this, adapterView.getItemAtPosition(i).toString(), Toast.LENGTH_LONG
            ).show()
        }

        rbIndian.setOnCheckedChangeListener { buttonView, isChecked ->
            fun showHide(view: View) {
                view.visibility = if (view.visibility == View.VISIBLE) {
                    View.GONE
                } else {
                    View.VISIBLE
                }
            }
            showHide(state)
        }

        rbForeign.setOnCheckedChangeListener { buttonView, isChecked ->
            fun showHide(view: View) {
                view.visibility = if (view.visibility == View.VISIBLE) {
                    View.GONE
                } else {
                    View.VISIBLE
                }
            }
            showHide(country)
        }

    }
}