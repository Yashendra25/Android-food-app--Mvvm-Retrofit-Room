package com.yashendra.foodorderingapp_easyfood.fragments.Bottomsheet

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yashendra.foodorderingapp_easyfood.Activities.MainActivity
import com.yashendra.foodorderingapp_easyfood.Activities.MealActivity
import com.yashendra.foodorderingapp_easyfood.R
import com.yashendra.foodorderingapp_easyfood.ViewModel.HomeViewModel
import com.yashendra.foodorderingapp_easyfood.fragments.HomeFragment
import kotlinx.android.synthetic.main.fragment_meal_bottom_sheet.*

private const val Meal_id = "meal_id"

class MealBottomSheetFragment : BottomSheetDialogFragment() {

    private var meal_id: String? = null
    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            meal_id = it.getString(Meal_id)

        }
        viewModel=(activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_meal_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        meal_id?.let {
            viewModel.getMealbyid(it)
        }
        observebottomSheetMeal()
        onBottomSheetDiaogClick()

    }

    private fun onBottomSheetDiaogClick() {
        mealbottomsheet.setOnClickListener {
                if(mealname!=null && mealThumb!=null){
                    val intent=Intent(activity,MealActivity::class.java)
                    intent.apply {
                        intent.putExtra(HomeFragment.Meal_id,meal_id)
                        intent.putExtra(HomeFragment.Meal_name,mealname)
                        intent.putExtra(HomeFragment.Meal_thumb,mealThumb)

                    }
                    startActivity(intent)
                }
        }
    }
    private var mealname:String?=null
    private var mealThumb:String?=null


    private fun observebottomSheetMeal() {
        viewModel.observeBottomSheetLivedata().observe(viewLifecycleOwner, Observer {
            meal->
            Glide.with(this).load(meal.strMealThumb).into(img_bottomsheet)
            img_bottomsheet_area.text=meal.strArea
            img_bottomsheet_category.text=meal.strCategory
            btmsheet_meal_Name.text=meal.strMeal

            mealname=meal.strMeal
            mealThumb=meal.strMealThumb
        })
    }

    companion object {
        
        @JvmStatic
        fun newInstance(param1: String) =
            MealBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putString(Meal_id, param1)

                }
            }
    }
}