package com.yashendra.foodorderingapp_easyfood.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yashendra.foodorderingapp_easyfood.R
import com.yashendra.foodorderingapp_easyfood.dataclasses.MealsByCategory

class MostPopularAdapter: RecyclerView.Adapter<MostPopularAdapter.PopularMealViewHolder>() {
    private var mealslist=ArrayList<MealsByCategory>()
    lateinit var onitemClick:((MealsByCategory)->Unit)

    var onLongItemclick:((MealsByCategory)->Unit)?=null

    fun setMeals(mealslist:ArrayList<MealsByCategory>){
        this.mealslist=mealslist
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMealViewHolder {
        return PopularMealViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.popular_items,parent,false))
    }

    override fun onBindViewHolder(holder: PopularMealViewHolder, position: Int) {
        Glide.with(holder.itemView).load(mealslist[position].strMealThumb).into(holder.popularimg)

        holder.itemView.setOnClickListener {
            onitemClick.invoke(mealslist[position])
        }
        holder.itemView.setOnLongClickListener {
            onLongItemclick?.invoke(mealslist[position])
            true
        }
    }

    override fun getItemCount(): Int {
        return mealslist.size
    }

    class PopularMealViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val popularimg:ImageView=itemView.findViewById(R.id.img_popular_item)
    }
}