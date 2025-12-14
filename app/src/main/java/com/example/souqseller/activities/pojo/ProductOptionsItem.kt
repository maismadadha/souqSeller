package com.example.souqseller.activities.pojo

data class ProductOptionsItem(
    val affects_variant: Int,
    val created_at: String,
    var id: Int? = null,
    var name: String? = null,
    val label: String,
    val product_id: Int,
    val required: Int,
    val selection: String,
    val sort_order: Int,
    val updated_at: String,
    val values: List<Value>
)