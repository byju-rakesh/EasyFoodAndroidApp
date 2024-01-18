package com.example.easyfood.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.easyfood.databinding.CategoryItemBinding
import com.example.easyfood.model.Category

class CategoriesAdapter():RecyclerView.Adapter<CategoriesAdapter.CategoriesViewModel>() {

    var onItemClick: ((Category)->Unit)?=null
    private var categories=ArrayList<Category>()

    fun setCategories(categories:ArrayList<Category>){
        this.categories=categories
        notifyDataSetChanged()
    }

    class CategoriesViewModel(val binding: CategoryItemBinding):ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewModel {
       return  CategoriesViewModel(CategoryItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
       return categories.size
    }

    override fun onBindViewHolder(holder: CategoriesViewModel, position: Int) {
       Glide.with(holder.itemView)
           .load(categories[position].strCategoryThumb)
           .into(holder.binding.imgCategory)

       holder.binding.tvCategoryHeading.text=categories[position].strCategory
        holder.itemView.setOnClickListener{
            onItemClick!!.invoke(categories[position])
        }
    }
}