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
import com.yashendra.foodorderingapp_easyfood.dataclasses.CategoryList

class CategoriesAdapter: RecyclerView.Adapter<CategoriesAdapter.CategoryViewholder>() {

    private var categoriesList=ArrayList<Category>()
    var onItemClick:((Category)->Unit)?=null
    fun setCategoryList(categoriesList: List<Category>){
        this.categoriesList=categoriesList as ArrayList<Category>
        notifyDataSetChanged()
    }


    inner class CategoryViewholder(itemView: View) :RecyclerView.ViewHolder(itemView){
        val categoryimg: ImageView =itemView.findViewById(R.id.img_category)
        val categorytext:TextView=itemView.findViewById(R.id.tv_category_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewholder {
        return CategoryViewholder(LayoutInflater.from(parent.context).inflate(R.layout.category_item,parent,false))

    }

    override fun onBindViewHolder(holder: CategoryViewholder, position: Int) {
        Glide.with(holder.itemView).load(categoriesList[position].strCategoryThumb).into(holder.categoryimg)
        holder.categorytext.text=categoriesList[position].strCategory

        holder.itemView.setOnClickListener {
            onItemClick!!.invoke(categoriesList[position])
        }
    }

    override fun getItemCount(): Int {
        return categoriesList.size
    }
}