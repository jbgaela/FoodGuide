package com.example.foodguide.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.foodguide.R
import com.example.foodguide.databinding.ActivityMealBinding
import com.example.foodguide.fragments.HomeFragment
import com.example.foodguide.pojo.Meal
import com.example.foodguide.viewModel.MealViewModel

class MealActivity : AppCompatActivity() {
    private lateinit var mealId: String
    private lateinit var mealName: String
    private lateinit var mealThumb: String
    private lateinit var youtubeLink: String
    private lateinit var binding: ActivityMealBinding
    private lateinit var mealMvvm: MealViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mealMvvm = ViewModelProviders.of(this)[MealViewModel::class.java]

        getMealInformationFromIntent()

        setInformationInViews()
        loadingCase()
        mealMvvm.getMealDetail(mealId)
        observeMealDetailsLiveData()
        onYoutubeImageClick()
    }

    private fun onYoutubeImageClick() {
        binding.imageYoutube.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink))
            startActivity(intent)
        }
    }

    private fun observeMealDetailsLiveData() {
        mealMvvm.observerMealDetailsLiveData().observe(this, object :Observer<Meal>{
            override fun onChanged(value: Meal) {
                onResponseCase()
                val meal = value
                binding.tvCategory.text = "Category: ${meal!!.strCategory}"
                binding.tvArea.text = "Origin: ${meal!!.strArea}"
                binding.tvInstructionsSteps.text = meal.strInstructions

                youtubeLink = meal.strYoutube
            }

        })
    }

    private fun setInformationInViews() {
        Glide.with(applicationContext)
            .load(mealThumb)
            .into(binding.imgMealDetail)

        binding.collapsingToolbar.title = mealName
        binding.collapsingToolbar.setCollapsedTitleTextColor(resources.getColor(R.color.white))
        binding.collapsingToolbar.setExpandedTitleColor(resources.getColor(R.color.white))
    }

    private fun getMealInformationFromIntent() {
        val intent = intent
        mealId = intent.getStringExtra(HomeFragment.MEAL_ID)!!
        mealName = intent.getStringExtra(HomeFragment.MEAL_NAME)!!
        mealThumb = intent.getStringExtra(HomeFragment.MEAL_THUMB)!!
    }

    private fun loadingCase() {
        binding.progressBar.visibility = View.VISIBLE
        binding.btnAddToFav.visibility = View.INVISIBLE
        binding.tvInstructions.visibility = View.INVISIBLE
        binding.tvCategory.visibility = View.INVISIBLE
        binding.tvArea.visibility = View.INVISIBLE
        binding.imageYoutube.visibility = View.INVISIBLE
    }

    private fun onResponseCase(){
        binding.progressBar.visibility = View.INVISIBLE
        binding.btnAddToFav.visibility = View.VISIBLE
        binding.tvInstructions.visibility = View.VISIBLE
        binding.tvCategory.visibility = View.VISIBLE
        binding.tvArea.visibility = View.VISIBLE
        binding.imageYoutube.visibility = View.VISIBLE
    }
}