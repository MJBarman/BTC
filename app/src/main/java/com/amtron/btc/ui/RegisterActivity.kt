package com.amtron.btc.ui

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.amtron.btc.R
import com.amtron.btc.databinding.ActivityRegisterBinding
import com.amtron.btc.helper.NotificationsHelper
import com.amtron.btc.model.User
import com.google.gson.Gson

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        sharedPreferences = this.getSharedPreferences("file", MODE_PRIVATE)
        editor = sharedPreferences.edit()

        binding.buttonProceed.setOnClickListener {
            if (binding.phoneNumber.text.toString().isEmpty()
                || binding.password.text.toString().isEmpty()
                || binding.confirmPassword.text.toString().isEmpty()
            ) {
                NotificationsHelper().getWarningAlert(this, "Please enter all the fields.")
            } else {
                if (binding.phoneNumber.text.toString().length == 10) {
                    if (binding.password.text.toString() == binding.confirmPassword.text.toString()) {
                        user.mobile = binding.phoneNumber.text.toString()
                        user.password = binding.confirmPassword.text.toString()
                        editor.putString("userString", Gson().toJson(user))
                        val intent = Intent(applicationContext, HomeActivity::class.java)
                        intent.putExtra("registration", "success")
                        startActivity(intent)
                    } else {
                        NotificationsHelper().getErrorAlert(this, "Passwords do not match.")
                    }
                } else {
                    NotificationsHelper().getErrorAlert(this, "Please enter 10 digit phone number.")
                }
            }
        }
    }
}