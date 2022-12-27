package com.amtron.btc.ui

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.amtron.btc.database.AppDatabase
import com.amtron.btc.databinding.ActivityEnterDetailsBinding
import com.amtron.btc.helper.DateHelper
import com.amtron.btc.helper.NotificationsHelper
import com.amtron.btc.model.*
import com.google.gson.Gson
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
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var gender: Gender
    private lateinit var state: State
    private lateinit var country: Country
    private lateinit var domicile: Domicile
    private lateinit var loginCredentials: LoginCredentials
    private lateinit var genderList: ArrayList<Gender>
    private lateinit var countryList: ArrayList<Country>
    private lateinit var stateList: ArrayList<State>
    private lateinit var domicileList: ArrayList<Domicile>
    private lateinit var grb: RadioButton
    private lateinit var drb: RadioButton
    private var countryStringList = arrayListOf<String>()
    private var stateStringList = arrayListOf<String>()

    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEnterDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        appDatabase = AppDatabase.getDatabase(this)

        val fadeAnimation = android.view.animation.AnimationUtils.loadAnimation(
            this,
            com.google.android.material.R.anim.abc_grow_fade_in_from_bottom
        )

        sharedPreferences = this.getSharedPreferences("file", MODE_PRIVATE)
        editor = sharedPreferences.edit()
        val loginCredentialsString = sharedPreferences.getString("loginCredentials", "DEFAULT")
        loginCredentials = Gson().fromJson(loginCredentialsString, LoginCredentials::class.java)

        genderList = loginCredentials.genders as ArrayList<Gender>
        countryList = loginCredentials.countries as ArrayList<Country>
        stateList = loginCredentials.states as ArrayList<State>
        domicileList = loginCredentials.domicile as ArrayList<Domicile>

        for (c in countryList) {
            countryStringList.add(c.country_name!!)
        }
        for (s in stateList) {
            stateStringList.add(s.state_name!!)
        }

        val stateAdapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_dropdown_item, stateStringList
        )
        val foreignAdapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_dropdown_item, countryStringList
        )

        val grg = binding.rgGender
        grg.gravity = Gravity.CENTER
        grg.orientation = RadioGroup.HORIZONTAL
        for (i in genderList.indices) {
            grb = RadioButton(this)
            grb.setPadding(5, 10, 5, 10)
            grb.text = genderList[i].gender_name
            grb.id = genderList[i].id!!
            grg.addView(grb)
        }
        grg.setOnCheckedChangeListener { _, checkedId ->
            for(i in genderList) {
                if (i.id == checkedId) {
                    gender = i
                    break
                }
            }
        }

        val drg = binding.rgResidency
        drg.gravity = Gravity.CENTER
        drg.orientation = RadioGroup.HORIZONTAL
        for (i in domicileList.indices) {
            drb = RadioButton(this)
            drb.setPadding(5, 10, 5, 10)
            drb.text = domicileList[i].dom_name
            drb.id = domicileList[i].id!!
            drg.addView(drb)
        }
        drg.setOnCheckedChangeListener { _, checkedId ->
            for(i in domicileList) {
                if (i.id == checkedId) {
                    domicile = i
                    break
                }
            }
        }

        binding.spinnerStateDropdown.setAdapter(stateAdapter)
        binding.spinnerCountryDropdown.setAdapter(foreignAdapter)

        binding.spinnerStateDropdown.setOnItemClickListener { _, _, position, _ ->
            state = stateList[position]
            if (state.state_name == "ASSAM") {
                binding.residencyLl.visibility = View.VISIBLE
                binding.residencyLl.startAnimation(fadeAnimation)
            } else {
                binding.residencyLl.visibility = View.GONE
            }
        }
        binding.spinnerCountryDropdown.setOnItemClickListener { _, _, position, _ ->
            country = countryList[position]
        }

        binding.rbIndian.setOnCheckedChangeListener { _, _ ->
            binding.spinnerStateDropdown.text = null
            if (binding.rbIndian.isChecked) {
                if (binding.spinnerCountry.isVisible) {
                    binding.spinnerCountry.visibility = View.GONE
                }
                binding.spinnerState.visibility = View.VISIBLE
                binding.spinnerState.startAnimation(fadeAnimation)
            }
        }

        binding.rbForeign.setOnCheckedChangeListener { _, _ ->
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
        var nationality = ""

        if (binding.rbIndian.isChecked && !binding.rbForeign.isChecked) {
            nationality = "I"
        } else if (binding.rbForeign.isChecked && !binding.rbIndian.isChecked) {
            nationality = "F"
        }

        if (name.isEmpty() || age.isEmpty() || nationality.isEmpty() || date.isEmpty()                        //check gender
        ) {
            NotificationsHelper().getErrorAlert(this, "Please enter all the fields")
        } else {
            if (nationality == "I") {
                if (state.id == 0) {
                    NotificationsHelper().getErrorAlert(this, "Please enter state")
                } else {
                    if (state.state_name == "ASSAM") {
                        if (domicile.id == 0) {
                            NotificationsHelper().getErrorAlert(this, "Please select residency")
                        } else {
                            addData(
                                name,
                                Integer.parseInt(age),
                                gender,
                                null,
                                state,
                                nationality,
                                domicile,
                                date
                            )
                            reset()
                        }
                    } else {
                        addData(
                            name,
                            Integer.parseInt(age),
                            gender,
                            null,
                            state,
                            nationality,
                            null,
                            date
                        )
                        reset()
                    }
                }
            } else if (nationality == "F") {
                if (country.id == 0) {
                    NotificationsHelper().getErrorAlert(this, "Please enter country")
                } else {
                    addData(
                        name,
                        Integer.parseInt(age),
                        gender,
                        country,
                        null,
                        nationality,
                        null,
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
        name: String, age: Int, gender: Gender, country: Country?, stateName: State?,
        nationality: String, residency: Domicile?, date: String
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