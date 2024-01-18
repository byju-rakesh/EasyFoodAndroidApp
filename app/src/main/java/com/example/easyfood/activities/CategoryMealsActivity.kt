package com.example.easyfood.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.easyfood.R
import com.example.easyfood.adapters.CategoryMealsAdapter
import com.example.easyfood.databinding.ActivityCategoryMealsBinding
import com.example.easyfood.fragments.HomeFragment
import com.example.easyfood.model.MealsByCategory
import com.example.easyfood.viewModel.CategoryMealsViewModel

class CategoryMealsActivity : AppCompatActivity() {
    private lateinit var binding:ActivityCategoryMealsBinding
    private lateinit var categoryMealsViewModel:CategoryMealsViewModel
    private lateinit var categoryName:String
    private lateinit var categoryMealsAdapter: CategoryMealsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityCategoryMealsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        categoryMealsViewModel=ViewModelProviders.of(this)[CategoryMealsViewModel::class.java]
        categoryMealsAdapter= CategoryMealsAdapter()
        prepareCategoryMealsRecyclerView()
        val intent=intent
        categoryName=intent.getStringExtra(HomeFragment.CATEGORY_NAME)!!
        categoryMealsViewModel.getMealByCategory(categoryName)
        observeMealsLiveData()
    }

    private fun prepareCategoryMealsRecyclerView() {
      binding.recViewCategoryMeals.apply {
          layoutManager=GridLayoutManager(applicationContext,2,GridLayoutManager.VERTICAL,false)
          adapter=categoryMealsAdapter
      }
    }

    private fun observeMealsLiveData() {
        categoryMealsViewModel.observeMealsLiveData().observe(this
        ) {
            binding.tvCategoryCount.text="count : ${it.size}"
           categoryMealsAdapter.setCategoryMeals(it as ArrayList<MealsByCategory>)
        }
    }
}