package com.amtron.btc.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.amtron.btc.databinding.ActivityLoginBinding
import com.amtron.btc.helper.NotificationsHelper
import com.amtron.btc.helper.ResponseHelper
import com.amtron.btc.helper.Util
import com.amtron.btc.model.LoginCredentials
import com.amtron.btc.network.Client
import com.amtron.btc.network.RetrofitHelper
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@DelicateCoroutinesApi
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var loginCredentialsString: String
    private var checkInternet: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = this.getSharedPreferences("file", MODE_PRIVATE)
        editor = sharedPreferences.edit()
        loginCredentialsString = sharedPreferences.getString("loginCredentials", "").toString()
        if (loginCredentialsString.isNotEmpty()) {
            val intent = Intent(this@LoginActivity, HomeActivity::class.java)
            startActivity(intent)
        }

        binding.buttonProceed.setOnClickListener {
            if (binding.phoneNumber.text.toString().isEmpty()
                || binding.password.text.toString().isEmpty()
            ) {
                NotificationsHelper().getWarningAlert(this, "Please enter all the fields.")
            } else {
                checkInternet = Util().isOnline(this@LoginActivity)
                if (!checkInternet) {
                    NotificationsHelper().getErrorAlert(
                        this@LoginActivity,
                        "No Internet Connection Available"
                    )
                } else {
                    if (binding.phoneNumber.text.toString().length == 10) {
                        binding.progressbar.show()
                        val api = RetrofitHelper.getInstance().create(Client::class.java)
                        GlobalScope.launch {
                            val call: Call<JsonObject> = api.login(
                                binding.phoneNumber.text.toString(),
                                binding.password.text.toString()
                            )
                            call.enqueue(object : Callback<JsonObject> {
                                @SuppressLint("CommitPrefEdits", "NotifyDataSetChanged")
                                override fun onResponse(
                                    call: Call<JsonObject>,
                                    response: Response<JsonObject>
                                ) {
                                    if (response.isSuccessful) {
                                        binding.progressbar.hide()
                                        val helper = ResponseHelper()
                                        helper.ResponseHelper(response.body())
                                        if (helper.isStatusSuccessful()) {
                                            val loginCredentials: LoginCredentials =
                                                Gson().fromJson(
                                                    helper.getDataAsString(),
                                                    object : TypeToken<LoginCredentials>() {}.type
                                                )
                                            editor.putString(
                                                "loginCredentials",
                                                Gson().toJson(loginCredentials)
                                            )
                                            editor.apply()
                                            val intent =
                                                Intent(applicationContext, HomeActivity::class.java)
                                            startActivity(intent)
                                        } else {
                                            NotificationsHelper().getErrorAlert(
                                                this@LoginActivity,
                                                helper.getErrorMsg()
                                            )
                                        }
                                    } else {
                                        binding.progressbar.hide()
                                        NotificationsHelper().getErrorAlert(
                                            this@LoginActivity,
                                            "Response Error Code" + response.message()
                                        )
                                    }
                                }

                                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                                    binding.progressbar.hide()
                                    NotificationsHelper().getErrorAlert(
                                        this@LoginActivity,
                                        "Server Error"
                                    )
                                }
                            })
                        }
                    } else {
                        NotificationsHelper().getErrorAlert(
                            this,
                            "Please enter 10 digit phone number."
                        )
                    }
                }
            }
        }

        binding.register.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun isOnline() {
        TODO("Not yet implemented")
    }
}