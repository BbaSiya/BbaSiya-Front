package com.example.bbasia

import com.google.gson.annotations.SerializedName
import retrofit2.Call

data class StockNewsResponse(
    @SerializedName("news") val news: String,
    @SerializedName("bs") val bs: Double,
    @SerializedName("eq") val eq: Int
)

