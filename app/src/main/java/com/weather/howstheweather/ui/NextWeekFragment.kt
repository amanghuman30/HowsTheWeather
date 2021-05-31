package com.weather.howstheweather.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.weather.howstheweather.R
import com.weather.howstheweather.adapters.ForecastAdapter
import com.weather.howstheweather.api.Resource
import com.weather.howstheweather.viewModels.ForecastViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_next_week.*

@AndroidEntryPoint
class NextWeekFragment() : Fragment(R.layout.fragment_next_week) {

    val forecastViewModel : ForecastViewModel by viewModels()
    lateinit var forecastAdapter : ForecastAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainActivity = activity as MainActivity

        mainActivity.userLocationLiveData.observe(viewLifecycleOwner, { location ->
            forecastViewModel.getForecastData(location.latitude, location.longitude)
        })

        setupRecyclerView()

        forecastViewModel.forecastListLiveData.observe(viewLifecycleOwner, { response ->
            when(response) {
                is Resource.Success -> {
                    progressBarForecast.visibility = View.GONE
                    forecastAdapter?.listDiffer.submitList(response.data?.list)
                }
                is Resource.Loading -> {
                    progressBarForecast.visibility = View.VISIBLE
                }
                is Resource.Error -> {
                    progressBarForecast.visibility = View.GONE
                    Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun setupRecyclerView() {
        forecastAdapter = ForecastAdapter()
        rvNextWeek.apply {
            adapter = forecastAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

}