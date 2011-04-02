package com.CookingAssistant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class RecipeDbAdapter {

	public static final String KEY_TITLE = "title";
    public static final String KEY_INGREDIENTS = "ingredients";
    public static final String KEY_INSTRUCTIONS = "instructions";
    public static final String KEY_ROWID = "_id";

    private static final String TAG = "RecipeDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    
    /**
     * Database creation sql statement
     */
    private static final String DATABASE_CREATE =
            "create table recipes ("
    		+ "_id integer primary key autoincrement, "
            + "title text not null, "
            + "ingredients text not null, "
            + "instructions text not null);";

    private static final String DATABASE_NAME = "data";
    public static final String DATABASE_TABLE = "recipes";
    private static final int DATABASE_VERSION = 5;

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
            db.execSQL("INSERT INTO "
                    + DATABASE_TABLE
                    + " (title, ingredients, instructions)"
                    + " VALUES ('First Recipe', '1 sugar\n2 salt\nwater', 'mix sugar with salt\nadd water');");
            db.execSQL("INSERT INTO "
                    + DATABASE_TABLE
                    + " (title, ingredients, instructions)"
                    + " VALUES ('Second Recipe', 'water noodles', 'boil water add noodles/neat');");
            db.execSQL("INSERT INTO "
                    + DATABASE_TABLE
                    + " (title, ingredients, instructions)"
                    + " VALUES ('Third Recipe', 'two licorice', 'eat and chew a lot');");
            db.execSQL("INSERT INTO "
                    + DATABASE_TABLE
                    + " (title, ingredients, instructions)"
                    + " VALUES ('Fourth Recipe', 'steak', 'buy steak');");
            db.execSQL("INSERT INTO "
                    + DATABASE_TABLE
                    + " (title, ingredients, instructions)"
                    + " VALUES ('Fifth Recipe', 'skittles', 'taste the rainbow');");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
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
        return this;
    }
    
    public void close() {
        mDbHelper.close();
    }


    /**
     * Create a new recipe using the title and body provided. If the recipe is
     * successfully created return the new rowId for that recipe, otherwise return
     * a -1 to indicate failure.
     * 
     * @param title the title of the recipe
     * @param body the body of the recipe
     * @return rowId or -1 if failed
     */
    public long createRecipe(String title, String ingredients, String instructions) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TITLE, title);
        initialValues.put(KEY_INGREDIENTS, ingredients);
        initialValues.put(KEY_INSTRUCTIONS, instructions);

        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    /**
     * Delete the recipe with the given rowId
     * 
     * @param rowId id of recipe to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteRecipe(long rowId) {

        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    /**
     * Return a Cursor over the list of all recipes in the database
     * 
     * @return Cursor over all recipes
     */
    public Cursor fetchAllRecipes() {

        return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_TITLE,
                KEY_INGREDIENTS, KEY_INSTRUCTIONS}, null, null, null, null, null);
    }

    /**
     * Return a Cursor positioned at the recipe that matches the given rowId
     * 
     * @param rowId id of recipe to retrieve
     * @return Cursor positioned to matching recipe, if found
     * @throws SQLException if recipe could not be found/retrieved
     */
    public Cursor fetchRecipe(long rowId) throws SQLException {
        Cursor mCursor =
                mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                        KEY_TITLE, KEY_INGREDIENTS, KEY_INSTRUCTIONS}, KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    
    public Cursor searchRecipes(String s) {
    	Cursor mCursor =
            mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                    KEY_TITLE, KEY_INGREDIENTS, KEY_INSTRUCTIONS}, KEY_TITLE + " LIKE " + '?', 
                    new String[]{"%" + s + "%"},
                    null, null, null, null);
    	if (mCursor != null) {
    		mCursor.moveToFirst();
    	}
    	return mCursor;
    }

    /**
     * Update the recipe using the details provided. The recipe to be updated is
     * specified using the rowId, and it is altered to use the title and body
     * values passed in
     * 
     * @param rowId id of recipe to update
     * @param title value to set recipe title to
     * @param body value to set recipe body to
     * @return true if the recipe was successfully updated, false otherwise
     */
    public boolean updaterecipe(long rowId, String title, String ingredients, String instructions) {
        ContentValues args = new ContentValues();
        args.put(KEY_TITLE, title);
        args.put(KEY_INGREDIENTS, ingredients);
        args.put(KEY_INSTRUCTIONS, instructions);

        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }
}
