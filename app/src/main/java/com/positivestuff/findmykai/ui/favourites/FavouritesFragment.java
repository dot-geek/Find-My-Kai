package com.positivestuff.findmykai.ui.favourites;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.positivestuff.findmykai.R;
import com.positivestuff.findmykai.RecipeDetailsActivity;
import com.positivestuff.findmykai.RequestManager;
import com.positivestuff.findmykai.adapters.ComplexSearchRecipeAdapter;
import com.positivestuff.findmykai.database.FavDatabaseHelper;
import com.positivestuff.findmykai.databinding.FragmentFavouritesBinding;
import com.positivestuff.findmykai.databinding.FragmentSearchBinding;
import com.positivestuff.findmykai.listeners.RecipeClickListener;
import com.positivestuff.findmykai.models.Recipe;
import com.positivestuff.findmykai.models.Result;

import java.util.List;

public class FavouritesFragment extends Fragment {

    private FragmentFavouritesBinding binding;
    RecyclerView recyclerView;
    ComplexSearchRecipeAdapter complexSearchRecipeAdapter;
    //List<Result> allFavouriteRecipeResults;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        FavouritesViewModel searchViewModel =
                new ViewModelProvider(this).get(FavouritesViewModel.class);

        binding = FragmentFavouritesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        // Get singleton instance of database
//        allFavouriteRecipeResults = FavDatabaseHelper.getInstance(getContext()).getAllFavouriteRecipeResults();
//
//        recyclerView = binding.recylerFavResults;
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
//        complexSearchRecipeAdapter = new ComplexSearchRecipeAdapter(getContext(), allFavouriteRecipeResults, recipeClickListener);
//        recyclerView.setAdapter(complexSearchRecipeAdapter);


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
//        allFavouriteRecipeResults.clear();
//        allFavouriteRecipeResults = FavDatabaseHelper.getInstance(getContext()).getAllFavouriteRecipeResults();
//        complexSearchRecipeAdapter.notifyDataSetChanged();

        // Get singleton instance of database
        List<Result> allFavouriteRecipeResults = FavDatabaseHelper.getInstance(getContext()).getAllFavouriteRecipeResults();

        recyclerView = binding.recylerFavResults;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        complexSearchRecipeAdapter = new ComplexSearchRecipeAdapter(getContext(), allFavouriteRecipeResults, recipeClickListener);
        recyclerView.setAdapter(complexSearchRecipeAdapter);

        super.onResume();

    }

    private final RecipeClickListener recipeClickListener = new RecipeClickListener() {
        @Override
        public void onRecipeClicked(String id) {

            Intent intent = new Intent(getContext(), RecipeDetailsActivity.class);
            intent.putExtra("id", id);
            startActivity(intent);
        }
    };

}
