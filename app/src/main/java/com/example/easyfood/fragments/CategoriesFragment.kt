package com.example.easyfood.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.easyfood.activities.CategoryMealsActivity
import com.example.easyfood.activities.MainActivity
import com.example.easyfood.adapters.CategoriesAdapter
import com.example.easyfood.databinding.FragmentCategoriesBinding
import com.example.easyfood.model.Category
import com.example.easyfood.viewModel.HomeViewModel


class CategoriesFragment : Fragment() {
    private lateinit var binding: FragmentCategoriesBinding
    private lateinit var viewModel: HomeViewModel
    lateinit var categoriesAdapter: CategoriesAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel=(activity as MainActivity).homeViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding=FragmentCategoriesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareCategoryRecyclerView()
        observeCategories()
        onCategoryClick()
    }

    private fun onCategoryClick() {
        categoriesAdapter.onItemClick={ category ->
            val intent= Intent(activity, CategoryMealsActivity::class.java)
            intent.putExtra(HomeFragment.CATEGORY_NAME,category.strCategory)
            startActivity(intent)
        }
    }

    private fun observeCategories() {
        viewModel.observeCategoriesLiveData().observe(viewLifecycleOwner) {
            categoriesAdapter.setCategories(it as ArrayList<Category>)
        }
    }

    private fun prepareCategoryRecyclerView() {
        categoriesAdapter= CategoriesAdapter()
        binding.recViewCategory.apply {
            layoutManager=GridLayoutManager(context,3,GridLayoutManager.VERTICAL,false)
            adapter=categoriesAdapter
        }
    }

}