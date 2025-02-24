package com.tt.driver.data.models.http

import com.google.gson.annotations.SerializedName

data class RunSheetResponse(
    val `data`: ArrayList<Datax>
)

data class Datax(
    val date: String,
    val name: String,
    val shipments: Int,
    val slots: List<Slot>
)

data class Slot(
    val delivered: Int,
    @SerializedName("end_time")
    val endTime: String,
    val id: Int,
    val reject: Int,
    val reschedule: Int,
    val shipments: Int,
    @SerializedName("start_time")
    val startTime: String
)