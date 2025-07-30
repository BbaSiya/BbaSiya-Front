package com.example.bbasia

//data class CategoryItem(
//    val category_id: Int,
//    val category_name: String,
////    val name1: String,
////    val name2: String,
////    val imageResId: Int
//)

import com.google.gson.annotations.SerializedName

data class CategoryItem(
    val id: Int,
    val name: String,
    val description: String,

)
