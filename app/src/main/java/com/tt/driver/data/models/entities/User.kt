package com.tt.driver.data.models.entities

data class User(
    val id: Int?,
    val address: Address?,
    val car: Car?,
    val car_id: String?,
    val civil_id: String?,
    val civil_id_end_date: String?,
    val driving_license: String?,
    val driving_license_category: String?,
    val driving_license_category_id: String?,
    val email: String?,
    val name: String?,
    val nationality: String?,
    val nationality_id: String?,
    val notes: String?,
    val pasport_end_date: String?,
    val pasport_id: String?,
    val phone: Any?,
    val receipt_date: String?,
    val recruitment_date: Any?,
    val residency: String?,
    val residency_end_date: String?,
    val salary: String?,
    val salary_type: String?,
    val salary_type_id: String?,
    val status: String?,
    val status_word: Any?,
    var image: String?,
    private var is_online: Int
) {
    fun isOnline() = is_online == 1

    fun updateStatus(isOnline: Boolean) {
        is_online = if (isOnline) 1 else 0
    }
}