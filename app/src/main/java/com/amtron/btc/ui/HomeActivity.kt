package com.amtron.btc.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.drawerlayout.widget.DrawerLayout
import com.amtron.btc.R
import com.amtron.btc.databinding.ActivityHomeBinding
import com.amtron.btc.helper.NotificationsHelper
import com.amtron.btc.model.User
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var userString: String
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = this.getSharedPreferences("file", MODE_PRIVATE)
        editor = sharedPreferences.edit()
        userString = sharedPreferences.getString("user", "").toString()

        val passwordChange = intent.getStringExtra("password update")
        if (passwordChange.equals("success")) {
            NotificationsHelper().getSuccessAlert(this, "Password successfully updated")
        }

        drawerLayout = binding.drawerLayout
        navView = binding.navView

        val headerView: View = navView.getHeaderView(0)
        val closeNavView: ImageView = headerView.findViewById(R.id.closeNavView)
        val user: User = Gson().fromJson(userString, User::class.java)

        binding.toggle.setOnClickListener {
            drawerLayout.openDrawer(navView)
        }

        closeNavView.setOnClickListener { drawerLayout.closeDrawer(navView) }

        binding.enterDetailsCard.setOnClickListener {
            val intent = Intent(this, EnterDetailsActivity::class.java)
            startActivity(intent)
        }

        binding.logoutButton.setOnClickListener {
            Toast.makeText(this, "Successfully logged out", Toast.LENGTH_SHORT).show()
            user.login = false
            editor.putString("user", Gson().toJson(user))
            editor.apply()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    startActivity(
                        Intent(this, HomeActivity::class.java)
                    )
                    true
                }
                R.id.change_password -> {
                    startActivity(
                        Intent(this, ChangePasswordActivity::class.java)
                    )
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(navView)) {
            drawerLayout.closeDrawer(navView)
        } else {
            val exit = BottomSheetDialog(this)
            exit.setContentView(R.layout.bottom_sheet_exit)
            val exitText = exit.findViewById<TextView>(R.id.exitText)
            val exitHeaderText = exit.findViewById<TextView>(R.id.exitHeaderText)
            val ok = exit.findViewById<Button>(R.id.go_back_btn)
            val continueBooking = exit.findViewById<Button>(R.id.con_book)
            exitText?.text = "Are you sure you want to exit the app?"
            exitHeaderText?.text = "EXIT?"
            ok?.text = "EXIT"
            continueBooking?.text = "CANCEL"
            exit.show()

            ok?.setOnClickListener {
                finishAffinity()
            }
            continueBooking?.setOnClickListener { exit.dismiss() }
        }
    }
}