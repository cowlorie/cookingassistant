package com.CookingAssistant;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.location.Address;
import android.os.Parcelable.Creator;
import android.util.Log;


public class RecipeDbAdapter {

	public static final String KEY_TITLE = "title";
	public static final String KEY_FAVORITE = "favorite";
    public static final String KEY_INGREDIENT = "ingredient";
    public static final String KEY_STEP = "instruction";
    public static final String KEY_ROWID = "_id";
    public static final String KEY_RECIPEID = "recipe";

    private static final String TAG = "RecipeDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    
    /**
     * Database creation sql statement
     */
    private static final String DATABASE_CREATE_RECIPE_TABLE =
            "create table recipe ("
    			+ "_id integer primary key autoincrement, "
    			+ "title text not null, "
    			+ "favorite integer); ";
    private static final String DATABASE_CREATE_INGREDIENT_TABLE =
    		"create table ingredient ("
    			+ "_id integer primary key autoincrement, "
    			+ "foreign key (recipe) references recipe(_id), "
    			+ "ingredient text not null); ";
    private static final String DATABASE_CREATE_STEP_TABLE = 
            "create table step ("
            	+ "_id integer primary key autoincrement, "
            	+ "foreign key (recipe) references recipe(_id), "
            	+ "step text not null); ";

    private static final String DATABASE_NAME = "data";
    private static final String DATABASE_RECIPE_TABLE = "recipes";
    private static final String DATABASE_INGREDIENT_TABLE = "ingredient";
    private static final String DATABASE_STEP_TABLE = "step";
    
