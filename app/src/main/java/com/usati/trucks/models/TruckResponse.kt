package com.usati.trucks.models

data class TruckResponse(
    val data: MutableList<Data>,
    val responseCode: ResponseCode
)