package com.positivestuff.findmykai;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.positivestuff.findmykai.adapters.FeaturedRecipeAdapter;
import com.positivestuff.findmykai.listeners.FeaturedRecipeResponseListener;
import com.positivestuff.findmykai.listeners.RecipeClickListener;
import com.positivestuff.findmykai.models.RandomRecipeAPIResponse;

import java.util.ArrayList;
import java.util.List;

public class RecipeListActivity extends AppCompatActivity {


    ProgressDialog dialog;
    RequestManager manager;
    FeaturedRecipeAdapter featuredRecipeAdapter;
    RecyclerView recyclerView;
    Spinner spinner;
    List<String> tags = new ArrayList<>();
    //SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_this_weeks_kai);

        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading...");

        manager = new RequestManager(this);

        tags = getIntent().getStringArrayListExtra("tags");

        recyclerView = findViewById(R.id.recyler_featured_kai);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(RecipeListActivity.this, 1));

        manager.getFeaturedRecipes(featuredRecipeResponseListener, tags);
        dialog.show();

    }




    private final FeaturedRecipeResponseListener featuredRecipeResponseListener = new FeaturedRecipeResponseListener() {
        @Override
        public void didFetch(RandomRecipeAPIResponse response, String message) {
            dialog.dismiss();
            featuredRecipeAdapter = new FeaturedRecipeAdapter(RecipeListActivity.this, response.recipes, recipeClickListener);
            recyclerView.setAdapter(featuredRecipeAdapter);
        }

        @Override
        public void didError(String message) {
            //Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

        }
    };

    private final RecipeClickListener recipeClickListener = new RecipeClickListener() {
        @Override
        public void onRecipeClicked(String id) {
            //Toast.makeText(MainActivity.this, id, Toast.LENGTH_SHORT).show();
            //Toast.makeText(getContext(), id, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(RecipeListActivity.this, RecipeDetailsActivity.class);
            intent.putExtra("id", id);
            startActivity(intent);
            //startActivity(new Intent(getContext(), RecipeDetailsActivity.class).putExtra("id", id));
        }
    };
}