    private static final int DATABASE_VERSION = 6;

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE_RECIPE_TABLE);
            db.execSQL(DATABASE_CREATE_INGREDIENT_TABLE);
            db.execSQL(DATABASE_CREATE_STEP_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            if (oldVersion < 6)
            	db.execSQL("DROP TABLE IF EXISTS recipes");
            onCreate(db);
        }
    }

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     * 
     * @param ctx the Context within which to work
     */
    public RecipeDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Open the recipes database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     * 
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public RecipeDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        
        // Populate DB with temporary test data
        Cursor cur = fetchAllRecipes();
        if (cur.getCount() == 0) {
        	Recipe r = new Recipe();
        	
        	r.title = "First recipe";
        	r.ingredients = new String[] {"1 sugar", "2 salt", "water"};
        	r.steps = new String[] {"mix sugar with salt", "add water"};
        	createRecipe(r);
        	
        	r.title = "Second recipe";
        	r.ingredients = new String[] {"water", "noodles"};
        	r.steps = new String[] {"boil water", "add noodles", "eat"};
        	createRecipe(r);
      
        	r.title = "Third recipe";
        	r.ingredients = new String[] {"two licorice"};
        	r.steps = new String[] {"eat and chew a lot"};
        	createRecipe(r);
        	
        	r.title = "Fourth recipe";
        	r.ingredients = new String[] {"steak"};
        	r.steps = new String[] {"buy steak"};
        	createRecipe(r);
        	
        	r.title = "Fifth recipe";
        	r.ingredients = new String[] {"skittles"};
        	r.steps = new String[] {"taste the rainbow"};
        	createRecipe(r);
        }
        cur.close();
        
        return this;
    }
    
    public void close() {
        mDbHelper.close();
    }


    /**
     * Create a new recipe using the content from the given Recipe. If the recipe is
     * successfully created return the new rowId for that recipe, otherwise return
     * a -1 to indicate failure.
     * 
     * @param title the title of the recipe
     * @param body the body of the recipe
     * @return rowId or -1 if failed
     */
    public long createRecipe(Recipe recipe) {
        ContentValues recipeValues = new ContentValues();
        recipeValues.put(KEY_TITLE, recipe.title);
        recipeValues.put(KEY_FAVORITE, recipe.favorite);
        long recipeId = mDb.insertOrThrow(DATABASE_RECIPE_TABLE, null, recipeValues);

    	recipeValues.clear();
        for (String ingredient : recipe.ingredients) {
        	recipeValues.put(KEY_RECIPEID, recipeId);
        	recipeValues.put(KEY_INGREDIENT, ingredient);
        	mDb.insert(DATABASE_INGREDIENT_TABLE, null, recipeValues);
        }
        
    	recipeValues.clear();
        for (String step : recipe.steps) {
        	recipeValues.put(KEY_RECIPEID, recipeId);
        	recipeValues.put(KEY_STEP, step);
        	mDb.insert(DATABASE_STEP_TABLE, null, recipeValues);
        }

        return recipeId;
    }

    /**
     * Delete the recipe with the given rowId
     * 
     * @param rowId id of recipe to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteRecipe(long rowId) {
    	boolean deleted = mDb.delete(DATABASE_RECIPE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    	if (deleted) {
    		mDb.delete(DATABASE_INGREDIENT_TABLE, KEY_RECIPEID + "=" + rowId, null);
    		mDb.delete(DATABASE_STEP_TABLE, KEY_RECIPEID + "=" + rowId, null);
    	}
    	return deleted;
    }

    /**
     * Return a Cursor over the list of all recipes in the database
     * 
     * @return Cursor over all recipes
     */
    public Cursor fetchAllRecipes() {
        return mDb.query(DATABASE_RECIPE_TABLE, new String[] {KEY_ROWID, KEY_TITLE, KEY_FAVORITE}, null, null, null, null, null);
    }

    /**
     * Return a Recipe loaded from the given rowId
     * 
     * @param rowId id of recipe to retrieve
     * @return Recipe loaded with values for given recipe
     * @throws SQLException if recipe could not be found/retrieved
     */
    public Recipe fetchRecipe(long rowId) throws SQLException {
    	Recipe recipe = new Recipe();
    	
    	// General recipe info
        Cursor mCursor = 
        	mDb.query(true, DATABASE_RECIPE_TABLE, 
        			  new String[] {KEY_ROWID, KEY_TITLE, KEY_FAVORITE},
        			  KEY_ROWID + "=" + rowId, null,
                      null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
            recipe.id = mCursor.getLong(mCursor.getColumnIndex(KEY_ROWID));
            recipe.title = mCursor.getString(mCursor.getColumnIndex(KEY_TITLE));
            recipe.favorite = mCursor.getInt(mCursor.getColumnIndex(KEY_FAVORITE)) != 0;
        }
        mCursor.close();
        
        // Ingredients
        ArrayList<String> ingredients = new ArrayList<String> ();
        mCursor =
        	mDb.query(false, DATABASE_INGREDIENT_TABLE,
        			  new String[] {KEY_INGREDIENT},
        			  KEY_RECIPEID + "=" + rowId, null,
        			  null, null, null, null);
        if (mCursor != null) {
        	mCursor.moveToFirst();
            while (mCursor.isAfterLast() == false) {
                ingredients.add(mCursor.getString(mCursor.getColumnIndex(KEY_INGREDIENT)));
                mCursor.moveToNext();
            }
        }
        recipe.ingredients = (String[]) ingredients.toArray();
        mCursor.close();
        
        // Steps
        ArrayList<String> steps = new ArrayList<String> ();
        mCursor =
        	mDb.query(false, DATABASE_STEP_TABLE,
        			  new String[] {KEY_STEP},
        			  KEY_RECIPEID + "=" + rowId, null,
        			  null, null, null, null);
        if (mCursor != null) {
        	mCursor.moveToFirst();
            while (mCursor.isAfterLast() == false) {
                steps.add(mCursor.getString(mCursor.getColumnIndex(KEY_STEP)));
                mCursor.moveToNext();
            }
        }
        recipe.steps = (String[]) steps.toArray();
        mCursor.close();
        
        return recipe;
    }
    
    public Cursor searchRecipes(String s) {
    	// FIXME: Need to query against the ingredients and steps!
    	Cursor mCursor =
            mDb.query(true, DATABASE_RECIPE_TABLE, new String[] {KEY_ROWID,
                    KEY_TITLE, KEY_FAVORITE}, KEY_TITLE + " LIKE " + '?', 
                    new String[]{"%" + s + "%"},
                    null, null, null, null);
    	if (mCursor != null) {
    		mCursor.moveToFirst();
    	}
    	return mCursor;
    }

    /**
     * Update the recipe using the details provided in Recipe. The recipe to be updated is
     * specified using the rowId, and it is altered to use the title and body
     * values passed in
     * 
     * @param rowId id of recipe to update
     * @param title value to set recipe title to
     * @param body value to set recipe body to
     * @return true if the recipe was successfully updated, false otherwise
     */
    public boolean updaterecipe(Recipe recipe) {
        ContentValues args = new ContentValues();
        args.put(KEY_TITLE, recipe.title);
        args.put(KEY_FAVORITE, recipe.favorite);
        boolean result = mDb.update(DATABASE_RECIPE_TABLE, args, KEY_ROWID + "=" + recipe.id, null) > 0;
        
        // FIXME: Need to also update ingredients and steps

        return result;
    }
}
