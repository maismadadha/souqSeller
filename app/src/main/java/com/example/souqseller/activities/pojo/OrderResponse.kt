package com.example.souqseller.activities.pojo

data class OrderResponse(
    val id: Int,
    val customer_id: Int,
    val store_id: Int,
    val address_id: Int,
    val subtotal: String,
    val delivery_fee: String,
    val discount_total: String,
    val total_price: String,
    val items_count: Int,
    val note: String?,
    val payment_method: String?,
    val status: String,
    val created_at: String,
    val updated_at: String,
    val customer_name: String?,
    val items: List<OrderItem>

)
