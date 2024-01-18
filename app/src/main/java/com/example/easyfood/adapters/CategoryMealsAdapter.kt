package com.example.easyfood.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.easyfood.databinding.MealItemBinding
import com.example.easyfood.model.MealsByCategory

class CategoryMealsAdapter : RecyclerView.Adapter<CategoryMealsAdapter.CategoryMealsViewHolder>() {

    private var categoryMealsList=ArrayList<MealsByCategory>()

    fun setCategoryMeals(categoryMealsList: ArrayList<MealsByCategory>){
        this.categoryMealsList=categoryMealsList
        notifyDataSetChanged()
    }

    class CategoryMealsViewHolder(val binding: MealItemBinding): ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryMealsViewHolder {
       return CategoryMealsViewHolder(MealItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
       return categoryMealsList.size
    }

    override fun onBindViewHolder(holder: CategoryMealsViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(categoryMealsList[position].strMealThumb)
            .into(holder.binding.imgCategoryMeals)
        holder.binding.tvCategoryMealName.text=categoryMealsList[position].strMeal
    }
}