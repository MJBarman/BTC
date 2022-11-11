package com.amtron.btc.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.amtron.btc.adapter.MasterDataAdapter
import com.amtron.btc.database.AppDatabase
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
    private lateinit var context: Context
    private var checkInternet: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMasterDataTableBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this

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

        binding.delete.setOnClickListener {
            deleteRecords()
        }
    }

    private fun readData() {
        GlobalScope.launch(Dispatchers.IO) {
            masterDataList.addAll(appDatabase.MasterDataDao().getAll())
            Log.d("msg", masterDataList.toString())

            if (masterDataList.isEmpty()) {
                runOnUiThread {
                    Log.d("msg", masterDataList.toString())
                    NotificationsHelper().getWarningAlert(
                        context,
                        "No Records Found"
                    )
                }
            }
        }
    }

    private fun deleteRecords() {
        SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
            .setTitleText("WARNING!")
            .setContentText("Only SYNCED files will be deleted.")
            .setConfirmText("YES DELETE!")
            .setConfirmClickListener {
                GlobalScope.launch(Dispatchers.IO) {
                    appDatabase.MasterDataDao().deleteBySynced(false)
                }
                SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Deleted!")
                    .setContentText("Files deleted!")
                    .setConfirmText("OK")
                    .setConfirmClickListener {
                        val intent = intent
                        startActivity(intent)
                    }
                    .show()
            }
            .showCancelButton(true)
            .show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }
}