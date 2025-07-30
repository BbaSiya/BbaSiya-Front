package com.example.bbasia

// models/TopPicResponse.kt
data class TopPicResponse(
    val stock_id: String,
    val stock_name: String,
    val eq: Int,
    val news: String,
    val bs: Double,
    val stock_type: String,
    val same_type_cnt: Int,
    val stock_industry: String,
    val same_industry_cnt: Int,
    val pattern_similarity: Double,
    val stocks: List<StocksData>,
    val chartData: List<ChartStockData>,
)

data class StocksData(
    val stock_id: String,
    val stock_name: String,
)

data class ChartStockData(
    val stock_id: String,
    val stock_name: String,
    val data: List<ChartPricePoint>
)

data class ChartPricePoint(
    val date: String,
    val closing_price: Int
)