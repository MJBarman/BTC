package com.amtron.btc.ui.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amtron.btc.adapter.RecordsAdapter
import com.amtron.btc.database.AppDatabase
import com.amtron.btc.databinding.FragmentSyncedBinding
import com.amtron.btc.databinding.FragmentUnsyncedBinding
import com.amtron.btc.helper.NotificationsHelper
import com.amtron.btc.helper.Util
import com.amtron.btc.model.MasterData
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class UnSyncedFragment : Fragment() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var mContext: Context
    private lateinit var recyclerView: RecyclerView
    private lateinit var masterDataAdapter: RecordsAdapter
    private lateinit var appDatabase: AppDatabase
    private lateinit var masterDataList: ArrayList<MasterData>
    private var recordsList: String = ""
    private var _binding: FragmentUnsyncedBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUnsyncedBinding.inflate(inflater, container, false)

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

        return binding.root
    }

    private fun readData() {
        GlobalScope.launch(Dispatchers.IO) {
            masterDataList.addAll(appDatabase.MasterDataDao().getUnSyncedMasterData())
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

}