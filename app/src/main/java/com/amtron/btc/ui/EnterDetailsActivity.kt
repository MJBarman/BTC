package com.amtron.btc.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.amtron.btc.R
import com.amtron.btc.database.AppDatabase
import com.amtron.btc.databinding.ActivityEnterDetailsBinding
import com.amtron.btc.helper.DateHelper
import com.amtron.btc.model.MasterData

class EnterDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEnterDetailsBinding
    private lateinit var appDatabase: AppDatabase
    private lateinit var masterData: MasterData
    private var stateName = ""
    private var country = ""

    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEnterDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        appDatabase = AppDatabase.getDatabase(this)
        val stateList = resources.getStringArray(R.array.state_list)
        val foreignList = resources.getStringArray(R.array.foreign_list)

        val stateAdapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_dropdown_item, stateList
        )
        val foreignAdapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_dropdown_item, foreignList
        )

        binding.spinnerStateDropdown.setAdapter(stateAdapter)
        binding.spinnerCountryDropdown.setAdapter(foreignAdapter)

        binding.spinnerStateDropdown.setOnItemClickListener { adapterView, view, position, id ->
            stateName = adapterView.getItemAtPosition(position).toString()
            if (stateName == "Assam") {
                binding.residencyLl.visibility = View.VISIBLE
            } else {
                binding.residencyLl.visibility = View.GONE
            }
        }
        binding.spinnerCountryDropdown.setOnItemClickListener { adapterView, view, position, id ->
            country= adapterView.getItemAtPosition(position).toString()
        }

        binding.rbIndian.setOnCheckedChangeListener { buttonView, isChecked ->
            binding.spinnerStateDropdown.text = null
            if (binding.rbIndian.isChecked) {
                if (binding.spinnerCountry.isVisible) {
                    binding.spinnerCountry.visibility = View.GONE
                }
                binding.spinnerState.visibility = View.VISIBLE
            }
        }

        binding.rbForeign.setOnCheckedChangeListener { buttonView, isChecked ->
            if (binding.rbForeign.isChecked) {
                if (binding.residencyLl.isVisible || binding.spinnerState.isVisible) {
                    binding.residencyLl.visibility = View.GONE
                    binding.spinnerState.visibility = View.GONE
                }
                binding.spinnerCountry.visibility = View.VISIBLE
            }
        }

        binding.addData.setOnClickListener {
            addData()
        }
    }

    private fun addData() {
        val date = DateHelper().getTodayOrTomorrow("today", "dd-MM-yyyy")
        var name = ""
        var age = ""
        var residency = ""
        var gender = ""
        var nationality = ""

        name = binding.name.text.toString()
        age = binding.age.text.toString()

        if (binding.rbMale.isChecked && !binding.rbFemale.isChecked) {
            gender = "Male"
        } else if (binding.rbFemale.isChecked && !binding.rbMale.isChecked) {
            gender = "Female"
        }

        if (binding.rbIndian.isChecked && !binding.rbForeign.isChecked) {
            nationality = "Indian"
        } else if (binding.rbForeign.isChecked && !binding.rbIndian.isChecked) {
            nationality = "foreign"
        }

        if (binding.rbBtr.isChecked && !binding.rbNonbtr.isChecked) {
            residency = "BTR"
        } else if (binding.rbNonbtr.isChecked && !binding.rbBtr.isChecked) {
            residency = "Non BTR"
        }

        masterData = MasterData(null, name, Integer.parseInt(age), gender, country,
            stateName, nationality, residency, date, false)

        Log.d("TAG",  masterData.toString())

    }
}