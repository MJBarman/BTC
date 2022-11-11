package com.amtron.btc.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.amtron.btc.R
import com.amtron.btc.database.AppDatabase
import com.amtron.btc.databinding.ActivityEnterDetailsBinding
import com.amtron.btc.helper.DateHelper
import com.amtron.btc.model.MasterData
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@DelicateCoroutinesApi
class EnterDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEnterDetailsBinding
    private lateinit var masterData: MasterData
    private lateinit var appDatabase: AppDatabase
    private val STATE_NAME = "Assam"
    private var found = false

    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEnterDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        appDatabase = AppDatabase.getDatabase(this)

        val stateList = resources.getStringArray(R.array.state_list)
        val numberAdapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_dropdown_item, stateList
        )

        binding.spinnerStateDropdown.setAdapter(numberAdapter)
        binding.spinnerStateDropdown.setOnItemClickListener { adapterView, view, i, l ->
            for (n in stateList) {
                if (n == STATE_NAME) {
                    found = true
                    Toast.makeText(
                        this, "Found", Toast.LENGTH_LONG
                    ).show()
                    break
                }
            }
        }

        binding.rbIndian.setOnCheckedChangeListener { buttonView, isChecked ->
            fun showHide(view: View) {
                view.visibility = if (view.visibility == View.VISIBLE) {
                    View.GONE
                } else {
                    View.VISIBLE
                }
            }
            showHide(binding.spinnerState)
        }

        binding.rbForeign.setOnCheckedChangeListener { buttonView, isChecked ->
            fun showHide(view: View) {
                view.visibility = if (view.visibility == View.VISIBLE) {
                    View.GONE
                } else {
                    View.VISIBLE
                }
            }
            showHide(binding.spinnerCountry)
        }

        binding.addData.setOnClickListener {
            writeData()
        }
    }

    private fun writeData() {
        val today = DateHelper().getTodayOrTomorrow("today", "dd-MM-yyyy")
        //Dummy Data
        val name = "Dzango"
        val age = 25
        val gender = "Male"
        val country = "India"
        val state = "Assam"
        val residency = "BTR"
        val isSynced = false
        masterData = MasterData(null, name, age, gender, country, state, residency, today, isSynced)

        GlobalScope.launch(Dispatchers.IO) {
            appDatabase.MasterDataDao().insert(masterData)
        }
        Toast.makeText(applicationContext, "Successful", Toast.LENGTH_SHORT).show()
    }
}