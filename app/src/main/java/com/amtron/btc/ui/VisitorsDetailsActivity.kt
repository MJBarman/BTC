package com.amtron.btc.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.amtron.btc.R
import com.amtron.btc.databinding.VisitorDetailsLayoutNewBinding
import com.amtron.btc.ui.fragments.SyncedFragment
import com.amtron.btc.ui.fragments.UnSyncedFragment
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class VisitorsDetailsActivity : AppCompatActivity() {
    private lateinit var binding: VisitorDetailsLayoutNewBinding
    private lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = VisitorDetailsLayoutNewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this

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

