package com.example.bbasia

data class ChartItem(
    val id: String,
    val name: String,
    val current_price: Int,
    val updown_rate: Double,
    val volume_power: Double,
    val eq: Int,
)
