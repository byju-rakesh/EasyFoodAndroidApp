package com.example.easyfood.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.easyfood.databinding.MealItemBinding
import com.example.easyfood.model.Meal

class MealsAdapter(): RecyclerView.Adapter<MealsAdapter.FavoritesMealViewHolder>() {
    private val diffUtil=object : DiffUtil.ItemCallback<Meal>() {
        override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return  oldItem.idMeal==newItem.idMeal
        }

        override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
           return oldItem==newItem
        }

    }
     val differ=AsyncListDiffer(this,diffUtil)

    class FavoritesMealViewHolder(val binding: MealItemBinding):ViewHolder(binding.root){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesMealViewHolder {
        return FavoritesMealViewHolder(MealItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: FavoritesMealViewHolder, position: Int) {

        val meal=differ.currentList[position]
       Glide.with(holder.itemView)
           .load(meal.strMealThumb)
           .into(holder.binding.imgCategoryMeals)
        holder.binding.tvCategoryMealName.text=meal.strMeal
    }
}