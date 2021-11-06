package com.usati.trucks.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.usati.trucks.TrucksApplication
import com.usati.trucks.models.TruckResponse
import com.usati.trucks.repository.TruckRepository
import com.usati.trucks.utils.Resource
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Response

class TrucksViewModel(
    app: Application,
    private val truckRepository: TruckRepository
) : AndroidViewModel(app) {
    val trucks: MutableLiveData<Resource<TruckResponse>> = MutableLiveData()
    var truckResponse: TruckResponse? = null

    init {
        getTrucksData()
    }

    fun getTrucksData() = viewModelScope.launch {
        safeCall()
    }

    private fun handleResponse(response: Response<TruckResponse>): Resource<TruckResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                if (truckResponse == null) {
                    truckResponse = resultResponse
                } else {
                    val oldData = truckResponse?.data
                    val newData = resultResponse.data
                    oldData?.addAll(newData)
                }

                return Resource.Success(truckResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private suspend fun safeCall() {
        trucks.postValue(Resource.Loading())
        try {
            if (isConnected()) {
                val response = truckRepository.getTrucksData()
                trucks.postValue(handleResponse(response))
            } else {
                trucks.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> trucks.postValue(Resource.Error("Network Failure"))
                else -> trucks.postValue(Resource.Error(t.message + " Conversion Error"))
            }
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = getApplication<TrucksApplication>()
            .getSystemService(
                Context.CONNECTIVITY_SERVICE
            ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}