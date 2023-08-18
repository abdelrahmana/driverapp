package com.tt.driver.data.models.entities

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Order(
    var created_at: String?,
    var email: String?,
    var phone: String?,
    val customer: Customer?,
    val delivery_category: DeliveryCategory?,
    val driver: Driver?,
    val from_address: String?,
    val from_governorate: Governorate?,
    val from_lat: String?,
    val from_long: String?,
    val paid : String?,
    val from_name: String?,
    val from_phone: String?,
    val from_region: FromRegion?,
    val hours: String?,
    val id: Int?,
    val is_order_per_hour: Boolean?,
    val notes: String?,
    val order_category: OrderCategory?,
    val order_number: String?,
    val price: String?,
    val price_per_hour: String?,
    private val status: String?,
    val status_word: String?,
    val time: String?,
    val to_address: String?,
    val to_governorate: Governorate?,
    val to_lat: String?,
    val to_long: String?,
    val to_name: String?,
    val to_phone: String?,
    val to_region: ToRegion?,
    val two_way_status: String?,
    val warnings: List<WarningImage>?,
    val from_home_number: String?,
    val to_home_number: String?,
    val to_phone_extra: String?,
    val to_block: Block?,
    val from_block: Block?,
    val from_blvd: String?,
    val to_blvd: String?,
    val from_apartment_number: String?,
    val to_apartment_number: String?,
    val from_floor_number: String?,
    val to_floor_number: String?,
    val order_type : String?,
    val orders_count : String?,
    val price_per_order : String?,
    val total_order_price : String?,
    val total_price_per_hour : String?

) : Parcelable {

    fun getHeadedLocation() = when (getStatus()) {
        OrderStatus.PICK_UP -> {
            LatLng(
                to_lat?.toDoubleOrNull() ?: 0.0,
                to_long?.toDoubleOrNull() ?: 0.0
            )
        }
        OrderStatus.ON_THE_WAY -> {
            LatLng(
                to_lat?.toDoubleOrNull() ?: 0.0,
                to_long?.toDoubleOrNull() ?: 0.0
            )
        }
        else -> { //pending
            LatLng(
                from_lat?.toDoubleOrNull() ?: 0.0,
                from_long?.toDoubleOrNull() ?: 0.0
            )
        }
    }


    fun getStatus() = OrderStatus.getByCode(status)

    fun isActive() =
        getStatus() == OrderStatus.PENDING
                || getStatus() == OrderStatus.PICK_UP
                || getStatus() == OrderStatus.ON_THE_WAY


    @Parcelize
    data class WarningImage(
        val image: String
    ) : Parcelable

    @Parcelize
    data class Customer(
        val address: String?,
//        val car: Any?,
//        val car_id: Any?,
//        val civil_id: Any?,
//        val civil_id_end_date: Any?,
        val email: String?,
        val id: Int?,
        val image: String?,
        val lat: String?,
        val long: String?,
        val name: String?,
        val nationality: String?,
        val nationality_id: String?,
//        val notes: Any?,
//        val passport_end_date: Any?,
//        val passport_id: Any?,
        val phone: String?,
//        val residency: Any?,
//        val residency_end_date: Any?,
        val status: String?,
        val status_word: String?
    ) : Parcelable

    @Parcelize
    data class DeliveryCategory(
        val id: Int?,
        val name: String?
    ) : Parcelable

    @Parcelize
    data class Driver(
        val address: String?,
        val car: Car?,
        val car_id: String?,
        val civil_id: String?,
        val civil_id_end_date: String?,
        val email: String?,
        val id: Int?,
        val image: String?,
        val lat: Number?,
        val long: Number?,
        val name: String?,
        val nationality: String?,
        val nationality_id: String?,
        val notes: String?,
        val passport_end_date: String?,
        val passport_id: String?,
        val phone: String?,
        val residency: String?,
        val residency_end_date: String?,
        val status: String?,
        val status_word: String?
    ) : Parcelable {
        @Parcelize
        data class Car(
            val car_category: String?,
            val car_category_id: String?,
            val car_mark: String?,
            val car_mark_id: String?,
            val car_number: String?,
            val chassis_number: String?,
            val fuel: String?,
            val id: Int?,
            val insurance_end_date: String?,
            val license_end_date: String?,
            val model: String?,
            val transmission: String?,
            val year: String?
        ) : Parcelable
    }

    @Parcelize
    data class Governorate(
        val id: Int?,
        val name: String?
    ) : Parcelable

    @Parcelize
    data class FromRegion(
        val governorate: Governorate?,
        val id: Int?,
        val name: String?
    ) : Parcelable {
        @Parcelize
        data class Governorate(
            val id: Int?,
            val name: String?
        ) : Parcelable
    }

    @Parcelize
    data class OrderCategory(
        val id: Int?,
        val name: String?
    ) : Parcelable

    @Parcelize
    data class ToRegion(
        val governorate: Governorate?,
        val id: Int?,
        val name: String?
    ) : Parcelable {
        @Parcelize
        data class Governorate(
            val id: Int?,
            val name: String?
        ) : Parcelable
    }

    @Parcelize
    data class Block(
        val id: Int?,
        val name: String?
    ) : Parcelable
}