package com.amtron.btc.model

data class User(
    val token: String? = "",
    var mobile: String? = "",
    var password: String? = "",
    var login: Boolean = false
)
