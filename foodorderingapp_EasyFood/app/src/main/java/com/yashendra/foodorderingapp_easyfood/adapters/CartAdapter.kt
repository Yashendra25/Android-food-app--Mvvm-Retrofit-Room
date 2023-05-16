package com.yashendra.foodorderingapp_easyfood.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.yashendra.foodorderingapp_easyfood.Interface.cartAdapter
import com.yashendra.foodorderingapp_easyfood.R
import com.yashendra.foodorderingapp_easyfood.dataclasses.MealWithPrice

class CartAdapter(options: FirestoreRecyclerOptions<MealWithPrice>,val listener: cartAdapter) : FirestoreRecyclerAdapter<MealWithPrice, CartAdapter.CartviewHolder>(
    options
) {
     var total = 0

    class CartviewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val mealname:TextView=itemView.findViewById(R.id.txtMealName)
        val imgMeal:ImageView=itemView.findViewById(R.id.imgMealThumb)
        val txtPrice:TextView=itemView.findViewById(R.id.txtPrice)
        val btndelete:ImageView=itemView.findViewById(R.id.btnDelete)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartviewHolder {
        val viewHolder= CartviewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cart_item,parent,false))
        viewHolder.btndelete.setOnClickListener{
            listener.ondeleteClicked(snapshots.getSnapshot(viewHolder.adapterPosition).id)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: CartviewHolder, position: Int, model: MealWithPrice) {
        holder.txtPrice.text=model.price
        holder.mealname.text=model.strMeal
        Glide.with(holder.imgMeal.context).load(model.strMealThumb).centerCrop().into(holder.imgMeal)

        // Update the total price in the activity or fragment
        val totalPrice = snapshots.map { it.price!!.toInt() }.sum()
        listener.onTotalPriceUpdated(totalPrice)


    }

    override fun onDataChanged() {
        super.onDataChanged()

        // Reset total when data changes
        total = 0

        // Iterate over all items and update total
        for (i in 0 until itemCount) {
            val model = getItem(i)
            total += model.price!!.toInt()
        }

        // Notify listener of total price update
        listener.onTotalPriceUpdated(total)
    }

    }

