package com.amtron.btc.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.amtron.btc.databinding.ActivityLoginBinding
import com.amtron.btc.helper.ErrorHelper
import com.amtron.btc.helper.Util

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var errorHelper: ErrorHelper
    private lateinit var userString: String
    private var checkInternet: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = this.getSharedPreferences("file", MODE_PRIVATE)
        userString = sharedPreferences.getString("user", "").toString()

        checkInternet = Util().isOnline(this)
        if (!checkInternet) {
            binding.buttonProceed.visibility = View.GONE
            binding.noInternetWarning.visibility = View.VISIBLE
            binding.restartBtn.setOnClickListener {
                finish()
                startActivity(intent)
                overridePendingTransition(0, 0)
            }
        } else {
            if (userString.isNotEmpty()) {
                val intent = Intent(applicationContext, HomeActivity::class.java)
                startActivity(intent)
            }

            binding.buttonProceed.setOnClickListener {
                if (binding.phoneNumber.text.toString().isEmpty()
                    || binding.password.text.toString().isEmpty()
                ) {
                    ErrorHelper().getErrorAlert(this, "Please enter all the fields.")
                } else {
                    if (binding.phoneNumber.text.toString().length == 10) {
                        val intent = Intent(applicationContext, HomeActivity::class.java)
                        startActivity(intent)
                    } else {
                        ErrorHelper().getErrorAlert(this, "Please enter 10 digit phone number.")
                    }
                }
            }
        }
    }


}