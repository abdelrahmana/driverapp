package com.tt.driver.data.models.entities

data class OrderReport(
    val total: Int,
    val completed: Int,
    val inprogress: Int,
    val comming: Int
)