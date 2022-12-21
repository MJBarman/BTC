package com.amtron.btc.model

data class LoginCredentials(
    val user: User,
    val genders: List<Gender>,
    val countries: List<Country>,
    val states: List<State>,
    val domicile: List<Domicile>
)
