package com.example.souqseller.activities.pojo

data class CreateProductRespons(
    val cover_image: String,
    val created_at: String,
    val description: String,
    val id: Int,
    val main_image_url: Any,
    val name: String,
    val preparation_time: String,
    val price: String,
    val store_category_id: Int,
    val store_id: Int,
    val updated_at: String
)

