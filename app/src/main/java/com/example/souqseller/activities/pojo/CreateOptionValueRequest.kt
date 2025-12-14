package com.example.souqseller.activities.pojo

data class CreateOptionValueRequest(
    val value: String,
    val label: String? = null,
    val price_delta: Double? = 0.0
)
