package com.amtron.btc.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gender_table")
data class Gender(
    @PrimaryKey val id: Int?,
    var gender_name: String?
)
