package com.positivestuff.findmykai.ui.search;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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
import com.positivestuff.findmykai.adapters.ComplexSearchRecipeAdapter;
import com.positivestuff.findmykai.adapters.FeaturedRecipeAdapter;
import com.positivestuff.findmykai.listeners.ComplexSearchRecipeResponseListener;
import com.positivestuff.findmykai.listeners.FeaturedRecipeResponseListener;
import com.positivestuff.findmykai.listeners.RecipeClickListener;
import com.positivestuff.findmykai.databinding.FragmentSearchBinding;
import com.positivestuff.findmykai.models.ComplexSearchRecipeResponse;
import com.positivestuff.findmykai.models.RandomRecipeAPIResponse;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;

    ProgressDialog dialog;
    RequestManager manager;
    ComplexSearchRecipeAdapter complexSearchRecipeAdapter;
    RecyclerView recyclerView;
    Spinner spinner;
    String recipeCategory;
    SearchView searchView;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SearchViewModel searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        dialog = new ProgressDialog(container.getContext());
        dialog.setTitle("Loading...");

        searchView = binding.searchViewSearchInput;
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

        spinner = binding.spinnerTags;
        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(
                container.getContext(),
                R.array.tags,
                R.layout.spinner_text
        );
        arrayAdapter.setDropDownViewResource(R.layout.spinner_inner_text);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(spinnerSelectedListener);

        manager = new RequestManager(container.getContext());

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private final AdapterView.OnItemSelectedListener spinnerSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            recipeCategory = parent.getSelectedItem().toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private final ComplexSearchRecipeResponseListener complexSearchRecipeResponseListener = new ComplexSearchRecipeResponseListener() {
        @Override
        public void didFetch(ComplexSearchRecipeResponse response, String message) {
            dialog.dismiss();
            if (response.results.size() > 0) {
                recyclerView = getView().findViewById(R.id.recyler_kai_results);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new GridLayoutManager(getActivity().getApplicationContext(), 1));
                complexSearchRecipeAdapter = new ComplexSearchRecipeAdapter(getActivity().getApplicationContext(), response.results, recipeClickListener);
                recyclerView.setAdapter(complexSearchRecipeAdapter);
            }
            else {
                // No results found
                Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.no_results), Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void didError(String message) {
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
        recipeCategory = recipeCategory.toLowerCase();
        manager.getComplexSearchRecipes(complexSearchRecipeResponseListener, searchWord, recipeCategory);
    }
}