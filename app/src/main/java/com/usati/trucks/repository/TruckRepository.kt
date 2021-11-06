package com.usati.trucks.repository

import com.usati.trucks.api.RetrofitInstance

class TruckRepository {
    suspend fun getTrucksData() =
        RetrofitInstance.api.getTrucksData()
}