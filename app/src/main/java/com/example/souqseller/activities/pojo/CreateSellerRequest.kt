package com.example.souqseller.activities.pojo

class CreateSellerRequest (
    val phone: String,
    val email: String?,               // ممكن يكون null
    val role: String = "seller",      // ثابت
    val name: String,
    val store_description: String,
    val main_category_id: Int,
    val password: String,
    val store_logo_url: String?,      // اختياري
    val store_cover_url: String?
)