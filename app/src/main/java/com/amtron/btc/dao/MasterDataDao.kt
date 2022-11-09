package com.amtron.btc.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.amtron.btc.model.MasterData

@Dao
interface MasterDataDao {

    @Query("SELECT * FROM MasterData")
    fun getAll(): List<MasterData>

    @Query("SELECT * FROM MasterData WHERE masterId > :id")
    fun getById(id: Int): MasterData
    
    @Insert
    fun insert(masterData: MasterData)
    
    @Delete
    fun delete(masterData: MasterData)
}