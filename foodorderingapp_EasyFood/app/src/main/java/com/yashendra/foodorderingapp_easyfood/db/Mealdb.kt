package com.yashendra.foodorderingapp_easyfood.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.yashendra.foodorderingapp_easyfood.dataclasses.Meal

@Database(entities = [Meal::class], version = 1, exportSchema = false)
@TypeConverters(MealtypeConvertor::class)
abstract class Mealdb : RoomDatabase() {
    abstract fun mealDao():MealDao

    companion object{
        var Instance:Mealdb?=null
        @Synchronized
        fun getInstance(context: Context):Mealdb{
            if(Instance==null){
                Instance=Room.databaseBuilder(context,Mealdb::class.java,"meal.db")
                    .fallbackToDestructiveMigration().build()
            }
            return Instance as Mealdb
        }
    }
}