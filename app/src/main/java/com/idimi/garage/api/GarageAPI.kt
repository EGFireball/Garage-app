package com.idimi.garage.api

import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GarageAPI {

    @GET("discover")
    suspend fun getPois(
        @Query("sw_corner") sw: String = "-84.540499,39.079888",
        @Query("ne_corner") ne: String = "-84.494260,39.113254",
        @Query("page_size") pageSize: Int = 50
    ): Response<JsonObject>
}