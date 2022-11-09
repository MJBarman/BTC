package com.amtron.btc.ui

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amtron.btc.R
import com.amtron.btc.adapter.MasterDataAdapter
import com.amtron.btc.databinding.ActivityLoginBinding
import com.amtron.btc.databinding.ActivityMasterDataTableBinding
import com.amtron.btc.helper.NotificationsHelper
import com.amtron.btc.helper.Util
import com.amtron.btc.model.MasterData

class MasterDataTableActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMasterDataTableBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var recyclerView: RecyclerView
    private lateinit var masterDataAdapter: MasterDataAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMasterDataTableBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = this.getSharedPreferences("file", MODE_PRIVATE)
        editor = sharedPreferences.edit()

        recyclerView = binding.recordsRecyclerView

        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager


        val masterDataList = arrayListOf<MasterData>()
        //Dummy Data
        val masterData: MasterData = MasterData(1, "ravan", 255,
            "male", "Sri Lanka", "colombo", "Non-BTR",
        "2022-11-09")

        masterDataList.add(masterData)
        masterDataAdapter = MasterDataAdapter(masterDataList)
        recyclerView.adapter = masterDataAdapter

        binding.sync.setOnClickListener {
            val checkInternet: Boolean = Util().isOnline(this)
            if (!checkInternet) {
                NotificationsHelper().getErrorAlert(this, "No Internet Connection Available")
            } else {
                //Proceed with sync
            }
        }
    }
}