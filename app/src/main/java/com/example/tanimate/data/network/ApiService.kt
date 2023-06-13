package com.example.tanimate.data.network

import com.example.tanimate.data.network.responses.LoginResponse
import com.example.tanimate.data.network.responses.RegisterResponse
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {

    @POST("api/login")
    fun login(
        @Body raw: JsonObject
    ): Call<LoginResponse>


    @POST("api/register")
    fun register(
        @Body raw: JsonObject
    ): Call<RegisterResponse>
}