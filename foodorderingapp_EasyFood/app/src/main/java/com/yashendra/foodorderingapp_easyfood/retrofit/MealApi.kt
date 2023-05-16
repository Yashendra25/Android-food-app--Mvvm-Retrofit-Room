package com.yashendra.foodorderingapp_easyfood.retrofit

import com.yashendra.foodorderingapp_easyfood.dataclasses.Category
import com.yashendra.foodorderingapp_easyfood.dataclasses.CategoryList
import com.yashendra.foodorderingapp_easyfood.dataclasses.MealsByCategoryList
import com.yashendra.foodorderingapp_easyfood.dataclasses.MealList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApi {
    @GET("random.php")
    fun getRandomMeal():Call<MealList>

    @GET("lookup.php?")
    fun getMealDetails(@Query("i")id:String):Call<MealList>

    @GET("filter.php?")
    fun getPopularItems(@Query("c")categoryname:String):Call<MealsByCategoryList>

    @GET("categories.php")
    fun getCategories():Call<CategoryList>

    @GET("filter.php")
    fun getMealsBycategory(@Query("c")categoryname:String):Call<MealsByCategoryList>

    @GET("search.php")
    fun searchMeals(@Query("s")searchQuery:String):Call<MealList>
}