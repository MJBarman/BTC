package com.amtron.btc.repository

import androidx.annotation.WorkerThread
import com.amtron.btc.dao.MasterDataDao
import com.amtron.btc.model.MasterData

class MasterDataRepo(private val masterDataDao: MasterDataDao) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allData: List<MasterData> = masterDataDao.getAll()

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(masterData: MasterData) {
        masterDataDao.insert(masterData)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun viewMasterData(id: Int) {
        masterDataDao.getById(id)
    }


}