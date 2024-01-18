package com.example.easyfood.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.easyfood.data.RetrofitInstance
import com.example.easyfood.model.MealsByCategory
import com.example.easyfood.model.MealsByCategoryList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryMealsViewModel : ViewModel() {
    private  var mealsLiveData=MutableLiveData<List<MealsByCategory>>()

    fun getMealByCategory(categoryName:String){
        RetrofitInstance.api.getMealsByCategory(categoryName).enqueue(object : Callback<MealsByCategoryList>{
            override fun onResponse(
                call: Call<MealsByCategoryList>,
                response: Response<MealsByCategoryList>
            ) {
              if(response.body()!=null){
                  mealsLiveData.value=response.body()!!.meals
              }
            }

            override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {
                Log.d("CategoryMealsViewModel",t.message.toString())
            }

        })
    }

    fun observeMealsLiveData():LiveData<List<MealsByCategory>>{
        return  mealsLiveData
    }
}