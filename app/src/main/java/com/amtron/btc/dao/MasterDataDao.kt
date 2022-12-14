package com.amtron.btc.dao

import androidx.room.*
import com.amtron.btc.model.MasterData

@Dao
interface MasterDataDao {

    @Query("SELECT * FROM MasterData")
    suspend fun getAll(): List<MasterData>

    @Query("SELECT * FROM MasterData WHERE isSynced = 0")
    suspend fun getUnSyncedMasterData(): List<MasterData>

    @Query("SELECT * FROM MasterData WHERE isSynced = 1")
    suspend fun getSyncedMasterData(): List<MasterData>

    @Query("SELECT * FROM MasterData WHERE masterId LIKE :id")
    suspend fun getById(id: Int): MasterData
    
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(masterData: MasterData)

    @Query("UPDATE MasterData SET isSynced= 1 WHERE masterId = :id")
    suspend fun updateIsSyncedToTrueById(id: Int)
    
    @Query("DELETE FROM MasterData")
    suspend fun deleteAll()

    @Query("DELETE FROM MasterData WHERE isSynced = :bool")
    suspend fun deleteBySynced(bool: Boolean)
}