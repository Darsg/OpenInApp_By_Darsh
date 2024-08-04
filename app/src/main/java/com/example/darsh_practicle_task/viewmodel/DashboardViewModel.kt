/*
package com.example.darsh_practicle_task.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.darsh_practicle_task.model.DashboardResponse
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DashboardViewModel(private val repository: DashboardRepository) : ViewModel() {

    private val _dashboardData = MutableStateFlow<DashboardResponse?>(null)
    val dashboardData: StateFlow<DashboardResponse?> get() = _dashboardData

    fun fetchDashboardData(token: String) {
        viewModelScope.launch {
            val data = repository.getDashboardData(token)
            _dashboardData.value = data
        }
    }
}
*/
package com.example.darsh_practicle_task.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.darsh_practicle_task.model.DashboardResponse
import com.example.darsh_practicle_task.repository.DashboardRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.HttpException

class DashboardViewModel(private val repository: DashboardRepository) : ViewModel() {

    private val _dashboardData = MutableStateFlow<DashboardResponse?>(null)
    val dashboardData: StateFlow<DashboardResponse?> get() = _dashboardData

    fun fetchDashboardData(token: String) {
        viewModelScope.launch {
            try {
                val data = repository.getDashboardData(token)
                _dashboardData.value = data
            } catch (e: HttpException) {
                if (e.code() == 401) {
                    Log.e("DashboardViewModel", "Unauthorized: Invalid or expired token")
                } else {
                    Log.e("DashboardViewModel", "HTTP error: ${e.code()}")
                }
            } catch (e: Exception) {
                Log.e("DashboardViewModel", "Error fetching dashboard data", e)
            }
        }
    }
}
