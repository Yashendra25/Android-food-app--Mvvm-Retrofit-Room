package com.yashendra.foodorderingapp_easyfood.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.yashendra.foodorderingapp_easyfood.dataclasses.Meal

@Dao
interface MealDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(meal: Meal)

    @Delete
    suspend fun delete(meal: Meal)

    @Query("SELECT * FROM mealInformation")
    fun getAllMeals():LiveData<List<Meal>>
}