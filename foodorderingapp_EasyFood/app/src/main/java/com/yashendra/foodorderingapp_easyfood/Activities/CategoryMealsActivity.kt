package com.yashendra.foodorderingapp_easyfood.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.yashendra.foodorderingapp_easyfood.R
import com.yashendra.foodorderingapp_easyfood.ViewModel.CategoryViewModel
import com.yashendra.foodorderingapp_easyfood.adapters.CategoryMealsAdapter
import com.yashendra.foodorderingapp_easyfood.fragments.HomeFragment
import kotlinx.android.synthetic.main.activity_category_meals.*

class CategoryMealsActivity : AppCompatActivity() {
    lateinit var categorymealsViewModel:CategoryViewModel
    private lateinit var categoryMealsAdapter: CategoryMealsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_meals)

        prpareRecyclerview()

        categorymealsViewModel=ViewModelProviders.of(this)[CategoryViewModel::class.java]
        categorymealsViewModel.getMealsByCategory(intent.getStringExtra(HomeFragment.CATEGORY_NAME)!!)
        categorymealsViewModel.observeMealsLiveData().observe(this,{
            mealslist->
                tv_category_count.text= "Count:${mealslist.size}"
              categoryMealsAdapter.setmealsList(mealslist)
        })

        onCategoryClick()

    }
    private fun onCategoryClick() {
        categoryMealsAdapter.onItemClick={
                Mealbycategory ->
            val intent=Intent(this,MealActivity::class.java)
            intent.putExtra("class","categorymeal")
            intent.putExtra("idMeal",Mealbycategory.idMeal)
            intent.putExtra("strMeal",Mealbycategory.strMeal)
            intent.putExtra("strMealThumb",Mealbycategory.strMealThumb)
            startActivity(intent)
        }
    }


    private fun prpareRecyclerview() {
       categoryMealsAdapter= CategoryMealsAdapter()
        rv_meals.apply {
            layoutManager=GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
            adapter=categoryMealsAdapter
        }
    }

}