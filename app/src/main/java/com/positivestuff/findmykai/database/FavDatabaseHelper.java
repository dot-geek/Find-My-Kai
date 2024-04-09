package com.positivestuff.findmykai.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.positivestuff.findmykai.models.Recipe;
import com.positivestuff.findmykai.models.Result;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class FavDatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "FINDMYKAI_LOG : ";
    // Database Info
    private static final String DATABASE_NAME = "FavouriteRecipeDatabase.db";
    private static final int DATABASE_VERSION = 1;

    private static FavDatabaseHelper sInstance;

    private static List<String> listOfFavouritesByID = new ArrayList<>();

    public static synchronized FavDatabaseHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new FavDatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * Make a call to the static method "getInstance()" instead.
     */
    private FavDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Called when the database connection is being configured.
    // Configure database settings for things like foreign key support, write-ahead logging, etc.
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    // Called when the database is created for the FIRST time.
    // If a database already exists on disk with the same DATABASE_NAME, this method will NOT be called.
    @Override
    public void onCreate(SQLiteDatabase db) {
//        String CREATE_POSTS_TABLE = "CREATE TABLE " + TABLE_POSTS +
//                "(" +
//                KEY_POST_ID + " INTEGER PRIMARY KEY," + // Define a primary key
//                KEY_POST_USER_ID_FK + " INTEGER REFERENCES " + TABLE_USERS + "," + // Define a foreign key
//                KEY_POST_TEXT + " TEXT" +
//                ")";

        String CREATE_FAV_RECIPE_TABLE = "CREATE TABLE " + FeedReaderContract.FeedEntry.TABLE_FAV_RECIPES + " (" +
                FeedReaderContract.FeedEntry._ID + " INTEGER PRIMARY KEY," +
                FeedReaderContract.FeedEntry.COLUMN_RECIPE_ID + " TEXT," +
                FeedReaderContract.FeedEntry.COLUMN_RECIPE_TITLE + " TEXT," +
                FeedReaderContract.FeedEntry.COLUMN_RECIPE_IMAGE + " TEXT)";


        //db.execSQL(CREATE_POSTS_TABLE);
        db.execSQL(CREATE_FAV_RECIPE_TABLE);
    }

    // Called when the database needs to be upgraded.
    // This method will only be called if a database already exists on disk with the same DATABASE_NAME,
    // but the DATABASE_VERSION is different than the version of the database that exists on disk.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            //db.execSQL("DROP TABLE IF EXISTS " + TABLE_POSTS);
            db.execSQL("DROP TABLE IF EXISTS " + FeedReaderContract.FeedEntry.TABLE_FAV_RECIPES);
            onCreate(db);
        }
    }

    // Insert or update a user in the database
    // Since SQLite doesn't support "upsert" we need to fall back on an attempt to UPDATE (in case the
    // user already exists) optionally followed by an INSERT (in case the user does not already exist).
    // Unfortunately, there is a bug with the insertOnConflict method
    // (https://code.google.com/p/android/issues/detail?id=13045) so we need to fall back to the more
    // verbose option of querying for the user's primary key if we did an update.
    public long addOrUpdateFavRecipe(Recipe recipe) {
        // The database connection is cached so it's not expensive to call getWriteableDatabase() multiple times.
        SQLiteDatabase db = getWritableDatabase();
        long userId = -1;

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(FeedReaderContract.FeedEntry.COLUMN_RECIPE_ID, String.valueOf(recipe.id));
            values.put(FeedReaderContract.FeedEntry.COLUMN_RECIPE_TITLE, recipe.title);
            values.put(FeedReaderContract.FeedEntry.COLUMN_RECIPE_IMAGE, recipe.image);

            // First try to update the user in case the user already exists in the database
            // This assumes userNames are unique
            int rows = db.update(FeedReaderContract.FeedEntry.TABLE_FAV_RECIPES, values,
                    FeedReaderContract.FeedEntry.COLUMN_RECIPE_ID + "= ?", new String[]{String.valueOf(recipe.id)});

            // Check if update succeeded
            if (rows == 1) {
                // Get the primary key of the user we just updated
                String recipeSelectQuery = String.format("SELECT %s FROM %s WHERE %s = ?",
                        FeedReaderContract.FeedEntry._ID,
                        FeedReaderContract.FeedEntry.TABLE_FAV_RECIPES,
                        FeedReaderContract.FeedEntry.COLUMN_RECIPE_ID);
                Cursor cursor = db.rawQuery(recipeSelectQuery, new String[]{String.valueOf(recipe.id)});
                try {
                    if (cursor.moveToFirst()) {
                        userId = cursor.getInt(0);
                        db.setTransactionSuccessful();
                    }
                } finally {
                    if (cursor != null && !cursor.isClosed()) {
                        cursor.close();
                    }
                }
            } else {
                // user with this userName did not already exist, so insert new user
                userId = db.insertOrThrow(FeedReaderContract.FeedEntry.TABLE_FAV_RECIPES, null, values);
                db.setTransactionSuccessful();
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add or update user");
        } finally {
            db.endTransaction();
        }
        listOfFavouritesByID.add(String.valueOf(recipe.id));
        return userId;
    }

    // Get all posts in the database
    public List<Recipe> getAllFavouriteRecipes() {
        List<Recipe> recipes = new ArrayList<>();

        String FAV_RECIPE_SELECT_QUERY =
                String.format("SELECT * FROM %s", FeedReaderContract.FeedEntry.TABLE_FAV_RECIPES);

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(FAV_RECIPE_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
//                    User newUser = new User();
//                    newUser.userName = cursor.getString(cursor.getColumnIndex(KEY_RECIPE_TITLE));
//                    newUser.profilePictureUrl = cursor.getString(cursor.getColumnIndex(KEY_RECIPE_IMAGE));

                    //TODO: Tidy up (int)
                    Recipe newRecipe = new Recipe();
                    String recipeID = cursor.getString((int)(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_RECIPE_ID)));
                    newRecipe.id = Integer.parseInt(recipeID);
                    newRecipe.title = cursor.getString((int)cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_RECIPE_TITLE));
                    newRecipe.image = cursor.getString((int)cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_RECIPE_IMAGE));

                    recipes.add(newRecipe);
                    listOfFavouritesByID.add(recipeID);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return recipes;
    }
    // Get all posts in the database
    public List<Result> getAllFavouriteRecipeResults() {
        List<Result> recipes = new ArrayList<>();

        listOfFavouritesByID.clear();


        String FAV_RECIPE_SELECT_QUERY =
                String.format("SELECT * FROM %s", FeedReaderContract.FeedEntry.TABLE_FAV_RECIPES);

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(FAV_RECIPE_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {

                    //TODO: Tidy up (int)
                    Result newRecipe = new Result();
                    String recipeID = cursor.getString((int)(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_RECIPE_ID)));
                    newRecipe.id = Integer.parseInt(recipeID);
                    newRecipe.title = cursor.getString((int)cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_RECIPE_TITLE));
                    newRecipe.image = cursor.getString((int)cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_RECIPE_IMAGE));

                    recipes.add(newRecipe);
                    listOfFavouritesByID.add(recipeID);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return recipes;
    }

    // Delete all posts and users in the database
    public void deleteAllFavRecipes() {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            // Order of deletions is important when foreign key relationships exist.
            //db.delete(TABLE_POSTS, null, null);
            db.delete(FeedReaderContract.FeedEntry.TABLE_FAV_RECIPES, null, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete all posts and users");
        } finally {
            db.endTransaction();
        }
    }

    public void initiate() {
        listOfFavouritesByID.clear();

        String FAV_RECIPE_SELECT_QUERY =
                String.format("SELECT * FROM %s", FeedReaderContract.FeedEntry.TABLE_FAV_RECIPES);

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(FAV_RECIPE_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    listOfFavouritesByID.add(cursor.getString((int)(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_RECIPE_ID))));
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }

    public boolean isFav(int recipeID) {
        return listOfFavouritesByID.contains(String.valueOf(recipeID));
    }


    public boolean removeFromFav(int recipeID){
        SQLiteDatabase db = getWritableDatabase();
//        db.execSQL("DELETE FROM " + FeedReaderContract.FeedEntry.TABLE_FAV_RECIPES +
//                " WHERE " + FeedReaderContract.FeedEntry.COLUMN_RECIPE_ID + "='" + recipeID + "'");
//        db.close();

        listOfFavouritesByID.remove(String.valueOf(recipeID));
        ContentValues args = new ContentValues();
        String whereClause = FeedReaderContract.FeedEntry.COLUMN_RECIPE_ID + "=?";
        int rowsDeleted = db.delete(FeedReaderContract.FeedEntry.TABLE_FAV_RECIPES,whereClause,new String[]{String.valueOf(recipeID)});
        return true;
    }


}