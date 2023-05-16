package com.yashendra.foodorderingapp_easyfood.ViewModel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yashendra.foodorderingapp_easyfood.dataclasses.Meal
import com.yashendra.foodorderingapp_easyfood.dataclasses.MealList
import com.yashendra.foodorderingapp_easyfood.db.Mealdb
import com.yashendra.foodorderingapp_easyfood.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MealViewModel(val mealdatabase:Mealdb):ViewModel() {

    private var MealDeatailsLiveData=MutableLiveData<Meal>()
    fun getMealdetail(id:String){
        RetrofitInstance.api.getMealDetails(id).enqueue(object :Callback<MealList>{
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
               if(response.body()!=null){
                   MealDeatailsLiveData.value=response.body()!!.meals[0]
               }
                else{
                    return
               }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("MealActivity",t.message.toString())
            }
        })
    }
    fun observerMealDetailsLiveData():LiveData<Meal>{
        return MealDeatailsLiveData
    }
    fun insertmeal(meal:Meal){
        viewModelScope.launch {
            mealdatabase.mealDao().update(meal)
        }

    }
}