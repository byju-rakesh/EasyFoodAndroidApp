package com.example.easyfood.activities

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
import com.example.easyfood.R
import com.example.easyfood.databinding.ActivityMealBinding
import com.example.easyfood.db.MealDatabase
import com.example.easyfood.fragments.HomeFragment
import com.example.easyfood.model.Meal
import com.example.easyfood.viewModel.MealViewModel
import com.example.easyfood.viewModel.MealViewModelFactory

class MealActivity : AppCompatActivity() {
    private lateinit var mealId:String
    private lateinit var mealName:String
    private lateinit var mealThumb:String
    private lateinit var youtubeLink:String
    private lateinit var binding: ActivityMealBinding
    private lateinit var mealViewModel:MealViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mealDatabase=MealDatabase.getInstance(this)
        val viewModelFactory=MealViewModelFactory(mealDatabase)
        mealViewModel=ViewModelProvider(this,viewModelFactory)[MealViewModel::class.java]

//        mealViewModel=ViewModelProviders.of(this)[MealViewModel::class.java]

        getMealInformationFromIntent()
        setInformationInViews()
        loadingCase()
        mealViewModel.getMealDetail(mealId)
        observeMealDetailsLiveData()
        onYoutubeImageClick()
        onFavoriteClick()
    }

    private fun onFavoriteClick() {
       binding.btnAddToFav.setOnClickListener{
           mealToSave?.let {
               mealViewModel.insertMeal(it)
               Toast.makeText(this,"Meal save",Toast.LENGTH_SHORT).show()
           }
       }
    }

    private fun onYoutubeImageClick() {
        binding.imgYoutube.setOnClickListener{
            val intent=Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink))
            startActivity(intent)
        }
    }

    private var mealToSave:Meal?=null

    private fun observeMealDetailsLiveData() {
       mealViewModel.observeMealDetailsLiveData().observe(this,object :Observer<Meal>{
           override fun onChanged(value: Meal) {
               responseCase()
               mealToSave=value
               binding.tvCategoryName.text="Category : ${value.strCategory}"
               binding.tvArea.text="Area : ${value.strArea}"
               binding.tvInstructionsSteps.text=value.strInstructions
               youtubeLink=value.strYoutube!!
           }

       })
    }

    private fun setInformationInViews() {
        Glide.with(applicationContext)
            .load(mealThumb)
            .into(binding.imgMealDetail)

        binding.collapsingToolbar.title=mealName
        binding.collapsingToolbar.setCollapsedTitleTextColor(resources.getColor(R.color.white))
        binding.collapsingToolbar.setExpandedTitleColor(resources.getColor(R.color.white))
    }

    private fun getMealInformationFromIntent() {
       val intent=intent
        mealId=intent.getStringExtra(HomeFragment.MEAL_ID)!!
        mealName=intent.getStringExtra(HomeFragment.MEAL_NAME)!!
        mealThumb=intent.getStringExtra(HomeFragment.MEAL_THUMB)!!

    }

    private  fun loadingCase(){
        binding.progressBar.visibility= View.VISIBLE
        binding.btnAddToFav.visibility= View.INVISIBLE
        binding.tvInstructions.visibility= View.INVISIBLE
        binding.tvCategoryName.visibility= View.INVISIBLE
        binding.tvCategoryName.visibility= View.INVISIBLE
        binding.imgYoutube.visibility= View.INVISIBLE

    }
    private  fun responseCase(){
        binding.progressBar.visibility= View.INVISIBLE
        binding.btnAddToFav.visibility= View.VISIBLE
        binding.tvInstructions.visibility= View.VISIBLE
        binding.tvCategoryName.visibility= View.VISIBLE
        binding.tvCategoryName.visibility= View.VISIBLE
        binding.imgYoutube.visibility= View.VISIBLE
    }
}