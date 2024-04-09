package com.positivestuff.findmykai.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.positivestuff.findmykai.R;
import com.positivestuff.findmykai.database.FavDatabaseHelper;
import com.positivestuff.findmykai.listeners.RecipeClickListener;
import com.positivestuff.findmykai.models.Recipe;
import com.positivestuff.findmykai.models.Result;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ComplexSearchRecipeAdapter extends RecyclerView.Adapter<ComplexSearchRecipeViewHolder> {
    Context context;
    List<Result> list;
    RecipeClickListener listener;

    public ComplexSearchRecipeAdapter(Context context, List<Result> list, RecipeClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ComplexSearchRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ComplexSearchRecipeViewHolder(LayoutInflater.from(context).inflate(R.layout.list_complex_recipe, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ComplexSearchRecipeViewHolder holder, int position) {
        holder.textView_title.setText(list.get(position).title);
        holder.textView_title.setSelected(true);
        Picasso.get().load(list.get(position).image).into(holder.imageView_food);

        if (FavDatabaseHelper.getInstance(context).isFav(list.get(position).id)) {
            holder.button_addOrRemoveFav.setText(holder.itemView.getContext().getResources().getString(R.string.remove_from_fav));
        }
        else {
            holder.button_addOrRemoveFav.setText(holder.itemView.getContext().getResources().getString(R.string.add_to_fav));
        }
        holder.button_addOrRemoveFav.setOnClickListener(view -> {
            Resources res = holder.itemView.getContext().getResources();
            Result theRecipe = list.get(holder.getAdapterPosition());

            // Get singleton instance of database
            FavDatabaseHelper databaseHelper = FavDatabaseHelper.getInstance(context);
            boolean isFav = databaseHelper.isFav(theRecipe.id);
            if (isFav) {
                databaseHelper.removeFromFav(theRecipe.id);
                String theMsg = theRecipe.title + res.getString(R.string.remove_from_fav_success);
                Toast.makeText(context, theMsg, Toast.LENGTH_LONG).show();
                holder.button_addOrRemoveFav.setText(res.getString(R.string.add_to_fav));
            }
            else {
                Recipe recipe = new Recipe();
                recipe.id = theRecipe.id;
                recipe.title = theRecipe.title;
                recipe.image = theRecipe.image;

                // Add sample post to the database
                databaseHelper.addOrUpdateFavRecipe(recipe);
                String theMsg = theRecipe.title + res.getString(R.string.add_to_fav_success);
                Toast.makeText(context, theMsg, Toast.LENGTH_LONG).show();
                holder.button_addOrRemoveFav.setText(res.getString(R.string.remove_from_fav));
            }
        });

        holder.featured_list_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRecipeClicked(String.valueOf(list.get(holder.getAdapterPosition()).id));

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
class ComplexSearchRecipeViewHolder extends RecyclerView.ViewHolder {

    CardView featured_list_container;
    TextView textView_title; //, textView_servings, textView_like, textView_time;
    ImageView imageView_food;
    Button button_addOrRemoveFav;

    public ComplexSearchRecipeViewHolder(@NonNull View itemView) {
        super(itemView);
        featured_list_container = itemView.findViewById(R.id.complex_list_container);
        textView_title = itemView.findViewById(R.id.textView_title);
        imageView_food = itemView.findViewById(R.id.imageView_food);
        button_addOrRemoveFav = itemView.findViewById(R.id.buttonView_addFav);
    }
}
