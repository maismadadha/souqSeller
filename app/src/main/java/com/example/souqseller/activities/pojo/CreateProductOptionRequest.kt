package com.example.souqseller.activities.pojo

data class CreateProductOptionRequest(
    val name: String,
    val label: String? = null,
    val selection: String? = "single", // "single" or "multi"
    val required: Boolean? = true,
    val affects_variant: Boolean? = true
)
