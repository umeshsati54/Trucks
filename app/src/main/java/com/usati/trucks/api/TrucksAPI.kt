package com.usati.trucks.api

import com.usati.trucks.models.TruckResponse
import com.usati.trucks.utils.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

//https://api.mystral.in/tt/mobile/logistics/searchTrucks?
// auth-company=PCH&
// companyId=33&
// deactivated=false&
// key=g2qb5jvucg7j8skpu5q7ria0mu&
// q-expand=true&
// q-include=lastRunningState,lastWaypoint
interface TrucksAPI {
    @GET("tt/mobile/logistics/searchTrucks")
    suspend fun getTrucksData(
        @Query("auth-company")
        authCompany: String = "PCH",
        @Query("companyId")
        companyId: Int = 33,
        @Query("deactivated")
        deactivated: Boolean = false,
        @Query("key")
        key: String = API_KEY,
        @Query("q-expand")
        qExpand: Boolean = true,
        @Query("q-include")
        qInclude: String = "lastRunningState,lastWaypoint"
    ): Response<TruckResponse>
}