package com.amtron.btc.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.amtron.btc.databinding.ActivityChangePasswordBinding
import com.amtron.btc.helper.NotificationsHelper
import com.amtron.btc.model.User
import com.google.gson.Gson

class ChangePasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangePasswordBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var userString: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = this.getSharedPreferences("file", MODE_PRIVATE)
        editor = sharedPreferences.edit()
        userString = sharedPreferences.getString("user", "").toString()

        binding.buttonProceed.setOnClickListener {
            if (binding.password.text.toString().isEmpty() || binding.confirmPassword.text.toString().isEmpty()) {
                NotificationsHelper().getErrorAlert(this, "Please enter all fields.")
            } else if (binding.password.text.toString() != binding.confirmPassword.text.toString()) {
                NotificationsHelper().getErrorAlert(this, "Passwords do not match.")
            } else {
                val u: User = Gson().fromJson(userString, User::class.java)
                u.password = binding.password.text.toString()
                val intent = Intent(this, HomeActivity::class.java)
                intent.putExtra("password update", "success")
                startActivity(intent)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }
}