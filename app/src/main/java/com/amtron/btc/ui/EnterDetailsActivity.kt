package com.amtron.btc.ui

import android.annotation.SuppressLint
import android.os.Bundle
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
    private val STATE_NAME = "Assam"

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
            var item = adapterView.getItemAtPosition(position).toString()
            if (item == STATE_NAME) {
                binding.residencyLl.visibility = View.VISIBLE
            } else {
                binding.residencyLl.visibility = View.GONE
            }
        }

        binding.spinnerCountryDropdown.setOnItemClickListener { adapterView, view, i, l ->
            //
        }


        binding.rbIndian.setOnCheckedChangeListener { buttonView, isChecked ->
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
            }
        }

        binding.addData.setOnClickListener {
            addData()
        }
    }


    private fun addData() {
        val date = DateHelper().getTodayOrTomorrow("today", "dd-MM-yyyy")
        var name = binding.name.text.toString()
        var age = binding.age.text.toString()
        lateinit var male: String
        lateinit var female: String

        if (binding.rbMale.isChecked && !binding.rbFemale.isChecked) {
            male = "Male"
            Toast.makeText(this, male, Toast.LENGTH_SHORT).show()
        } else if (binding.rbFemale.isChecked && !binding.rbMale.isChecked) {
            female = "Female"
            Toast.makeText(this, female, Toast.LENGTH_SHORT).show()
        }


//        masterData = MasterData(null, "")

    }
}