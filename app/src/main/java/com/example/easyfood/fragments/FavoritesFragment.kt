package com.example.easyfood.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.easyfood.activities.MainActivity
import com.example.easyfood.adapters.MealsAdapter
import com.example.easyfood.databinding.FragmentFavoritesBinding
import com.example.easyfood.viewModel.HomeViewModel
import com.google.android.material.snackbar.Snackbar


class FavoritesFragment : Fragment() {
    private lateinit var binding: FragmentFavoritesBinding
   private lateinit var  viewModel: HomeViewModel
   private lateinit var favoritesMealsAdapter: MealsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel=(activity as MainActivity).homeViewModel

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding=FragmentFavoritesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareFavoriteMealsRecyclerView()
        observeFavorites()

        val itemTouchHelper=object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            )=true

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
               val position=viewHolder.adapterPosition
                val deletedMeal=favoritesMealsAdapter.differ.currentList[position]
                viewModel.deleteMeal(deletedMeal)
                Snackbar.make(requireView(),"Meal deleted",Snackbar.LENGTH_LONG).setAction(
                    "Undo",
                    View.OnClickListener {
                        viewModel.insertMeal(deletedMeal)
                    }
                ).show()
            }

        }

        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.recViewFavorites)

    }

    private fun prepareFavoriteMealsRecyclerView() {
        favoritesMealsAdapter= MealsAdapter()
        binding.recViewFavorites.apply {
            layoutManager=GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
            adapter=favoritesMealsAdapter
        }
    }

    private fun observeFavorites() {
        viewModel.observeFavoritesMealsLiveData().observe(viewLifecycleOwner
        ) { meals ->
           favoritesMealsAdapter.differ.submitList(meals)
        }
    }

}