package com.amtron.btc.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

data class LoginCredentials(
    val user: User,
    val genders: List<Gender>,
    val countries: List<Country>,
    val states: List<State>,
    val domicile: List<Domicile>
)
