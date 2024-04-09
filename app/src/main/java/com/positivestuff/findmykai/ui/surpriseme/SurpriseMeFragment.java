package com.positivestuff.findmykai.ui.surpriseme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.positivestuff.findmykai.R;
import com.positivestuff.findmykai.RecipeDetailsActivity;
import com.positivestuff.findmykai.RequestManager;
import com.positivestuff.findmykai.adapters.FeaturedRecipeAdapter;
import com.positivestuff.findmykai.listeners.FeaturedRecipeResponseListener;
import com.positivestuff.findmykai.listeners.RecipeClickListener;
import com.positivestuff.findmykai.databinding.FragmentSurpriseMeBinding;
import com.positivestuff.findmykai.models.RandomRecipeAPIResponse;

import java.util.ArrayList;
import java.util.List;

public class SurpriseMeFragment extends Fragment {

    private FragmentSurpriseMeBinding binding;

    ProgressDialog dialog;
    RequestManager manager;
    FeaturedRecipeAdapter featuredRecipeAdapter;
    RecyclerView recyclerView;
    Spinner spinner;
    List<String> tags = new ArrayList<>();
    SearchView searchView;
    CheckBox tagCheckBox1, tagCheckBox2, tagCheckBox3, tagCheckBox4, tagCheckBox5, tagCheckBox6;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SurpriseMeViewModel searchViewModel =
                new ViewModelProvider(this).get(SurpriseMeViewModel.class);

        binding = FragmentSurpriseMeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();



        dialog = new ProgressDialog(container.getContext());
        dialog.setTitle("Loading...");

        searchView = binding.surpriseMeSearchInput;
        //searchView = (SurpriseMeView) getView().findViewById(R.id.searchView_home);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchRecipe(query);
                dialog.show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        tagCheckBox1 = binding.tagCheckBox1;
        tagCheckBox2 = binding.tagCheckBox2;
        tagCheckBox3 = binding.tagCheckBox3;
        tagCheckBox4 = binding.tagCheckBox4;
        tagCheckBox5 = binding.tagCheckBox5;
        tagCheckBox6 = binding.tagCheckBox6;

        manager = new RequestManager(container.getContext());


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }





    private final FeaturedRecipeResponseListener featuredRecipeResponseListener = new FeaturedRecipeResponseListener() {
        @Override
        public void didFetch(RandomRecipeAPIResponse response, String message) {
            dialog.dismiss();
            recyclerView = getView().findViewById(R.id.recyler_kai_results);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity().getApplicationContext(), 1));
            featuredRecipeAdapter = new FeaturedRecipeAdapter(getActivity().getApplicationContext(), response.recipes, recipeClickListener);
            recyclerView.setAdapter(featuredRecipeAdapter);
        }

        @Override
        public void didError(String message) {
            //Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT).show();

        }
    };

    private final RecipeClickListener recipeClickListener = new RecipeClickListener() {
        @Override
        public void onRecipeClicked(String id) {

            Intent intent = new Intent(getContext(), RecipeDetailsActivity.class);
            intent.putExtra("id", id);
            startActivity(intent);
        }
    };

    private void searchRecipe(String searchWord) {
        tags.clear();
        tags.add(searchWord);
        // Go through checkboxes
        if (tagCheckBox1.isChecked())
        {
            tags.add( getResources().getString(R.string.food_tag_1));
        }
        if (tagCheckBox2.isChecked())
        {
            tags.add( getResources().getString(R.string.food_tag_2));
        }
        if (tagCheckBox3.isChecked())
        {
            tags.add( getResources().getString(R.string.food_tag_3));
        }
        if (tagCheckBox4.isChecked())
        {
            tags.add( getResources().getString(R.string.food_tag_4));
        }
        if (tagCheckBox5.isChecked())
        {
            tags.add( getResources().getString(R.string.food_tag_5));
        }
        if (tagCheckBox6.isChecked())
        {
            tags.add( getResources().getString(R.string.food_tag_6));
        }

        manager.getFeaturedRecipes(featuredRecipeResponseListener, tags);

    }
}