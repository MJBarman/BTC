package com.amtron.btc.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import com.amtron.btc.R
import com.amtron.btc.database.AppDatabase
import com.amtron.btc.databinding.VisitorDetailsLayoutNewBinding
import com.amtron.btc.model.MasterData
import com.amtron.btc.ui.fragments.SyncedFragment
import com.amtron.btc.ui.fragments.UnSyncedFragment
import com.google.gson.Gson
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@DelicateCoroutinesApi
class VisitorsDetailsActivity : AppCompatActivity() {
    private lateinit var binding: VisitorDetailsLayoutNewBinding
    private lateinit var context: Context
    private lateinit var appDatabase: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = VisitorDetailsLayoutNewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this

        appDatabase = AppDatabase.getDatabase(this)

        replaceFragment(UnSyncedFragment())

        binding.bottomBar.onTabSelected = {
            when (it.id) {
                R.id.tab_unsync -> replaceFragment(UnSyncedFragment())
                R.id.tab_sync -> replaceFragment(SyncedFragment())
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }
}

