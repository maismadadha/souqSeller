package com.example.souqseller.activities.pojo

data class CreateProductOptionsRespons(
    val affects_variant: Boolean,
    val created_at: String,
    val id: Int,
    val label: String,
    val name: String,
    val product_id: Int,
    val required: Boolean,
    val selection: String,
    val sort_order: Int,
    val updated_at: String,
    val values: List<OptionValueresponse>
)