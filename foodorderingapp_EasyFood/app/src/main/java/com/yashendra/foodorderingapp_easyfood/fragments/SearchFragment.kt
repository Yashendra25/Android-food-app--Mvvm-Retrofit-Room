package com.yashendra.foodorderingapp_easyfood.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.yashendra.foodorderingapp_easyfood.Activities.MainActivity
import com.yashendra.foodorderingapp_easyfood.Activities.MealActivity
import com.yashendra.foodorderingapp_easyfood.R
import com.yashendra.foodorderingapp_easyfood.ViewModel.HomeViewModel
import com.yashendra.foodorderingapp_easyfood.adapters.FavouriteMealsAdapter
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var searchRecylerviewAdapter: FavouriteMealsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel=(activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecyclerview()
        seracheditemclick()

        img_search_arrow.setOnClickListener {
            searchmeals()
        }
        observeSerachmealsLivedata()

        var searchjob:Job?=null
        search_box.addTextChangedListener {  searchquery->
            searchjob?.cancel()
            searchjob=lifecycleScope.launch{
                delay(500)
                viewModel.serachMeal(searchquery.toString())
            }
        }
    }

    private fun seracheditemclick() {
        searchRecylerviewAdapter.onfavclick={
                meal->
            val intent= Intent(activity, MealActivity::class.java)
            intent.putExtra(HomeFragment.Meal_id,meal.idMeal)
            intent.putExtra(HomeFragment.Meal_name,meal.strMeal)
            intent.putExtra(HomeFragment.Meal_thumb,meal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun observeSerachmealsLivedata() {
        viewModel.observeSerachmealsLivedata().observe(viewLifecycleOwner, Observer {
            mealslist->
            searchRecylerviewAdapter.differ.submitList(mealslist)
        })
    }

    private fun searchmeals() {
        val searchquery=search_box.text.toString()
        if (searchquery.isNotEmpty()){
            viewModel.serachMeal(searchquery)
        }
    }

    private fun prepareRecyclerview() {
        searchRecylerviewAdapter= FavouriteMealsAdapter()
        rv_search.apply {
            layoutManager= GridLayoutManager(context,2, GridLayoutManager.VERTICAL,false)
            adapter=searchRecylerviewAdapter

        }
    }


}