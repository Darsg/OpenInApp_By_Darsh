package com.example.darsh_practicle_task.network

import com.example.darsh_practicle_task.model.DashboardResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header

interface ApiService {
    @GET("dashboardNew")
    suspend fun getDashboardData(
        @Header("Authorization") token: String
    ): DashboardResponse

    companion object {
        private const val BASE_URL = "https://api.inopenapp.com/api/v1/"

        fun create(): ApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}