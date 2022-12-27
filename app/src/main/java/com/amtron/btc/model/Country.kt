package com.amtron.btc.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "countries_table")
data class Country(
    @PrimaryKey val id: Int?,
    var country_name: String?
)
