package com.tt.driver.data.models.entities

data class PaymentReport(
    val month: Report?,
    val today: Report?,
    val week: Report?
) {
    data class Report(
        val cash: Cash?,
        val knet: Knet?,
        val total: Total?,
    ) {
        data class Cash(
            val count: Int? = 0,
            val sum: Int? = 0
        )

        data class Knet(
            val count: Int? = 0,
            val sum: Int? = 0
        )

        data class Total(
            val count: Int? = 0,
            val sum: Int? = 0
        )
    }
}