package com.yashendra.foodorderingapp_easyfood.Activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DatabaseReference
import com.yashendra.foodorderingapp_easyfood.R
import com.yashendra.foodorderingapp_easyfood.ViewModel.MealViewModel
import com.yashendra.foodorderingapp_easyfood.ViewModel.MealViewModelFactory
import com.yashendra.foodorderingapp_easyfood.dataclasses.Meal
import com.yashendra.foodorderingapp_easyfood.db.Mealdb
import com.yashendra.foodorderingapp_easyfood.db.fbdao
import com.yashendra.foodorderingapp_easyfood.fragments.HomeFragment
import kotlinx.android.synthetic.main.activity_meal.*
import kotlin.random.Random

class MealActivity : AppCompatActivity() {
    private lateinit var mealId:String
    private lateinit var mealName:String
    private lateinit var mealThumb:String
    private lateinit var mealMvvm:MealViewModel
    private lateinit var youtubelink:String
    private lateinit var fbdao: fbdao
    val randomNumber = generateRandomNumber()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meal)

//        mealMvvm=ViewModelProviders.of(this)[MealViewModel::class.java]

        val mealdatabase=Mealdb.getInstance(this)
        val viewModelFactory=MealViewModelFactory(mealdatabase)
        mealMvvm=ViewModelProvider(this,viewModelFactory)[MealViewModel::class.java]


        getMealInformationFromIntent()
        mealMvvm.getMealdetail(mealId)
        setInformationInViews()
        loadingcase()

        ObserverMealDetailsLiveData()
        onYoutubeImageClick()
        onfavouriteClick()
        cartclick()


    }
    fun generateRandomNumber(): String {
        return Random.nextInt(100, 1200).toString()
    }

    private fun cartclick() {
        fbdao= fbdao()
        img_cart.setOnClickListener {
//            val intent=Intent(this,CartActivity::class.java)
//            intent.putExtra("mealId",mealId)
//            intent.putExtra("mealName",mealName)
//            intent.putExtra("mealThumb",mealThumb)


            fbdao.addMeal(mealId,mealName,mealThumb,randomNumber)
            Toast.makeText(this, "Item Added to Cart", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun onfavouriteClick() {
        btnaddtofav.setOnClickListener {
            mealtosave?.let {
                mealMvvm.insertmeal(it)
                Toast.makeText(this, "Meal Saved", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onYoutubeImageClick() {
        img_youtube.setOnClickListener{
            val intent=Intent(Intent.ACTION_VIEW,Uri.parse(youtubelink))
            startActivity(intent)
        }
    }
    private var mealtosave:Meal?=null

    private fun ObserverMealDetailsLiveData() {
        mealMvvm.observerMealDetailsLiveData().observe(this,object:Observer<Meal>{
            override fun onChanged(t: Meal?) {
                onResponseCase()
                val meal=t
                mealtosave=meal
                tvcategory.text="Category : ${meal!!.strCategory}"
                tvarea.text="Area : ${meal!!.strArea}"
                instructionsteps.text=meal.strInstructions
                youtubelink= meal.strYoutube.toString()
            }

        })
    }

    private fun setInformationInViews() {
        Glide.with(applicationContext).load(mealThumb).into(img_meal_detail)
        collapsing_toolbar.title=mealName
        collapsing_toolbar.setCollapsedTitleTextColor(resources.getColor(R.color.white))
        collapsing_toolbar.setExpandedTitleColor(resources.getColor(R.color.white))
        tv_price.append(randomNumber)



    }

    private fun getMealInformationFromIntent() {

        val intent=intent
        if (intent.getStringExtra("class").equals("categorymeal"))
        {
            mealId=intent.getStringExtra("idMeal")!!
            mealName=intent.getStringExtra("strMeal")!!
            mealThumb=intent.getStringExtra("strMealThumb")!!
        }
        else
        {
            mealId = intent.getStringExtra(HomeFragment.Meal_id)!!
            mealName = intent.getStringExtra(HomeFragment.Meal_name)!!
            mealThumb = intent.getStringExtra(HomeFragment.Meal_thumb)!!
        }
    }

    private fun loadingcase(){
        btnaddtofav.visibility= View.INVISIBLE
        progressbar.visibility= View.VISIBLE
        tv_instructions.visibility= View.INVISIBLE
        tvcategory.visibility= View.INVISIBLE
        tvarea.visibility= View.INVISIBLE
        img_youtube.visibility= View.INVISIBLE
    }
    private  fun onResponseCase(){
        btnaddtofav.visibility= View.VISIBLE
        progressbar.visibility= View.INVISIBLE
        tv_instructions.visibility= View.VISIBLE
        tvcategory.visibility= View.VISIBLE
        tvarea.visibility= View.VISIBLE
        img_youtube.visibility= View.VISIBLE
    }
}