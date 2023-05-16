package com.yashendra.foodorderingapp_easyfood.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.yashendra.foodorderingapp_easyfood.Activities.CategoryMealsActivity
import com.yashendra.foodorderingapp_easyfood.Activities.MainActivity
import com.yashendra.foodorderingapp_easyfood.R
import com.yashendra.foodorderingapp_easyfood.ViewModel.HomeViewModel
import com.yashendra.foodorderingapp_easyfood.adapters.CategoriesAdapter
import com.yashendra.foodorderingapp_easyfood.adapters.CategoryMealsAdapter
import kotlinx.android.synthetic.main.fragment_categories.*


class categoriesFragment : Fragment() {
    private lateinit var categoriesAdapter: CategoriesAdapter
    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel=(activity as MainActivity).viewModel

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_categories, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareRecyclerview()
        observeCategories()
        onCategoryClick()
    }
    private fun onCategoryClick() {
        categoriesAdapter.onItemClick={
                category ->
            val intent= Intent(activity, CategoryMealsActivity::class.java)
            intent.putExtra(HomeFragment.CATEGORY_NAME,category.strCategory)
            startActivity(intent)
        }
    }

    private fun observeCategories() {
        viewModel.observeCategoriesLivedata().observe(viewLifecycleOwner, Observer {
            categories->
            categoriesAdapter.setCategoryList(categories)
        })
    }

    private fun prepareRecyclerview() {
        categoriesAdapter= CategoriesAdapter()
        rv_categories.apply {
            layoutManager=GridLayoutManager(context,3,GridLayoutManager.VERTICAL,false)
            adapter=categoriesAdapter
        }

    }

}