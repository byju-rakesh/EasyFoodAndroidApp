package com.example.easyfood.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.easyfood.R
import com.example.easyfood.activities.CategoryMealsActivity
import com.example.easyfood.activities.MainActivity
import com.example.easyfood.activities.MealActivity
import com.example.easyfood.adapters.CategoriesAdapter
import com.example.easyfood.adapters.MostPopularItemsAdapter
import com.example.easyfood.databinding.FragmentHomeBinding
import com.example.easyfood.fragments.bottomsheet.MealBottomSheetFragment
import com.example.easyfood.model.Category
import com.example.easyfood.model.MealsByCategory
import com.example.easyfood.model.Meal
import com.example.easyfood.viewModel.HomeViewModel



class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var randomMeal:Meal
    private lateinit var popularItemsAdapter: MostPopularItemsAdapter
    private lateinit var categoriesAdapter: CategoriesAdapter

    companion object{
        const val MEAL_ID="com.example.easyfood.fragments.idMeal"
        const val MEAL_NAME="com.example.easyfood.fragments.nameMeal"
        const val MEAL_THUMB="com.example.easyfood.fragments.thumbMeal"
        const val CATEGORY_NAME="com.example.easyfood.fragments.categoryName"

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//       homeViewModel=ViewModelProviders.of(this)[HomeViewModel::class.java]
        homeViewModel=(activity as MainActivity).homeViewModel
        popularItemsAdapter= MostPopularItemsAdapter()
        categoriesAdapter= CategoriesAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preparePopularItemsRecyclerView()

       homeViewModel.getRandomMeal()
        observerRandomMeal()
        onRandomMealClick()
        homeViewModel.getPopularItems()
        observePopularItemsLiveData()
        onPopularItemClick()
        prepareCategoriesRecyclerView()
        homeViewModel.getCategories()
        observeCategoriesLiveData()
        onCategoryClick()
        onPopularItemLongClick()
        onSearchIconClick()
    }

    private fun onSearchIconClick() {
        binding.imgSearch.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }
    }

    private fun onPopularItemLongClick() {
        popularItemsAdapter.onLongItemClick={
            val mealBottomSheetFragment=MealBottomSheetFragment.newInstance(it.idMeal)
            mealBottomSheetFragment.show(childFragmentManager,"Meal Info")
        }
    }

    private fun onCategoryClick() {
       categoriesAdapter.onItemClick={ category ->
           val intent=Intent(activity,CategoryMealsActivity::class.java)
           intent.putExtra(CATEGORY_NAME,category.strCategory)
           startActivity(intent)
       }
    }

    private fun prepareCategoriesRecyclerView() {
        binding.recViewCategories.apply {
            layoutManager=GridLayoutManager(activity,3,GridLayoutManager.VERTICAL,false)
            adapter=categoriesAdapter
        }
    }

    private fun observeCategoriesLiveData() {
       homeViewModel.observeCategoriesLiveData().observe(viewLifecycleOwner) { categories ->

               categoriesAdapter.setCategories(categories as ArrayList<Category> )

       }
    }

    private fun onPopularItemClick() {
       popularItemsAdapter.onItemClick={ meal->
           val intent=Intent(activity,MealActivity::class.java)
           intent.putExtra(MEAL_ID,meal.idMeal)
           intent.putExtra(MEAL_NAME,meal.strMeal)
           intent.putExtra(MEAL_THUMB,meal.strMealThumb)
           startActivity(intent)
       }
    }

    private fun preparePopularItemsRecyclerView() {
       binding.recViewMealsPopular.apply {
           layoutManager=LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
           adapter=popularItemsAdapter
       }
    }

    private fun observePopularItemsLiveData() {
      homeViewModel.observePopularItemsLiveData().observe(viewLifecycleOwner
      ) {mealList->
          popularItemsAdapter.setMeals(mealList as ArrayList<MealsByCategory>)

      }
    }

    private fun onRandomMealClick() {
        binding.randomMealCard.setOnClickListener{
            val intent=Intent(activity,MealActivity::class.java)
            intent.putExtra(MEAL_ID,randomMeal.idMeal)
            intent.putExtra(MEAL_NAME,randomMeal.strMeal)
            intent.putExtra(MEAL_THUMB,randomMeal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun observerRandomMeal() {
        homeViewModel.observeRandomMealLiveData().observe(viewLifecycleOwner
        ) { value ->
            Glide.with(this@HomeFragment)
                .load(value.strMealThumb)
                .into(binding.imgRandomMeal)

            this.randomMeal=value
        }
    }


}