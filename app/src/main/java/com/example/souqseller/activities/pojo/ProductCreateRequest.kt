package com.example.souqseller.activities.pojo

data class ProductCreateRequest(
    val store_id: Int,
    val store_category_id: Int,
    val name: String,
    val description: String?,
    val price: Double,
    val preparation_time: String,
)

