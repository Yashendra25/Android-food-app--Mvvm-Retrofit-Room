package com.yashendra.foodorderingapp_easyfood.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yashendra.foodorderingapp_easyfood.R
import com.yashendra.foodorderingapp_easyfood.dataclasses.Category
import com.yashendra.foodorderingapp_easyfood.dataclasses.MealsByCategory

class CategoryMealsAdapter: RecyclerView.Adapter<CategoryMealsAdapter.CategoryMealviewHolder>() {

    private var mealsList=ArrayList<MealsByCategory>()
    var onItemClick:((MealsByCategory)->Unit)?=null
    fun setmealsList(mealslist:List<MealsByCategory>){
        this.mealsList=mealslist as ArrayList<MealsByCategory>
        notifyDataSetChanged()

    }


    inner class CategoryMealviewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val categoryImg: ImageView =itemView.findViewById(R.id.img_meal)
        val categorytext:TextView=itemView.findViewById(R.id.tv_meal_name)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryMealviewHolder {
        return CategoryMealviewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.meal_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CategoryMealviewHolder, position: Int) {
        Glide.with(holder.itemView).load(mealsList[position].strMealThumb).into(holder.categoryImg)
        holder.categorytext.text=mealsList[position].strMeal

        holder.itemView.setOnClickListener {
            onItemClick!!.invoke(mealsList[position])
        }

    }

    override fun getItemCount(): Int {
        return mealsList.size
    }
}