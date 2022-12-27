package com.amtron.btc.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity
data class MasterData(
    @PrimaryKey(autoGenerate = true) val masterId: Int?,
    val name: String,
    val age: Int,
    val gender: Gender,
    val country: Country?,
    val state: State?,
    val nationality: String,
    val domicile: Domicile?,
    val date: String,
    var isSynced: Boolean
)