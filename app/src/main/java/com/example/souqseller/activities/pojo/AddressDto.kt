package com.example.souqseller.activities.pojo

data class AddressDto(
    val id: Int,
    val user_id: Int,

    val address_name: String,

    val city_name: String,

    val street: String?,           // ✅ nullable
    val building_number: Int?,      // ✅ nullable
    val address_note: String?,      // ✅ nullable

    val latitude: Double,
    val longitude: Double,

    val created_at: String,
    val updated_at: String,

    val is_default: Int             // 0 أو 1
)
