package com.tt.driver.data.models.http

import com.tt.driver.data.models.entities.OrderReport

data class OrdersReportResponse(
    val `data`: OrderReports
) {
    data class OrderReports(
        val month: OrderReport,
        val today: OrderReport,
        val week: OrderReport
    )
}