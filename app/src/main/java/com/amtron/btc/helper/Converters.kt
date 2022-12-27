package com.amtron.btc.helper

import androidx.room.TypeConverter
import com.amtron.btc.model.Country
import com.amtron.btc.model.Domicile
import com.amtron.btc.model.Gender
import com.amtron.btc.model.State
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun genderToString(gender: Gender): String = Gson().toJson(gender)

    @TypeConverter
    fun genderFromString(gender: String): Gender {
        return Gson().fromJson(
            gender,
            object : TypeToken<Gender>() {}.type
        )
    }

    @TypeConverter
    fun stateToString(state: State?): String = Gson().toJson(state)

    @TypeConverter
    fun stateFromString(state: String): State? {
        return Gson().fromJson(
            state,
            object : TypeToken<State>() {}.type
        )
    }

    @TypeConverter
    fun countryToString(country: Country?): String = Gson().toJson(country)

    @TypeConverter
    fun countryFromString(country: String): Country? {
        return Gson().fromJson(
            country,
            object : TypeToken<Country>() {}.type
        )
    }

    @TypeConverter
    fun domicileToString(domicile: Domicile?): String = Gson().toJson(domicile)

    @TypeConverter
    fun domicileFromString(domicile: String): Domicile? {
        return Gson().fromJson(
            domicile,
            object : TypeToken<Domicile>() {}.type
        )
    }
}