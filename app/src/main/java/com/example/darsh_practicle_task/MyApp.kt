package com.example.darsh_practicle_task

import android.app.Application
import com.example.darsh_practicle_task.network.ApiService
import com.example.darsh_practicle_task.repository.DashboardRepository

class MyApp : Application() {
    val apiService by lazy { ApiService.create() }
    val repository by lazy { DashboardRepository(apiService) }
}