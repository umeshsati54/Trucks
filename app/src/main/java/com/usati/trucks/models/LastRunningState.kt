package com.usati.trucks.models

data class LastRunningState(
    val lat: Double,
    val lng: Double,
    val stopNotficationSent: Int,
    val stopStartTime: Long,
    val truckId: Int,
    val truckRunningState: Int
)