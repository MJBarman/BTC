package com.amtron.btc.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.amtron.btc.R
import com.amtron.btc.database.AppDatabase
import com.amtron.btc.databinding.ActivityEnterDetailsBinding
import com.amtron.btc.helper.DateHelper
import com.amtron.btc.helper.NotificationsHelper
import com.amtron.btc.model.MasterData
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

@DelicateCoroutinesApi
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
        val fadeAnimation = android.view.animation.AnimationUtils.loadAnimation(
            this,
            com.google.android.material.R.anim.abc_grow_fade_in_from_bottom
        )


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
                binding.residencyLl.startAnimation(fadeAnimation)
            } else {
                binding.residencyLl.visibility = View.GONE
            }
        }
        binding.spinnerCountryDropdown.setOnItemClickListener { adapterView, view, position, id ->
            country = adapterView.getItemAtPosition(position).toString()
        }

        binding.rbIndian.setOnCheckedChangeListener { buttonView, isChecked ->
            binding.spinnerStateDropdown.text = null
            if (binding.rbIndian.isChecked) {
                if (binding.spinnerCountry.isVisible) {
                    binding.spinnerCountry.visibility = View.GONE
                }
                binding.spinnerState.visibility = View.VISIBLE
                binding.spinnerState.startAnimation(fadeAnimation)
            }
        }

        binding.rbForeign.setOnCheckedChangeListener { buttonView, isChecked ->
            if (binding.rbForeign.isChecked) {
                if (binding.residencyLl.isVisible || binding.spinnerState.isVisible) {
                    binding.residencyLl.visibility = View.GONE
                    binding.spinnerState.visibility = View.GONE
                }
                binding.spinnerCountry.visibility = View.VISIBLE
                binding.spinnerCountry.startAnimation(fadeAnimation)
            }
        }

        binding.addData.setOnClickListener {
            getData()
        }
    }

    private fun getData() {
        val date = DateHelper().getTodayOrTomorrow("today", "dd-MM-yyyy")
        val name = binding.name.text.toString().uppercase(Locale.getDefault())
        val age = binding.age.text.toString().uppercase(Locale.getDefault())
        var residency = ""
        var gender = ""
        var nationality = ""

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

        if (name.isEmpty() || age.isEmpty() || gender.isEmpty() || nationality.isEmpty()
            || date.isEmpty()
        ) {
            NotificationsHelper().getErrorAlert(this, "Please enter all the fields")
        } else {
            if (nationality == "Indian") {
                if (stateName.isEmpty()) {
                    NotificationsHelper().getErrorAlert(this, "Please enter all the fields")
                } else if (stateName == "Assam") {
                    if (residency.isEmpty()) {
                        NotificationsHelper().getErrorAlert(this, "Please enter all the fields")
                    } else {
                        addData(
                            name,
                            Integer.parseInt(age),
                            gender,
                            "India",
                            stateName,
                            nationality,
                            residency,
                            date
                        )
                        reset()
                    }
                } else {
                    addData(
                        name,
                        Integer.parseInt(age),
                        gender,
                        "India",
                        stateName,
                        nationality,
                        "",
                        date
                    )
                    reset()
                }
            } else if (nationality == "foreign") {
                if (country.isEmpty()) {
                    NotificationsHelper().getErrorAlert(this, "Please enter all the fields")
                } else {
                    addData(
                        name,
                        Integer.parseInt(age),
                        gender,
                        country,
                        "",
                        nationality,
                        "",
                        date
                    )
                    reset()
                }
            }
        }
    }

    private fun reset() {
        binding.name.text = null
        binding.age.text = null
        binding.rgGender.clearCheck()
        binding.rgNationality.clearCheck()
        binding.rgResidency.clearCheck()
        binding.spinnerCountryDropdown.text = null
        binding.spinnerStateDropdown.text = null
        binding.spinnerCountry.visibility = View.GONE
        binding.spinnerState.visibility = View.GONE
        binding.residencyLl.visibility = View.GONE
        NotificationsHelper().getSuccessAlert(this, "Details saved successfully")

    }

    private fun addData(
        name: String, age: Int, gender: String, country: String, stateName: String,
        nationality: String, residency: String, date: String
    ) {
        masterData = MasterData(
            null, name, age, gender, country,
            stateName, nationality, residency, date, false
        )

        saveData(masterData)
    }

    private fun saveData(masterData: MasterData) {
        GlobalScope.launch(Dispatchers.IO) {
            appDatabase.MasterDataDao().insert(masterData)
        }
    }


}