package com.usati.trucks.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.usati.trucks.repository.TruckRepository

class TrucksViewModelProviderFactory(
    val app: Application,
    private val truckRepository: TruckRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TrucksViewModel(app, truckRepository) as T
    }
}