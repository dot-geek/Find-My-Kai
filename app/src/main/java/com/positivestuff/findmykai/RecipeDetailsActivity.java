package com.positivestuff.findmykai;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.positivestuff.findmykai.adapters.IngredientsAdapter;
import com.positivestuff.findmykai.database.FavDatabaseHelper;
import com.positivestuff.findmykai.listeners.RecipeDetailsListener;
import com.positivestuff.findmykai.models.AnalyzedInstruction;
import com.positivestuff.findmykai.models.ExtendedIngredient;
import com.positivestuff.findmykai.models.Recipe;
import com.positivestuff.findmykai.models.RecipeDetailsResponse;
import com.positivestuff.findmykai.models.Step;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecipeDetailsActivity extends AppCompatActivity {
    int id;
    TextView textView_meal_name, textView_meal_source, textView_meal_summary, textView_meal_ingredients, textView_list_ingredients;
    TextView textView_time, textView_servings;
    ImageView imageView_meal_image;
    //RecyclerView recycler_meal_ingredients;
    Button button_add_to_fav, button_cook_it;
    RequestManager manager;
    ProgressDialog dialog;
    //IngredientsAdapter ingredientsAdapter;
    RecipeDetailsResponse theRecipeResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        findViews();

        id = Integer.parseInt(getIntent().getStringExtra("id"));
        manager = new RequestManager(this);
        manager.getRecipeDetails(recipeDetailsListener, id);

        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading details...");
        dialog.show();;
    }

    private void findViews() {
        textView_meal_name = findViewById(R.id.textView_meal_name);
        textView_meal_source = findViewById(R.id.textView_meal_source);
        textView_meal_summary = findViewById(R.id.textView_meal_summary);
        textView_meal_ingredients = findViewById(R.id.textView_meal_ingredients);
        textView_list_ingredients = findViewById(R.id.textView_list_ingredients);
        imageView_meal_image = findViewById(R.id.imageView_meal_image);
        textView_time = findViewById(R.id.textView_time);
        textView_servings = findViewById(R.id.textView_servings);

        button_add_to_fav = findViewById(R.id.add_to_fav);
        button_add_to_fav.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                // Get singleton instance of database
                FavDatabaseHelper databaseHelper = FavDatabaseHelper.getInstance(RecipeDetailsActivity.this);
                boolean isFav = databaseHelper.isFav(theRecipeResponse.id);
                if (isFav) {
                    databaseHelper.removeFromFav(theRecipeResponse.id);
                    String theMsg = theRecipeResponse.title + getResources().getString(R.string.remove_from_fav_success);
                    Toast.makeText(RecipeDetailsActivity.this, theMsg, Toast.LENGTH_LONG).show();
                    button_add_to_fav.setText(getResources().getString(R.string.add_to_fav));
                }
                else {
                    Recipe recipe = new Recipe();
                    recipe.id = theRecipeResponse.id;
                    recipe.title = theRecipeResponse.title;
                    recipe.image = theRecipeResponse.image;

                    // Add sample post to the database
                    databaseHelper.addOrUpdateFavRecipe(recipe);
                    String theMsg = theRecipeResponse.title + getResources().getString(R.string.add_to_fav_success);
                    Toast.makeText(RecipeDetailsActivity.this, theMsg, Toast.LENGTH_LONG).show();
                    button_add_to_fav.setText(getResources().getString(R.string.remove_from_fav));
                }
            }
        });

        button_cook_it = findViewById(R.id.cook_it);
        button_cook_it.setOnClickListener(view -> {
            String url = theRecipeResponse.sourceUrl;
            Intent urlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(urlIntent);
        });

        //recycler_meal_ingredients = findViewById(R.id.recycler_meal_ingredients);
    }

    private final RecipeDetailsListener recipeDetailsListener = new RecipeDetailsListener() {
        @Override
        public void didFetch(RecipeDetailsResponse response, String message) {
            theRecipeResponse = response;
            dialog.dismiss();
            textView_meal_name.setText(response.title);
            textView_meal_source.setText(response.sourceName);
            //textView_meal_source.setText(response.sourceUrl);
            //textView_meal_summary.setText(response.sourceUrl + "\n\n" + response.summary);
            //textView_meal_summary.setText(response.sourceUrl);
            //textView_meal_summary.setText(response.summary);
            Picasso.get().load(response.image).into(imageView_meal_image);

            //textView_time.setText(response.readyInMinutes + R.string.minutes);
            //textView_servings.setText(response.servings + R.string.servings);
            textView_time.setText(R.string.minutes);
            textView_servings.setText(R.string.servings);


            List<ExtendedIngredient> list = response.extendedIngredients;
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < list.size(); i++) {
                builder.append("- ");
                builder.append(list.get(i).original);
                builder.append(" ");
                builder.append(list.get(i).name);
                builder.append("\n");
            }
            textView_list_ingredients.setText(builder.toString());

            builder.setLength(0);
            ArrayList<AnalyzedInstruction> analyzedInstructions = response.analyzedInstructions;
            for (int i = 0; i < analyzedInstructions.size(); i++) {
                List<Step> steps = analyzedInstructions.get(i).steps;
                for (int index = 0; index < steps.size(); index++) {
                    builder.append(steps.get(index).number);
                    builder.append(". ");
                    builder.append(steps.get(index).step);
                    builder.append("\n");
                }
            }
//            String theString = builder.toString() + "\n\n" + response.instructions + "\n\n" + response.summary;
//            textView_meal_summary.setText(theString);
            textView_meal_summary.setText(builder.toString());

            //textView_meal_summary.setText(response.instructions + "\n\n" + response.summary);


            FavDatabaseHelper databaseHelper = FavDatabaseHelper.getInstance(getApplicationContext());
            boolean isFav = databaseHelper.isFav(response.id);
            if (isFav) {
                button_add_to_fav.setText(getResources().getString(R.string.remove_from_fav));
            }
            else {
                button_add_to_fav.setText(getResources().getString(R.string.add_to_fav));
            }
        }

        @Override
        public void didError(String message) {
            Toast.makeText(RecipeDetailsActivity.this, message, Toast.LENGTH_SHORT).show();

        }
    };
}