package com.yashendra.foodorderingapp_easyfood.db

import android.util.Log
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.yashendra.foodorderingapp_easyfood.dataclasses.MealWithPrice
import com.yashendra.foodorderingapp_easyfood.dataclasses.MealsByCategory

class fbdao {
    val db = FirebaseFirestore.getInstance()
    val Collcartections = db.collection("Cart")

    fun addMeal(mealId: String, mealName: String, mealThumb: String, price: String) {
        val meal = MealWithPrice(mealId, mealName, mealThumb, price)

        Collcartections.document().set(meal)
    }

    fun deleteCartDocument(mealId: String) {
        Collcartections.document(mealId)
            .delete()
            .addOnSuccessListener {
                Log.d("fbdao", "DocumentSnapshot successfully deleted!")
                return@addOnSuccessListener
            }
            .addOnFailureListener { e ->
                Log.w("fbdao", "Error deleting document", e)
                return@addOnFailureListener
            }
    }
}
