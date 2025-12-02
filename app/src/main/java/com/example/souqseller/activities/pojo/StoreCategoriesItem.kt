package com.example.souqseller.activities.pojo

data class StoreCategoriesItem(
    val id: Int,
    val name: String,
    val store_id: Int,
    val created_at: String,
    val updated_at: String,
    val products: List<Product>
)