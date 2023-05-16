package com.yashendra.foodorderingapp_easyfood.fragments


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DatabaseReference
import com.yashendra.foodorderingapp_easyfood.Activities.CategoryMealsActivity
import com.yashendra.foodorderingapp_easyfood.Activities.LoginActivity
import com.yashendra.foodorderingapp_easyfood.Activities.MainActivity
import com.yashendra.foodorderingapp_easyfood.Activities.MealActivity
import com.yashendra.foodorderingapp_easyfood.R
import com.yashendra.foodorderingapp_easyfood.ViewModel.HeaderViewModel
import com.yashendra.foodorderingapp_easyfood.ViewModel.HeaderViwModelFactory
import com.yashendra.foodorderingapp_easyfood.ViewModel.HomeViewModel
import com.yashendra.foodorderingapp_easyfood.adapters.CategoriesAdapter
import com.yashendra.foodorderingapp_easyfood.adapters.MostPopularAdapter
import com.yashendra.foodorderingapp_easyfood.databinding.FragmentHomeBinding
import com.yashendra.foodorderingapp_easyfood.dataclasses.MealsByCategory
import com.yashendra.foodorderingapp_easyfood.dataclasses.Meal
import com.yashendra.foodorderingapp_easyfood.fragments.Bottomsheet.MealBottomSheetFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.nav_header_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import kotlin.math.log

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var dbreference: DatabaseReference
    lateinit var drawerLayout: DrawerLayout
    private lateinit var viewModel: HomeViewModel
    private lateinit var randomMeal:Meal
    private lateinit var popularItemsAdapter: MostPopularAdapter
    private lateinit var categoriesAdapter: CategoriesAdapter
    private lateinit var headerviewModel: HeaderViewModel


    companion object{

        const val Meal_id="com.yashendra.foodorderingapp_easyfood.fragments.idMeal"
        const val Meal_name="com.yashendra.foodorderingapp_easyfood.fragments.nameMeal"
        const val Meal_thumb="com.yashendra.foodorderingapp_easyfood.fragments.thumbMeal"
        const val CATEGORY_NAME="com.yashendra.foodorderingapp_easyfood.fragments.categoryName"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel=(activity as MainActivity).viewModel

//        viewModel=androidx.lifecycle.ViewModelProviders.of(this)[HomeViewModel::class.java]
        headerviewModel = ViewModelProvider(this, HeaderViwModelFactory()).get(HeaderViewModel::class.java)
        popularItemsAdapter= MostPopularAdapter()
//        ViewModelProvider(this).get(HeaderViewModel::class.java)
//        headerViewModel=androidx.lifecycle.ViewModelProviders.of(this)[HeaderViewModel::class.java]
        

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        drawerLayout = drawerlayout

        menu_button.setOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }
        navigation_view.setNavigationItemSelectedListener {
            menuitem->
            if(menuitem.itemId==R.id.signout)
            {
                val sharedPrefs = requireContext().getSharedPreferences("MyApp", Context.MODE_PRIVATE)
                sharedPrefs.edit().clear().apply()

                val intent= Intent(context,LoginActivity::class.java)
                startActivity(intent)
                requireActivity().finish()

            }

            true
        }

        preparepopularitemsRecyclerView()

        viewModel.getRandomMeal()
        observerRandomMeal()
        onRandomMealClick()

        viewModel.getPopularItems()
        observerPopularItemsLivedata()
        onPopularItemClick()

        prepareCategoriesRecyclerView()
        viewModel.getCategories()
        observeCategoriesLiveData()

        onCategoryClick()
        onPopularItemLongClick()

        onSearchItmeClick()


//        observeNavheaderLivedata()

        settingvalues_using_companion()



    }

    private fun settingvalues_using_companion() {
        val textandemail=binding.navigationView
        val tv_email=textandemail.findViewById<TextView>(R.id.textViewEmail)
        tv_email.text=LoginActivity.UserSession.Uemail
        val tv_name=textandemail.findViewById<TextView>(R.id.textViewName)
        tv_name.text=LoginActivity.UserSession.Uname
//        header.textViewEmail.text=LoginActivity.UserSession.Uemail
    }

//    private fun observeNavheaderLivedata() {
//        Log.d("controlchek","a gya")
//        headerviewModel.headeremail.observe(viewLifecycleOwner) {
//            Log.d("headername", it.toString())
//            val emailTextView = textViewEmail
//            emailTextView.text = it
//
//
//
//        }
//
//        headerviewModel.headername.observe(viewLifecycleOwner) {
//            val nameTextView = textViewName
//            nameTextView.text = it
//            Log.d("headermail", it)
//        }
//    }




    private fun onSearchItmeClick() {
        img_search.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }
    }

    private fun onPopularItemLongClick() {
        popularItemsAdapter.onLongItemclick={
            val mealBottomSheetFragment=MealBottomSheetFragment.newInstance(it.idMeal)
            mealBottomSheetFragment.show(childFragmentManager,"Meal info")
        }
    }

    private fun onCategoryClick() {
        categoriesAdapter.onItemClick={
            category ->
            val intent=Intent(activity,CategoryMealsActivity::class.java)
            intent.putExtra(CATEGORY_NAME,category.strCategory)
            startActivity(intent)
        }
    }

    private fun prepareCategoriesRecyclerView() {
        categoriesAdapter=CategoriesAdapter()
        rec_view_categories.apply {
            layoutManager=GridLayoutManager(context,3,GridLayoutManager.VERTICAL,false)

            adapter=categoriesAdapter
        }
    }

    private fun observeCategoriesLiveData() {
        viewModel.observeCategoriesLivedata().observe(viewLifecycleOwner,{
            categories->
           categoriesAdapter.setCategoryList(categories)
        })
    }

    private fun onPopularItemClick() {
        popularItemsAdapter.onitemClick={
            meal->
            val intent=Intent(activity,MealActivity::class.java)
            intent.putExtra(Meal_id,meal.idMeal)
            intent.putExtra(Meal_name,meal.strMeal)
            intent.putExtra(Meal_thumb,meal.strMealThumb)
            startActivity(intent)

        }
    }

    private fun preparepopularitemsRecyclerView() {
        rec_view_meals_popular.apply {
            layoutManager=LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
            adapter=popularItemsAdapter
        }
    }

    private fun observerPopularItemsLivedata() {
        viewModel.observePopularItemsLivedata().observe(viewLifecycleOwner
        ) { meallist->
                popularItemsAdapter.setMeals(mealslist = meallist as ArrayList<MealsByCategory>)
        }
    }

    private fun onRandomMealClick() {
        random_mealcard.setOnClickListener {
            val intent=Intent(activity,MealActivity::class.java)
            intent.putExtra(Meal_id,randomMeal.idMeal)
            intent.putExtra(Meal_name,randomMeal.strMeal)
            intent.putExtra(Meal_thumb,randomMeal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun observerRandomMeal() {
        viewModel.observeRandomMealLivedata().observe(viewLifecycleOwner,object :Observer<Meal>{
            override fun onChanged(value: Meal) {
                Glide.with(this@HomeFragment).load(value.strMealThumb).into(img_random_meal)
                randomMeal=value
            }
        })
    }


}
