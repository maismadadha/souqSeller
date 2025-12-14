package com.example.souqseller.activities.pojo

class CreateSellerRequest (
    val phone: String,
    val email: String?,
    val role: String = "seller",
    val name: String,
    val store_description: String,
    val main_category_id: Int,
    val password: String,
    val store_logo_url: String?,
    val store_cover_url: String?
)