package com.example.darsh_practicle_task.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.darsh_practicle_task.MyApp
import com.example.darsh_practicle_task.R
import com.example.darsh_practicle_task.utils.TokenManager
import com.example.darsh_practicle_task.viewmodel.DashboardViewModel
import com.example.darsh_practicle_task.viewmodel.DashboardViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DashboardFragment : Fragment() {

    private val viewModel: DashboardViewModel by viewModels {
        DashboardViewModelFactory((requireActivity().application as MyApp).repository)
    }

    private lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tokenManager = TokenManager(requireContext())

        viewLifecycleOwner.lifecycleScope.launch {
            tokenManager.token.collect { token ->
                token?.let {
                    viewModel.fetchDashboardData("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MjU5MjcsImlhdCI6MTY3NDU1MDQ1MH0.dCkW0ox8tbjJA2GgUx2UEwNlbTZ7Rr38PVFJevYcXFI")
                }
            }
        }

       CoroutineScope(Dispatchers.IO).launch {
           viewModel.dashboardData.collect { data ->
               // Update UI with dashboard data
               view.findViewById<TextView>(R.id.dashboardText).text = data.toString()
           }
       }
    }
}
