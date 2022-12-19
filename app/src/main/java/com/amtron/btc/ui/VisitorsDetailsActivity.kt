package com.amtron.btc.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.amtron.btc.R
import com.amtron.btc.adapter.RecordsAdapter
import com.amtron.btc.database.AppDatabase
import com.amtron.btc.databinding.VisitorDetailsLayoutNewBinding
import com.amtron.btc.helper.NotificationsHelper
import com.amtron.btc.helper.ResponseHelper
import com.amtron.btc.model.MasterData
import com.amtron.btc.network.Client
import com.amtron.btc.network.RetrofitHelper
import com.amtron.btc.ui.fragments.SyncedFragment
import com.amtron.btc.ui.fragments.UnSyncedFragment
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@DelicateCoroutinesApi
class VisitorsDetailsActivity : AppCompatActivity() {
    private lateinit var binding: VisitorDetailsLayoutNewBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var recyclerView: RecyclerView
    private lateinit var masterDataAdapter: RecordsAdapter
    private lateinit var appDatabase: AppDatabase
    private lateinit var masterDataList: ArrayList<MasterData>
    private lateinit var context: Context
    private var checkInternet: Boolean = false
    private var recordsList: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = VisitorDetailsLayoutNewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this

        replaceFragment(UnSyncedFragment())

        binding.bottomBar.onTabSelected = {
            when (it.id){
                R.id.tab_unsync-> replaceFragment(UnSyncedFragment())
                R.id.tab_sync -> replaceFragment(SyncedFragment())
            }
        }

        sharedPreferences = this.getSharedPreferences("file", MODE_PRIVATE)
        editor = sharedPreferences.edit()

        masterDataList = arrayListOf()
        appDatabase = AppDatabase.getDatabase(this)
        readData()

//        recyclerView = binding.recordsRecyclerView

        val linearLayoutManager = LinearLayoutManager(this)
//        recyclerView.layoutManager = linearLayoutManager
//
//        masterDataAdapter = RecordsAdapter(masterDataList)
//        recyclerView.adapter = masterDataAdapter


//        binding.sync.setOnClickListener {
//            checkInternet = Util().isOnline(this)
//            if (!checkInternet) {
//                NotificationsHelper().getErrorAlert(this, "No Internet Connection Available")
//            } else {
//                //get masterData from server
//                fetchSyncedRecords()
//            }
//        }

//        binding.delete.setOnClickListener {
//            deleteRecords()
//        }
    }

    private fun readData() {
        GlobalScope.launch(Dispatchers.IO) {
            masterDataList.addAll(appDatabase.MasterDataDao().getAll())
            recordsList = Gson().toJson(masterDataList)
            Log.d("msg", recordsList)

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

    private fun fetchSyncedRecords() {
//        binding.progressbar.show()
        val api = RetrofitHelper.getInstance().create(Client::class.java)
        GlobalScope.launch {
            val call: Call<JsonObject> = api.getSyncedRecords(
                recordsList
            )
            call.enqueue(object : Callback<JsonObject> {
                @SuppressLint("CommitPrefEdits", "NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                    if (response.isSuccessful) {
//                        binding.progressbar.hide()
                        val helper = ResponseHelper()
                        helper.ResponseHelper(response.body())
                        if (helper.isStatusSuccessful()) {
//                            val syncedMasterDataList = Gson().fromJson(
//                                helper.getDataAsString(),
//                                object : TypeToken<List<MasterData>>() {}.type
//                            )
                        } else {
                            NotificationsHelper().getErrorAlert(context, helper.getErrorMsg())
                        }
                    } else {
                        NotificationsHelper().getErrorAlert(context, "Response Error Code" + response.message())
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    NotificationsHelper().getErrorAlert(context, "Server Error")
                }
            })
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }
}

