package com.example.easyfood.fragments.bottomsheet

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.easyfood.activities.MainActivity
import com.example.easyfood.activities.MealActivity

import com.example.easyfood.databinding.FragmentMealBottomSheetBinding
import com.example.easyfood.fragments.HomeFragment
import com.example.easyfood.model.Meal
import com.example.easyfood.viewModel.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

private const val MEAL_ID="param1"

class MealBottomSheetFragment : BottomSheetDialogFragment(){
    private lateinit var binding: FragmentMealBottomSheetBinding
    private lateinit var viewModel:HomeViewModel

    private var mealId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mealId=it.getString(MEAL_ID)
        }
        viewModel=(activity as MainActivity).homeViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding= FragmentMealBottomSheetBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mealId?.let { viewModel.getMealById(it) }
        observeBottomSheetMealLiveData()
        onBottomSheetDialogClick()
    }

    private fun onBottomSheetDialogClick() {
       binding.bottomSheet.setOnClickListener{
           if(mealName!=null && mealThumb!=null){
               val intent=Intent(activity,MealActivity::class.java)
               intent.apply {
                   putExtra(HomeFragment.MEAL_ID,mealId)
                   putExtra(HomeFragment.MEAL_NAME,mealName)
                   putExtra(HomeFragment.MEAL_THUMB,mealThumb)
               }
               startActivity(intent)
           }
       }
    }

    private  var mealName:String?=null
    private  var mealThumb:String?=null
    private fun observeBottomSheetMealLiveData() {
        viewModel.observeBottomSheetMealLiveData().observe(viewLifecycleOwner
        ) {
            Glide.with(this)
                .load(it.strMealThumb)
                .into(binding.imgBottomSheet)

            binding.tvBottomSheetArea.text=it.strArea
            binding.tvBottomSheetCategory.text=it.strCategory
            binding.tvBottomSheetMealName.text=it.strMeal

            mealName=it.strMeal
            mealThumb=it.strMealThumb
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String) =
            MealBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putString(MEAL_ID, param1)

                }
            }
    }
}