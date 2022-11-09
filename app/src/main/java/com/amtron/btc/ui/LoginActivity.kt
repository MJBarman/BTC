package com.amtron.btc.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.amtron.btc.databinding.ActivityLoginBinding
import com.amtron.btc.helper.NotificationsHelper
import com.amtron.btc.model.User
import com.google.gson.Gson

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var user: User
    private lateinit var u: User
    private lateinit var userString: String
    private var checkInternet: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = this.getSharedPreferences("file", MODE_PRIVATE)
        editor = sharedPreferences.edit()
        userString = sharedPreferences.getString("user", "").toString()
        if (userString.isNotEmpty()) {
            u = Gson().fromJson(userString, User::class.java)
        }
        user = User()

        if (userString.isNotEmpty()) {
            if (u.login) {
                val intent = Intent(applicationContext, HomeActivity::class.java)
                startActivity(intent)
            }
        }

        binding.buttonProceed.setOnClickListener {
            if (binding.phoneNumber.text.toString().isEmpty()
                || binding.password.text.toString().isEmpty()
            ) {
                NotificationsHelper().getWarningAlert(this, "Please enter all the fields.")
            } else {
                if (binding.phoneNumber.text.toString().length == 10) {
                    if (userString.isNotEmpty()) {
                        if (u.mobile != binding.phoneNumber.text.toString()) {
                            editor.clear()
                            user.mobile = binding.phoneNumber.text.toString()
                            user.password = binding.password.text.toString()
                            user.login = true
                            editor.putString("user", Gson().toJson(user))
                            editor.apply()
                            val intent = Intent(applicationContext, HomeActivity::class.java)
                            startActivity(intent)
                        } else {
                            if (u.mobile == binding.phoneNumber.text.toString() && u.password != binding.password.text.toString()) {
                                NotificationsHelper().getErrorAlert(
                                    this,
                                    "Incorrect phone or password."
                                )
                            } else {
                                val intent = Intent(applicationContext, HomeActivity::class.java)
                                startActivity(intent)
                            }
                        }
                    } else {
                        user.mobile = binding.phoneNumber.text.toString()
                        user.password = binding.password.text.toString()
                        user.login = true
                        editor.putString("user", Gson().toJson(user))
                        editor.apply()
                        val intent = Intent(applicationContext, HomeActivity::class.java)
                        startActivity(intent)
                    }
                } else {
                    NotificationsHelper().getErrorAlert(this, "Please enter 10 digit phone number.")
                }
            }
        }

        binding.register.setOnClickListener {
            val intent = Intent(applicationContext, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}