package com.tt.driver.data.models.entities

enum class OrderStatus(val code: String) {
    PENDING("0"),
    PICK_UP("1"),
    ON_THE_WAY("2"),
    COMPLETED("3"),
    CANCELED_BY_CUSTOMER("4"),
    CANCELLED_BY_CLIENT("5"),
    CANCELLED_BY_ADMIN("6");

    companion object {
        fun getByCode(code: String?): OrderStatus {
            return values().find { it.code == code } ?: COMPLETED
        }
    }

}