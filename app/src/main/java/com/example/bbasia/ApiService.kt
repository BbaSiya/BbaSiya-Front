package com.example.bbasia

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("/api/category")
    fun getCategories():Call<List<CategoryItem>>

    @GET("/api/category/category_id={id}")
    fun getChartItems(@Path("id") categoryId: Int): Call<List<ChartItem>>
//    @GET("api/category/")
//    fun getChartItems(@Query("category_id") categoryId: Int): Call<List<ChartItem>>

    @GET("/api/stock/stock_id={stockId}&user_id={userId}")
    suspend fun getStockNews(
        @Path("stockId") stockId: Int,
        @Path("userId") userId: Int
    ): List<StockNewsResponse>
}
