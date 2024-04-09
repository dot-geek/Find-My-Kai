package com.positivestuff.findmykai.database;

import android.provider.BaseColumns;

public final class FeedReaderContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private FeedReaderContract() {}

    /* Inner class that defines the table contents */
    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_FAV_RECIPES = "favRecipes";
        // Table Colummns
        public static final String COLUMN_RECIPE_ID = "recipeID";
        public static final String COLUMN_RECIPE_TITLE = "recipeTitle";
        public static final String COLUMN_RECIPE_IMAGE = "recipeImageURL";
    }
}
