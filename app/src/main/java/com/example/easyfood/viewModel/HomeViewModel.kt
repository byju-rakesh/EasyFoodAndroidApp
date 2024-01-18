package com.example.easyfood.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easyfood.data.RetrofitInstance
import com.example.easyfood.db.MealDatabase
import com.example.easyfood.model.Category
import com.example.easyfood.model.CategoryList
import com.example.easyfood.model.MealsByCategoryList
import com.example.easyfood.model.MealsByCategory
import com.example.easyfood.model.Meal
import com.example.easyfood.model.MealList
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(
    private val mealDatabase: MealDatabase
) :ViewModel() {

    private  var randomMealLiveData= MutableLiveData<Meal>()
    private var popularItemsLiveData=MutableLiveData<List<MealsByCategory>>()
    private var categoriesLiveData=MutableLiveData<List<Category>>()
    private var favoritesMealLiveData=mealDatabase.mealDao().getAllMeals()
    private var bottomSheetMealLiveData=MutableLiveData<Meal>()
    private var searchMealsLiveData=MutableLiveData<List<Meal>>()

    // this store the data  1 way
//    init {
//        getRandomMeal()
//    }
    // 2nd way use let block

   private var saveStateRandomMeal:Meal?=null
    fun getRandomMeal(){

       saveStateRandomMeal?.let{
            randomMealLiveData.postValue(it)
           return
        }

        RetrofitInstance.api.getRandomMeal().enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.body()!=null){
                    val randomMeal: Meal =response.body()!!.meals[0]
                    randomMealLiveData.value=randomMeal
                    saveStateRandomMeal=randomMeal
                }
                else{
                    return
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("HomeFragment",t.message.toString())
            }

        })
    }

    fun getPopularItems(){
        RetrofitInstance.api.getPopularItems("Seafood").enqueue(object :Callback<MealsByCategoryList>{
            override fun onResponse(call: Call<MealsByCategoryList>, response: Response<MealsByCategoryList>) {
                if(response.body()!=null){
                    popularItemsLiveData.value=response.body()!!.meals
                }
            }

            override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {
                Log.d("HomeFragment",t.message.toString())
            }

        })
    }

    fun getCategories(){
        RetrofitInstance.api.getCategories().enqueue(object : Callback<CategoryList>{
            override fun onResponse(call: Call<CategoryList>, response: Response<CategoryList>) {
                if (response.body()!=null){
                    categoriesLiveData.value=response.body()!!.categories
                }
            }

            override fun onFailure(call: Call<CategoryList>, t: Throwable) {
                Log.d("HomeViewModel",t.message.toString())
            }

        })
    }

    fun getMealById(mealId:String){
        RetrofitInstance.api.getMealDetails(mealId).enqueue(object :Callback<MealList>{
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.body()!=null){
                    bottomSheetMealLiveData.value=response.body()!!.meals.first()
                }
                else return
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("BottomSheetFragment",t.message.toString())
            }

        } )
    }

    fun deleteMeal(meal: Meal){
        viewModelScope.launch {
            mealDatabase.mealDao().delete(meal)
        }
    }
    fun insertMeal(meal: Meal){
        viewModelScope.launch {
            mealDatabase.mealDao().insertMeal(meal)
        }
    }

    fun searchMeals(searchQuery:String)=RetrofitInstance.api.searchMeals(searchQuery).enqueue(
        object :Callback<MealList>{
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
               if (response.body()!=null){
                   searchMealsLiveData.value=response.body()!!.meals
               }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
               Log.d("HomeViewModel",t.message.toString())
            }

        }
    )


    fun observeRandomMealLiveData():LiveData<Meal>{
        return  randomMealLiveData;
    }

    fun observePopularItemsLiveData():LiveData<List<MealsByCategory>>{
        return popularItemsLiveData
    }

    fun observeCategoriesLiveData():LiveData<List<Category>>{
        return categoriesLiveData
    }
    fun observeFavoritesMealsLiveData():LiveData<List<Meal>>{
        return favoritesMealLiveData
    }

    fun observeBottomSheetMealLiveData():LiveData<Meal>{
        return bottomSheetMealLiveData
    }

    fun observeSearchMealsLiveData():LiveData<List<Meal>>{
        return searchMealsLiveData
    }
}