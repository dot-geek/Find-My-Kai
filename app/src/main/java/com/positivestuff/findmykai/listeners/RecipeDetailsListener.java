package com.positivestuff.findmykai.listeners;

import com.positivestuff.findmykai.models.RecipeDetailsResponse;

public interface RecipeDetailsListener {
    void didFetch(RecipeDetailsResponse response, String message);
    void didError(String message);
}
