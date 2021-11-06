package com.usati.trucks.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.usati.trucks.R
import com.usati.trucks.repository.TruckRepository
import com.usati.trucks.ui.ListFragment.Companion.searchView
import com.usati.trucks.viewmodel.TrucksViewModel
import com.usati.trucks.viewmodel.TrucksViewModelProviderFactory

class MainActivity : AppCompatActivity() {
    lateinit var viewModel: TrucksViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val truckRepository = TruckRepository()
        val viewModelProviderFactory = TrucksViewModelProviderFactory(application, truckRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(TrucksViewModel::class.java)
    }

    override fun onBackPressed() {
        if (!searchView.isIconified) {
            searchView.isIconified = true
            return
        }
        super.onBackPressed()
    }
}