package com.yashendra.foodorderingapp_easyfood.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import com.yashendra.foodorderingapp_easyfood.db.Mealdb

class HomelViewModelFactory(val mealdatabase: Mealdb):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(mealdatabase) as T
    }
}