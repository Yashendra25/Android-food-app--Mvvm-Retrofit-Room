package com.yashendra.foodorderingapp_easyfood.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yashendra.foodorderingapp_easyfood.R
import com.yashendra.foodorderingapp_easyfood.dataclasses.Meal

class FavouriteMealsAdapter:RecyclerView.Adapter<FavouriteMealsAdapter.FavouriteMealsViewHolder> (){

    lateinit var onfavclick:((Meal)->Unit)

     inner class FavouriteMealsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
         val categoryImg: ImageView =itemView.findViewById(R.id.img_meal)
         val categorytext: TextView =itemView.findViewById(R.id.tv_meal_name)
    }
    //diffutill->better method then notify data set changed() it does not refresh alll the content it only change the content which is affected
    private val diffUtil= object : DiffUtil.ItemCallback<Meal>() {
        override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem.idMeal==newItem.idMeal
        }

        override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem==newItem
        }

    }
    val differ=AsyncListDiffer(this,diffUtil)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteMealsViewHolder {
        return FavouriteMealsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.meal_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: FavouriteMealsViewHolder, position: Int) {
        val meal=differ.currentList[position]
        Glide.with(holder.itemView).load(meal.strMealThumb).into(holder.categoryImg)
        holder.categorytext.text=meal.strMeal

        holder.itemView.setOnClickListener {
            onfavclick.invoke(differ.currentList[position])
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}