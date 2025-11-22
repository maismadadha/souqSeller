package com.example.souqseller.activities.pojo

class SellerProfile (
    val user_id: Int,
    val name: String,
    val password: String,
    val store_description: String,
    val main_category_id: Int,
    val store_logo_url: String?,
    val store_cover_url: String?,
    val preparation_days: Int,
    val preparation_hours: Int,
    val delivery_price: Double?,       // أو Any? لو بدك ترتاحي
    val created_at: String,
    val updated_at: String
)