package com.positivestuff.findmykai;

import android.content.Context;

import com.positivestuff.findmykai.listeners.ComplexSearchRecipeResponseListener;
import com.positivestuff.findmykai.listeners.FeaturedRecipeResponseListener;
import com.positivestuff.findmykai.listeners.RecipeDetailsListener;
import com.positivestuff.findmykai.models.ComplexSearchRecipeResponse;
import com.positivestuff.findmykai.models.RandomRecipeAPIResponse;
import com.positivestuff.findmykai.models.RecipeDetailsResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class RequestManager {
    Context context;
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.spoonacular.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public RequestManager(Context context) {
        this.context = context;
    }

    public void getFeaturedRecipes(FeaturedRecipeResponseListener listener, List<String> tags) {
        CallFeaturedRecipes callFeaturedRecipes = retrofit.create(CallFeaturedRecipes.class);
        Call<RandomRecipeAPIResponse> call = callFeaturedRecipes.callFeaturedRecipe(context.getString(R.string.api_key),"10", tags);
        call.enqueue(new Callback<RandomRecipeAPIResponse>() {
            @Override
            public void onResponse(Call<RandomRecipeAPIResponse> call, Response<RandomRecipeAPIResponse> response) {
                if (!response.isSuccessful()) {
                    listener.didError(response.message());
                    return;
                }
                listener.didFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<RandomRecipeAPIResponse> call, Throwable t) {
                listener.didError(t.getMessage());
            }
        });
    }

    public void getRecipeDetails(RecipeDetailsListener listener, int id) {
        CallRecipeDetails callRecipeDetails = retrofit.create(CallRecipeDetails.class);
        Call<RecipeDetailsResponse> call = callRecipeDetails.callRecipeDetails(id, context.getString(R.string.api_key));
        call.enqueue(new Callback<RecipeDetailsResponse>() {
            @Override
            public void onResponse(Call<RecipeDetailsResponse> call, Response<RecipeDetailsResponse> response) {
                if (!response.isSuccessful()) {
                    listener.didError(response.message());
                    return;
                }
                listener.didFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<RecipeDetailsResponse> call, Throwable t) {
                listener.didError(t.getMessage());
            }
        });
    }

    public void getComplexSearchRecipes(ComplexSearchRecipeResponseListener listener, String query, String type) {
        CallComplexSearchRecipe callSearchRecipes = retrofit.create(CallComplexSearchRecipe.class);
        Call<ComplexSearchRecipeResponse> call = callSearchRecipes.callComplexSearchRecipe(context.getString(R.string.api_key),"10", query, type);
        call.enqueue(new Callback<ComplexSearchRecipeResponse>() {
            @Override
            public void onResponse(Call<ComplexSearchRecipeResponse> call, Response<ComplexSearchRecipeResponse> response) {
                if (!response.isSuccessful()) {
                    listener.didError(response.message());
                    return;
                }
                listener.didFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<ComplexSearchRecipeResponse> call, Throwable t) {
                listener.didError(t.getMessage());
            }
        });
    }

    private interface CallFeaturedRecipes {
        @GET("recipes/random")
        Call<RandomRecipeAPIResponse> callFeaturedRecipe (
                @Query("apiKey") String apiKey,
                @Query("number") String number,
                @Query("include-tags") List<String> tags
        );
    }

    private interface CallRecipeDetails {
        @GET("recipes/{id}/information")
        Call<RecipeDetailsResponse> callRecipeDetails (
                @Path("id") int id,
                @Query("apiKey") String apiKey
        );
    }

    private interface CallComplexSearchRecipe {
        @GET("recipes/complexSearch")
        Call<ComplexSearchRecipeResponse> callComplexSearchRecipe (
                @Query("apiKey") String apiKey,
                @Query("number") String number,
                @Query("query") String query,
                @Query("type") String type
        );
    }
}
