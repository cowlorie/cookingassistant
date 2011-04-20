package com.CookingAssistant;

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

	public static final String KEY_ROWID = "_id";

	public static final String KEY_NAME = "name";
	public static final String KEY_FAVORITE = "favorite";
	public static final String KEY_COOK_TIME = "cook_time";
	public static final String KEY_SERV_SIZE = "serv_size";
	public static final String KEY_PHOTO_URLS = "photo_urls";
	public static final String KEY_DESC = "desc";

	public static final String KEY_RECIPEID = "recipe_id";
	public static final String KEY_INGREDIENT = "ingredient";
	public static final String KEY_STEP = "step";

	private static final String TAG = "RecipeDbAdapter";
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;

	/**
	 * Database creation sql statement
	 */
	private static final String DATABASE_CREATE_RECIPE_TABLE = "create table recipe ("
			+ "_id integer primary key autoincrement, "
			+ "name text not null, "
			+ "favorite integer, "
			+ "cook_time text, "
			+ "desc text, "
			+ "serv_size text, "
			+ "photo_urls text); ";
	private static final String DATABASE_CREATE_INGREDIENT_TABLE = "create table ingredient ("
			+ "_id integer primary key autoincrement, "
			+ "recipe_id integer not null, "
			+ "ingredient text not null,"
			+ "foreign key (recipe_id) references recipe(_id)); ";
	private static final String DATABASE_CREATE_STEP_TABLE = "create table step ("
			+ "_id integer primary key autoincrement, "
			+ "recipe_id integer not null, "
			+ "step text not null, "
			+ "foreign key (recipe_id) references recipe(_id)); ";
	// TODO: Multiple shopping lists?
	private static final String DATABASE_CREATE_SHOPPING_LIST_TABLE = "create table shopping_list ("
			+ "_id integer primary key autoincrement, "
			+ "ingredient text not null);";

	private static final String DATABASE_NAME = "data";
	private static final String DATABASE_RECIPE_TABLE = "recipe";
	private static final String DATABASE_INGREDIENT_TABLE = "ingredient";
	private static final String DATABASE_STEP_TABLE = "step";
	private static final String DATABASE_SHOPPING_LIST_TABLE = "shopping_list";

	private static final int DATABASE_VERSION = 15;

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
			db.execSQL(DATABASE_CREATE_SHOPPING_LIST_TABLE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			if (oldVersion < newVersion) {
				db.execSQL("DROP TABLE IF EXISTS recipe");
				db.execSQL("DROP TABLE IF EXISTS ingredient");
				db.execSQL("DROP TABLE IF EXISTS step");
				db.execSQL("DROP TABLE IF EXISTS shopping_list");
			}
			onCreate(db);
		}
	}

	/**
	 * Constructor - takes the context to allow the database to be
	 * opened/created
	 * 
	 * @param ctx
	 *            the Context within which to work
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
	 * @throws SQLException
	 *             if the database could be neither opened or created
	 */
	public RecipeDbAdapter open() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();

		// Populate DB with temporary test data
		Cursor cur = fetchAllRecipes();
		if (cur.getCount() == 0) {
			Recipe r = new Recipe();

			r.name = "First recipe";
			r.ingredients = new String[] { "1 sugar", "2 salt", "water" };
			r.steps = new String[] { "mix sugar with salt", "add water" };
			createRecipe(r);

			r.name = "Second recipe";
			r.ingredients = new String[] { "water", "noodles" };
			r.steps = new String[] { "boil water", "add noodles", "eat" };
			createRecipe(r);

			r.name = "Third recipe";
			r.ingredients = new String[] { "two licorice" };
			r.steps = new String[] { "eat and chew a lot" };
			createRecipe(r);

			r.name = "Fourth recipe";
			r.ingredients = new String[] { "steak" };
			r.steps = new String[] { "buy steak" };
			createRecipe(r);

			r.name = "Fifth recipe";
			r.ingredients = new String[] { "skittles" };
			r.steps = new String[] { "taste the rainbow" };
			createRecipe(r);
		}
		cur.close();

		return this;
	}

	public void close() {
		mDbHelper.close();
	}

	/**
	 * Create a new recipe using the content from the given Recipe. If the
	 * recipe is successfully created return the new rowId for that recipe,
	 * otherwise return a -1 to indicate failure.
	 * 
	 * @param name
	 *            the title of the recipe
	 * @param body
	 *            the body of the recipe
	 * @return rowId or -1 if failed
	 */
	public long createRecipe(Recipe recipe) {
		ContentValues recipeValues = new ContentValues();
		recipeValues.put(KEY_NAME, recipe.name);
		recipeValues.put(KEY_FAVORITE, recipe.favorite);
		recipeValues.put(KEY_COOK_TIME, recipe.cook_time);
		recipeValues.put(KEY_SERV_SIZE, recipe.serv_size);
		recipeValues.put(KEY_PHOTO_URLS, recipe.photo_urls);
		recipeValues.put(KEY_DESC, recipe.desc);
		long recipeId = mDb.insertOrThrow(DATABASE_RECIPE_TABLE, null,
				recipeValues);

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
	 * @param rowId
	 *            id of recipe to delete
	 * @return true if deleted, false otherwise
	 */
	public boolean deleteRecipe(long rowId) {
		boolean deleted = mDb.delete(DATABASE_RECIPE_TABLE, KEY_ROWID + "="
				+ rowId, null) > 0;
		if (deleted) {
			mDb.delete(DATABASE_INGREDIENT_TABLE, KEY_RECIPEID + "=" + rowId,
					null);
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
		return mDb.query(DATABASE_RECIPE_TABLE, new String[] { KEY_ROWID,
				KEY_NAME, KEY_FAVORITE }, null, null, null, null, null);
	}

	/**
	 * Return a Recipe loaded from the given rowId
	 * 
	 * @param rowId
	 *            id of recipe to retrieve
	 * @return Recipe loaded with values for given recipe
	 * @throws SQLException
	 *             if recipe could not be found/retrieved
	 */
	public Recipe fetchRecipe(long rowId) throws SQLException {
		Recipe recipe = new Recipe();

		// General recipe info
		Cursor mCursor = mDb.query(true, DATABASE_RECIPE_TABLE, new String[] {
				KEY_ROWID, KEY_NAME, KEY_FAVORITE, KEY_COOK_TIME,
				KEY_SERV_SIZE, KEY_PHOTO_URLS, KEY_DESC }, KEY_ROWID + "="
				+ rowId, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
			recipe.id = mCursor.getLong(mCursor.getColumnIndex(KEY_ROWID));
			recipe.name = mCursor.getString(mCursor.getColumnIndex(KEY_NAME));
			recipe.favorite = mCursor.getInt(mCursor
					.getColumnIndex(KEY_FAVORITE)) != 0;
			recipe.cook_time = mCursor.getString(mCursor
					.getColumnIndex(KEY_COOK_TIME));
			recipe.serv_size = mCursor.getString(mCursor
					.getColumnIndex(KEY_SERV_SIZE));
			recipe.photo_urls = mCursor.getString(mCursor
					.getColumnIndex(KEY_PHOTO_URLS));
			recipe.desc = mCursor.getString(mCursor.getColumnIndex(KEY_DESC));
		}
		mCursor.close();

		// Ingredients
		mCursor = mDb.query(false, DATABASE_INGREDIENT_TABLE,
				new String[] { KEY_INGREDIENT }, KEY_RECIPEID + "=" + rowId,
				null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
			recipe.ingredients = new String[mCursor.getCount()];
			int i = 0;
			while (mCursor.isAfterLast() == false) {
				recipe.ingredients[i++] = mCursor.getString(mCursor
						.getColumnIndex(KEY_INGREDIENT));
				mCursor.moveToNext();
			}
		}
		mCursor.close();

		// Steps
		mCursor = mDb.query(false, DATABASE_STEP_TABLE,
				new String[] { KEY_STEP }, KEY_RECIPEID + "=" + rowId, null,
				null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
			recipe.steps = new String[mCursor.getCount()];
			int s = 0;
			while (mCursor.isAfterLast() == false) {
				recipe.steps[s++] = mCursor.getString(mCursor
						.getColumnIndex(KEY_STEP));
				mCursor.moveToNext();
			}
		}
		mCursor.close();

		return recipe;
	}

	public Cursor searchRecipes(String s) {
		// FIXME: Need to query against the ingredients and steps!
		Cursor mCursor = mDb.query(true, DATABASE_RECIPE_TABLE, new String[] {
				KEY_ROWID, KEY_NAME, KEY_FAVORITE }, KEY_NAME + " LIKE " + '?',
				new String[] { "%" + s + "%" }, null, null, KEY_NAME, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	/**
	 * Update the recipe using the details provided in Recipe. The recipe to be
	 * updated is specified using the rowId, and it is altered to use the title
	 * and body values passed in
	 * 
	 * @param rowId
	 *            id of recipe to update
	 * @param name
	 *            value to set recipe title to
	 * @param body
	 *            value to set recipe body to
	 * @return true if the recipe was successfully updated, false otherwise
	 */
	public boolean updaterecipe(Recipe recipe) {
		ContentValues args = new ContentValues();
		args.put(KEY_NAME, recipe.name);
		args.put(KEY_FAVORITE, recipe.favorite);
		args.put(KEY_COOK_TIME, recipe.cook_time);
		args.put(KEY_SERV_SIZE, recipe.serv_size);
		args.put(KEY_PHOTO_URLS, recipe.photo_urls);
		args.put(KEY_DESC, recipe.desc);

		boolean result = mDb.update(DATABASE_RECIPE_TABLE, args, KEY_ROWID
				+ "=" + recipe.id, null) > 0;

		// FIXME: Need to also update ingredients and steps

		return result;
	}

	/**
	 * Add the ingredients from a given recipe to the global shopping list
	 * 
	 * @param recipe
	 */
	public void addRecipeToShoppingList(Recipe recipe) {
		ContentValues item = new ContentValues();
		for (String ingredient : recipe.ingredients) {
			item.put(KEY_INGREDIENT, ingredient);
			mDb.insert(DATABASE_SHOPPING_LIST_TABLE, null, item);
			item.clear();
		}
	}

	/**
	 * Return a Cursor over the list of shopping list items
	 * 
	 * @return Cursor over all shopping list items
	 */
	public Cursor fetchShoppingList() {
		return mDb.query(DATABASE_SHOPPING_LIST_TABLE, new String[] {
				KEY_ROWID, KEY_INGREDIENT }, null, null, null, null, null);
	}

	/**
	 * Clear the shopping list table of all entries
	 */
	public void clearShoppingList() {
		mDb.delete(DATABASE_SHOPPING_LIST_TABLE, "1 == 1", null);
	}

	public int addRecipeToFavorites(Recipe recipe) {
		ContentValues args = new ContentValues();
		args.put(KEY_FAVORITE, true);
		return mDb.update(DATABASE_RECIPE_TABLE, args, KEY_ROWID + "=" + recipe.id, null);
	}

	public Cursor fetchFavs() {
		Cursor mCursor = mDb.query(true, DATABASE_RECIPE_TABLE, new String[] {
				KEY_ROWID, KEY_NAME}, KEY_FAVORITE + "=" + "1",
				null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	public void clearFavs() {
		ContentValues args = new ContentValues();
		args.put(KEY_FAVORITE, false);
		mDb.update(DATABASE_RECIPE_TABLE, args,KEY_FAVORITE + "=" + "1", null);
	}
}
