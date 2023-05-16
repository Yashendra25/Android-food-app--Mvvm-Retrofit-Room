package com.yashendra.foodorderingapp_easyfood.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.yashendra.foodorderingapp_easyfood.R
import com.yashendra.foodorderingapp_easyfood.ViewModel.HomeViewModel
import com.yashendra.foodorderingapp_easyfood.ViewModel.HomelViewModelFactory
import com.yashendra.foodorderingapp_easyfood.db.Mealdb
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(){

    val viewModel: HomeViewModel by lazy {
        val mealdatabase=Mealdb.getInstance(this)
        val homelViewModelFactory=HomelViewModelFactory(mealdatabase)
        ViewModelProvider(this,homelViewModelFactory)[HomeViewModel::class.java]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //for switching frgaments between with hostfragment
        val navController=Navigation.findNavController(this, R.id.hostFragment)
        val bottomnavigation=findViewById<BottomNavigationView>(R.id.btm_nav)
        NavigationUI.setupWithNavController(bottomnavigation,navController)
    }








}