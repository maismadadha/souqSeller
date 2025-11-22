package com.example.souqseller.activities.pojo

class User (
    val id: Int,
    val email: String?,
    val phone: String,
    val role: String,
    val created_at: String,
    val updated_at: String,
    val customer_profile: Any?,         // أو CustomerProfile? لو عاملاه
    val seller_profile: SellerProfile?, // مهم يكون ? عشان null تمشي
    val delivery_profile: Any?
)