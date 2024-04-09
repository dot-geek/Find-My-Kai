package com.positivestuff.findmykai.listeners;

import com.positivestuff.findmykai.models.RandomRecipeAPIResponse;

public interface FeaturedRecipeResponseListener {
    void didFetch(RandomRecipeAPIResponse response, String message);
    void didError(String message);
}
