package com.amtron.btc.network

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface Client {
    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("mobile") mobile: String?,
        @Field("password") password: String?
    ) : Call<JsonObject>

    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("upload-visitor-data")
    fun sendMasterData(
        @Field("masterData") masterData: String?
    ) : Call<JsonObject>
}