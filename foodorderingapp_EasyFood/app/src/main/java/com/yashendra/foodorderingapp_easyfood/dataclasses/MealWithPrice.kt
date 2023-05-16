package com.yashendra.foodorderingapp_easyfood.dataclasses

class MealWithPrice {
    var idMeal: String?=null
    var strMeal: String?=null
    var strMealThumb: String?=null
    var price:String?=null
    constructor()
    constructor(idMeal: String?, strMeal: String?, strMealThumb: String?, price: String?) {
        this.idMeal = idMeal
        this.strMeal = strMeal
        this.strMealThumb = strMealThumb
        this.price = price
    }

}

