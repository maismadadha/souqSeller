package com.example.souqseller.activities.pojo

data class Value(
    val created_at: String,
    var id: Int? = null,
    var label: String? = null,
    val option_id: Int,
    val price_delta: String,
    val sort_order: Int,
    val updated_at: String,
    var value: String
)