package com.amtron.btc.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MasterData (
    @PrimaryKey(autoGenerate = true) val masterId: Int,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "age") val age: Int?,
    @ColumnInfo(name = "gender") val gender: String?,
    @ColumnInfo(name = "country") val country: String?,
    @ColumnInfo(name = "state") val state: String?,
    @ColumnInfo(name = "residency") val residency: String,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "isSynced") val isSynced: Boolean
)