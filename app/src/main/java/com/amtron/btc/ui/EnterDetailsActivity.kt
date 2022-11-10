package com.amtron.btc.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.amtron.btc.R
import com.amtron.btc.databinding.ActivityEnterDetailsBinding

class EnterDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEnterDetailsBinding
    private val STATE_NAME = "Assam"
    private var found = false

    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEnterDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

    }
}