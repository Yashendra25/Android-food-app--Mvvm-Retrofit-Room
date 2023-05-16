package com.yashendra.foodorderingapp_easyfood.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yashendra.foodorderingapp_easyfood.dataclasses.MealsByCategory
import com.yashendra.foodorderingapp_easyfood.dataclasses.MealsByCategoryList
import com.yashendra.foodorderingapp_easyfood.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryViewModel:ViewModel() {
    val mealsLiveData=MutableLiveData < List<MealsByCategory>>()

    fun getMealsByCategory(categoryName:String){
        RetrofitInstance.api.getPopularItems(categoryName).enqueue(object :
            Callback<MealsByCategoryList> {
            override fun onResponse(call: Call<MealsByCategoryList>, response: Response<MealsByCategoryList>) {
                response.body()?.let {
                    mealslist->
                    mealsLiveData.postValue(mealslist.meals)
                }
            }

            override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {
                Log.d("CategoryMealsViewModel",t.message.toString())
            }

        })

    }
    fun observeMealsLiveData():LiveData<List<MealsByCategory>>{
        return mealsLiveData
    }
}