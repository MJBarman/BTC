package com.amtron.btc.ui

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amtron.btc.R
import com.amtron.btc.adapter.MasterDataAdapter
import com.amtron.btc.database.AppDatabase
import com.amtron.btc.databinding.ActivityLoginBinding
import com.amtron.btc.databinding.ActivityMasterDataTableBinding
import com.amtron.btc.helper.NotificationsHelper
import com.amtron.btc.helper.Util
import com.amtron.btc.model.MasterData
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@DelicateCoroutinesApi
class MasterDataTableActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMasterDataTableBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var recyclerView: RecyclerView
    private lateinit var masterDataAdapter: MasterDataAdapter
    private lateinit var appDatabase: AppDatabase
    private lateinit var masterDataList: ArrayList<MasterData>
    private var checkInternet: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMasterDataTableBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = this.getSharedPreferences("file", MODE_PRIVATE)
        editor = sharedPreferences.edit()

        masterDataList = arrayListOf()
        appDatabase = AppDatabase.getDatabase(this)
        readData()

        recyclerView = binding.recordsRecyclerView

        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager

        masterDataAdapter = MasterDataAdapter(masterDataList)
        recyclerView.adapter = masterDataAdapter

        binding.sync.setOnClickListener {
            checkInternet = Util().isOnline(this)
            if (!checkInternet) {
                NotificationsHelper().getErrorAlert(this, "No Internet Connection Available")
            } else {
                //Proceed with sync
            }
        }
    }

    private fun readData() {
        GlobalScope.launch(Dispatchers.IO) {
            masterDataList.addAll(appDatabase.MasterDataDao().getAll())
            Log.d("msg", masterDataList.toString())

            if (masterDataList.isEmpty()) {
            NotificationsHelper().getWarningAlert(applicationContext, "No Records Found")
        }
        }
    }
}