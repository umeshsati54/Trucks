package com.usati.trucks.utils

import java.util.*

class Utils {
    companion object {
        fun getDateDifference(timestamp: Long): String {
            val createTimeDate = Date(timestamp)
            val result = Calendar.getInstance().timeInMillis - createTimeDate.time
            val seconds: Long = result / 1000
            val minutes = seconds / 60
            val hours = minutes / 60
            val days = hours / 24

            val result1: String = if (days < 1L) {
                if (hours < 1L){
                    if (minutes < 1L){
                        "$seconds secs"
                    }else{
                        "$minutes min"
                    }
                } else {
                    "$hours hours"
                }
            }else{
                "$days days"
            }
            return result1
        }

        fun getSeconds(timestamp: Long): Long {
            val createTimeDate = Date(timestamp)
            val result = Calendar.getInstance().timeInMillis - createTimeDate.time
            return result / 1000
        }
    }
}