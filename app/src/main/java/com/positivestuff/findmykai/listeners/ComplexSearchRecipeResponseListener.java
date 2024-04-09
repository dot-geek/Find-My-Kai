package com.positivestuff.findmykai.listeners;

import com.positivestuff.findmykai.models.ComplexSearchRecipeResponse;
import com.positivestuff.findmykai.models.RandomRecipeAPIResponse;

public interface ComplexSearchRecipeResponseListener {
    void didFetch(ComplexSearchRecipeResponse response, String message);
    void didError(String message);
}
