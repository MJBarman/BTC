package com.amtron.btc.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "states_table")
data class State(
    @PrimaryKey val id: Int?,
    var state_name: String?
)
