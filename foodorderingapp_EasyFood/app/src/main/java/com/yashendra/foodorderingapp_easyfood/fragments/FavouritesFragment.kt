package com.yashendra.foodorderingapp_easyfood.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.yashendra.foodorderingapp_easyfood.Activities.MainActivity
import com.yashendra.foodorderingapp_easyfood.Activities.MealActivity
import com.yashendra.foodorderingapp_easyfood.R
import com.yashendra.foodorderingapp_easyfood.ViewModel.HomeViewModel
import com.yashendra.foodorderingapp_easyfood.adapters.FavouriteMealsAdapter
import kotlinx.android.synthetic.main.fragment_favourites.*


class FavouritesFragment : Fragment() {
    private lateinit var viewModel: HomeViewModel
    private lateinit var favouriteMealsAdapter: FavouriteMealsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel=(activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareRecyclerView()
        observeFavourites()
        favouritesItemclick()

        val itemTouchHelper= object : ItemTouchHelper.SimpleCallback (ItemTouchHelper.UP or ItemTouchHelper.DOWN,
        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) =true

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position=viewHolder.adapterPosition
                viewModel.deleteMeal(favouriteMealsAdapter.differ.currentList[position])
                Snackbar.make(requireView(),"Meal Deleted",Snackbar.LENGTH_LONG).setAction(
                    "Undo",View.OnClickListener {
                        viewModel.insertmeal(favouriteMealsAdapter.differ.currentList[position])
                    }
                ).show()

            }

        }
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(rv_favourites)
    }

    private fun favouritesItemclick() {
        favouriteMealsAdapter.onfavclick={
                meal->
            val intent= Intent(activity, MealActivity::class.java)
            intent.putExtra(HomeFragment.Meal_id,meal.idMeal)
            intent.putExtra(HomeFragment.Meal_name,meal.strMeal)
            intent.putExtra(HomeFragment.Meal_thumb,meal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun prepareRecyclerView() {
        favouriteMealsAdapter= FavouriteMealsAdapter()
        rv_favourites.apply {
            layoutManager=GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
            adapter=favouriteMealsAdapter
        }
    }

    private fun observeFavourites() {
        viewModel.observeFavouritesMealslivedata().observe(requireActivity(), Observer {
            meals->
            favouriteMealsAdapter.differ.submitList(meals)
        })
    }
}