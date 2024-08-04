package com.example.darsh_practicle_task.repository

import com.example.darsh_practicle_task.model.DashboardResponse
import com.example.darsh_practicle_task.network.ApiService

class DashboardRepository(private val apiService: ApiService) {

    suspend fun getDashboardData(token: String): DashboardResponse {
        return apiService.getDashboardData(token)
    }
}
