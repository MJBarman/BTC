package com.amtron.btc.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "domicile_table")
data class Domicile(
    @PrimaryKey val id: Int?,
    val dom_name: String?
)
