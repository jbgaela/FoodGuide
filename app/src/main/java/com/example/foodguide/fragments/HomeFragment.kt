package com.example.foodguide.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.foodguide.activities.MealActivity
import com.example.foodguide.databinding.FragmentHomeBinding
import com.example.foodguide.pojo.Meal
import com.example.foodguide.viewModel.HomeViewModel

class HomeFragment : Fragment() {
    private lateinit var binding:FragmentHomeBinding
    private lateinit var homeMvvm:HomeViewModel
    private lateinit var randomMeal:Meal

    companion object {
        const val MEAL_ID = "package com.example.foodguide.fragments.idMeal"
        const val MEAL_NAME = "package com.example.foodguide.fragments.nameMeal"
        const val MEAL_THUMB = "package com.example.foodguide.fragments.thumbMeal"

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeMvvm = ViewModelProvider(this)[HomeViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeMvvm.getRandomMeal()
        observerRandomMeal()
        onRandomMealClick()

    }

    private fun onRandomMealClick(){
        binding.randomMealCard.setOnClickListener{
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, randomMeal.idMeal)
            intent.putExtra(MEAL_NAME, randomMeal.strMeal)
            intent.putExtra(MEAL_THUMB, randomMeal.strMealThumb)
            startActivity(intent)

        }
    }

    private fun observerRandomMeal() {
        homeMvvm.observeRandomMealLiveData().observe(viewLifecycleOwner
        ){
            Glide.with(this@HomeFragment)
                .load(it!!.strMealThumb)
                .into(binding.imgRandomMeal)

            this.randomMeal = it
        }

    }

}

