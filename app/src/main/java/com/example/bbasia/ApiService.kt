package com.example.bbasia

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("/api/category")
    fun getCategories():Call<List<CategoryItem>>

    @GET("/api/category/category_id={id}")
    fun getChartItems(@Path("id") categoryId: Int): Call<List<ChartItem>>

    @GET("/api/stock/stock_id={stockId}&user_id={userId}")
    fun getStockNews(
        @Path("stockId") stockId: String,
        @Path("userId") userId: Int
    ): Call<StockNewsResponse>

    @GET("/api/topPic")
    fun getTopPick(
        @Query("user_id") userId: Int,
        @Query("category_id") categoryId: Int
    ): Call<TopPicResponse>
}
