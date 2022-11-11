package com.amtron.btc.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.amtron.btc.R
import com.amtron.btc.database.AppDatabase
import com.amtron.btc.databinding.ActivityEnterDetailsBinding
import com.amtron.btc.helper.DateHelper
import com.amtron.btc.model.MasterData

class EnterDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEnterDetailsBinding
    private lateinit var appDatabase: AppDatabase
    private val STATE_NAME = "Assam"

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
            var item = adapterView.getItemAtPosition(i).toString()
            if (item == STATE_NAME) {
                showHide(binding.residencyLl)
            } else {
                binding.residencyLl.visibility = View.GONE
            }
        }


        binding.rbIndian.setOnCheckedChangeListener { buttonView, isChecked ->
            showHide(binding.spinnerState)
        }

        binding.rbForeign.setOnCheckedChangeListener { buttonView, isChecked ->
            showHide(binding.spinnerCountry)
            showHide(binding.residencyLl)
        }

        binding.addData.setOnClickListener {
            addData()
        }
    }

    private fun showHide(view: View) {
        view.visibility = if (view.visibility == View.VISIBLE) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun addData() {
        val date = DateHelper().getTodayOrTomorrow("today", "dd-MM-yyyy")

    }
}