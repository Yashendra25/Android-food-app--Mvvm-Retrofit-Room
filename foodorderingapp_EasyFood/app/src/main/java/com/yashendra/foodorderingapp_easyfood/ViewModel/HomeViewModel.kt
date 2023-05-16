package com.yashendra.foodorderingapp_easyfood.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yashendra.foodorderingapp_easyfood.dataclasses.*
import com.yashendra.foodorderingapp_easyfood.db.Mealdb
import com.yashendra.foodorderingapp_easyfood.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.log

class HomeViewModel(private val mealdatabase:Mealdb): ViewModel() {
    private var randomMealLivedata=MutableLiveData<Meal>()
    private var popularItemsLiveData=MutableLiveData<List<MealsByCategory>>()
    private  var categoriesLivedata=MutableLiveData<List<Category>>()
    private var favouritesMealsLivedata=mealdatabase.mealDao().getAllMeals()
    private var bottomsheetMealLivedata =MutableLiveData<Meal>()
    private var searchmealsLivedata=MutableLiveData<List<Meal>>()
    private var savestateRandomMeal:Meal?=null
    private var addressLivedata=MutableLiveData<String>()

    fun setaddressdata(data:String){
        addressLivedata.postValue(data)

    }
    fun getRandomMeal(){

        savestateRandomMeal?.let {
            randommeal->
            randomMealLivedata.postValue(randommeal)
            return
        }
        RetrofitInstance.api.getRandomMeal().enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.body()!=null){
                    val randomMeal=response.body()!!.meals[0]
                    randomMealLivedata.value=randomMeal

                    savestateRandomMeal=randomMeal

                }
                else{
                    return
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("home fragment",t.message.toString())
            }
        })
    }

    fun getPopularItems(){
        RetrofitInstance.api.getPopularItems("Seafood").enqueue(object :Callback<MealsByCategoryList>{
            override fun onResponse(call: Call<MealsByCategoryList>, response: Response<MealsByCategoryList>) {
                if(response.body()!=null){
                    popularItemsLiveData.value=response.body()!!.meals
                }
            }

            override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {
                Log.d("HomeFragment",t.message.toString())
            }

        })
    }
    fun getCategories(){
        RetrofitInstance.api.getCategories().enqueue(object :Callback<CategoryList>{
            override fun onResponse(call: Call<CategoryList>, response: Response<CategoryList>) {
                response.body()?.let { categoryList ->
                    categoriesLivedata.postValue(categoryList.categories)
                }
            }

            override fun onFailure(call: Call<CategoryList>, t: Throwable) {
                Log.e("HomeFragment",t.message.toString())
            }

        })
    }

    fun getMealbyid(id:String){
        RetrofitInstance.api.getMealDetails(id).enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                val meal=response.body()?.meals?.first()
                meal?.let {
                    bottomsheetMealLivedata.postValue(it)
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
               Log.d("bottomsheetLivedata",t.toString())
            }

        })
    }

    fun ObserveAddressLivedata(): MutableLiveData<String> {
        return addressLivedata
    }
    fun observeBottomSheetLivedata():LiveData<Meal>{
        return bottomsheetMealLivedata
    }

    fun observeRandomMealLivedata():LiveData<Meal>{
        return randomMealLivedata
    }
    fun observePopularItemsLivedata():LiveData<List<MealsByCategory>>{
        return popularItemsLiveData

    }
    fun observeCategoriesLivedata():LiveData<List<Category>>{
        return categoriesLivedata
    }
    fun observeFavouritesMealslivedata():LiveData<List<Meal>>{
        return favouritesMealsLivedata
    }
    fun insertmeal(meal:Meal){
        viewModelScope.launch {
            mealdatabase.mealDao().update(meal)
        }

    }
    fun deleteMeal(meal:Meal){
        viewModelScope.launch {
            mealdatabase.mealDao().delete(meal)
        }
    }
    fun serachMeal(searchQuery:String)=RetrofitInstance.api.searchMeals(searchQuery).enqueue(object :Callback<MealList>{
        override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
            val mealslist=response.body()?.meals
            mealslist?.let {
                searchmealsLivedata.postValue(it)
            }
        }

        override fun onFailure(call: Call<MealList>, t: Throwable) {
            Log.d("Search failure",t.toString())
        }

    })
    fun observeSerachmealsLivedata():LiveData<List<Meal>> =searchmealsLivedata
}