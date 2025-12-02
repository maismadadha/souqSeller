package com.example.souqseller.activities.pojo

data class Product(
    val id: Int,
    val store_id: Int,
    val store_category_id: Int,
    val name: String,
    val description: String?,
    val price: String,
    val preparation_time: String,
    val attributes: Any?,     // أحيانًا null
    val created_at: String,
    val updated_at: String,
    val main_image_url: String,
)
