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
    @POST("")
    fun getSyncedRecords(
        @Field("masterData") masterData: String?
    ) : Call<JsonObject>

    /*@Headers("Accept: application/json")
    @GET("getSourceGhats")
    fun getSrcGhats(
        @Header("Authorization") bearer: String?
    ): Call<JsonObject>*/
}