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
import cn.pedant.SweetAlert.SweetAlertDialog
import com.amtron.btc.adapter.RecordsAdapter
import com.amtron.btc.database.AppDatabase
import com.amtron.btc.databinding.FragmentUnsyncedBinding
import com.amtron.btc.helper.NotificationsHelper
import com.amtron.btc.helper.ResponseHelper
import com.amtron.btc.helper.Util
import com.amtron.btc.model.MasterData
import com.amtron.btc.network.Client
import com.amtron.btc.network.RetrofitHelper
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

@DelicateCoroutinesApi
class UnSyncedFragment : Fragment() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var mContext: Context
    private lateinit var recyclerView: RecyclerView
    private lateinit var masterDataAdapter: RecordsAdapter
    private lateinit var appDatabase: AppDatabase
    private lateinit var masterDataList: ArrayList<MasterData>
    private lateinit var notBox: SweetAlertDialog
    private var recordsList: String = ""
    private var _binding: FragmentUnsyncedBinding? = null
    private val binding get() = _binding!!
    private var checkInternet: Boolean = false
    private var recordsPresent = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUnsyncedBinding.inflate(inflater, container, false)

        mContext = container!!.context
        sharedPreferences =
            this.requireActivity().getSharedPreferences("file", AppCompatActivity.MODE_PRIVATE)
        editor = sharedPreferences.edit()

        masterDataList = arrayListOf()
        appDatabase = AppDatabase.getDatabase(mContext)
        readData()

        recyclerView = binding.unSyncedRecordsRecyclerView

        val linearLayoutManager = LinearLayoutManager(mContext)
        recyclerView.layoutManager = linearLayoutManager
        masterDataAdapter = RecordsAdapter(masterDataList)
        recyclerView.adapter = masterDataAdapter

        binding.syncBtn.setOnClickListener {
            if (recordsPresent) {
                syncRecords()
            } else {
                NotificationsHelper().getWarningAlert(
                    mContext,
                    "No Records Found"
                )
            }
        }

        return binding.root
    }

    private fun syncRecords() {
        checkInternet = Util().isOnline(mContext)
        if (!checkInternet) {
            NotificationsHelper().getErrorAlert(
                mContext,
                "No Internet Connection Available"
            )
        } else {
            notBox = SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
            notBox.setTitleText("WARNING!")
                .setContentText("Are you sure you want to sync the records?")
                .setConfirmText("Yes")
                .setConfirmClickListener {
                    binding.progressbar.show()
                    val api = RetrofitHelper.getInstance().create(Client::class.java)
                    GlobalScope.launch {
                        val call: Call<JsonObject> = api.sendMasterData(
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
                                        binding.progressbar.hide()
                                        getAndUpdateMasterData(masterDataList)
                                    } else {
                                        NotificationsHelper().getErrorAlert(
                                            mContext,
                                            helper.getErrorMsg()
                                        )
                                    }
                                } else {
                                    binding.progressbar.hide()
                                    NotificationsHelper().getErrorAlert(
                                        mContext,
                                        "Response Error Code" + response.message()
                                    )
                                }
                            }

                            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                                binding.progressbar.hide()
                                NotificationsHelper().getErrorAlert(
                                    mContext,
                                    "Server Error"
                                )
                            }
                        })
                    }
                }
                .show()
        }
    }

    private fun getAndUpdateMasterData(masterDataList: ArrayList<MasterData>) {
        for (masterData in masterDataList) {
            GlobalScope.launch(Dispatchers.IO) {
                appDatabase.MasterDataDao().updateIsSyncedToTrueById(masterData.masterId!!)
            }
        }
        requireActivity().runOnUiThread {
            NotificationsHelper().getSuccessAlert(mContext, "Records Synced")
            notBox.dismiss()
            requireActivity().recreate()
        }
    }

    private fun readData() {
        GlobalScope.launch(Dispatchers.IO) {
            masterDataList.addAll(appDatabase.MasterDataDao().getUnSyncedMasterData())
            recordsList = Gson().toJson(masterDataList)

            if (masterDataList.isEmpty()) {
                requireActivity().runOnUiThread {
//                    binding.unSyncedRecordsRecyclerView.visibility = View.GONE
//                    binding.txtNoUnSyncedRecordsFound.visibility = View.VISIBLE
                    recordsPresent = false
                }
            } else {
                requireActivity().runOnUiThread {
//                    binding.unSyncedRecordsRecyclerView.visibility = View.VISIBLE
//                    binding.txtNoUnSyncedRecordsFound.visibility = View.GONE
                }
            }
        }
    }

}