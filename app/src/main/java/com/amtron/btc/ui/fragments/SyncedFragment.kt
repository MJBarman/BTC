package com.amtron.btc.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amtron.btc.R
import com.amtron.btc.adapter.RecordsAdapter
import com.amtron.btc.database.AppDatabase
import com.amtron.btc.databinding.FragmentSyncedBinding
import com.amtron.btc.helper.NotificationsHelper
import com.amtron.btc.helper.ResponseHelper
import com.amtron.btc.helper.Util
import com.amtron.btc.model.MasterData
import com.amtron.btc.network.Client
import com.amtron.btc.network.RetrofitHelper
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SyncedFragment : Fragment() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var mContext: Context
    private lateinit var recyclerView: RecyclerView
    private lateinit var masterDataAdapter: RecordsAdapter
    private lateinit var appDatabase: AppDatabase
    private lateinit var masterDataList: ArrayList<MasterData>
    private var checkInternet: Boolean = false
    private var recordsList: String = ""
    private var _binding: FragmentSyncedBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSyncedBinding.inflate(inflater, container, false)

        mContext = container!!.context
        sharedPreferences =
            this.activity!!.getSharedPreferences("file", AppCompatActivity.MODE_PRIVATE)
        editor = sharedPreferences.edit()

        masterDataList = arrayListOf()
        appDatabase = AppDatabase.getDatabase(mContext)
        readData()

        recyclerView = binding.recordsRecyclerView

        val linearLayoutManager = LinearLayoutManager(mContext)
        recyclerView.layoutManager = linearLayoutManager

        masterDataAdapter = RecordsAdapter(masterDataList)
        recyclerView.adapter = masterDataAdapter

        binding.sync.setOnClickListener {
            checkInternet = Util().isOnline(mContext)
            if (!checkInternet) {
                NotificationsHelper().getErrorAlert(mContext, "No Internet Connection Available")
            } else {
                //get masterData from server
                fetchSyncedRecords()
            }
        }

        return binding.root
    }

    private fun readData() {
        GlobalScope.launch(Dispatchers.IO) {
            masterDataList.addAll(appDatabase.MasterDataDao().getSyncedMasterData())
            recordsList = Gson().toJson(masterDataList)

            if (masterDataList.isEmpty()) {
                activity!!.runOnUiThread {
                    NotificationsHelper().getWarningAlert(
                        mContext,
                        "No Records Found"
                    )
                }
            }
        }
    }

    private fun fetchSyncedRecords() {
        binding.progressbar.show()
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
                        binding.progressbar.hide()
                        val helper = ResponseHelper()
                        helper.ResponseHelper(response.body())
                        if (helper.isStatusSuccessful()) {
//                            val syncedMasterDataList = Gson().fromJson(
//                                helper.getDataAsString(),
//                                object : TypeToken<List<MasterData>>() {}.type
//                            )
                        } else {
                            NotificationsHelper().getErrorAlert(mContext, helper.getErrorMsg())
                        }
                    } else {
                        NotificationsHelper().getErrorAlert(
                            mContext,
                            "Response Error Code" + response.message()
                        )
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    NotificationsHelper().getErrorAlert(mContext, "Server Error")
                }
            })
        }
    }
}