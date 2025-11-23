package com.example.souqseller.activities.pojo

data class OrderItem(
    val id: Int,
    val order_id: Int,
    val product_id: Int,
    val quantity: Int,
    val price: String,
    val discount: String,
    val customizations: Map<String, String>?,  // لأنو sometimes JSON object
    val created_at: String,
    val updated_at: String,
    val product: Product


)
