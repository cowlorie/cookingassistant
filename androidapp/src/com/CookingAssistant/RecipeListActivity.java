package com.CookingAssistant;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class RecipeListActivity extends ListActivity {
	private RecipeDbAdapter mDbHelper;
	private Cursor mRecipesCursor;
	public static final int INSERT_ID = Menu.FIRST;
    
    /** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_list);
        mDbHelper = new RecipeDbAdapter(this);
        mDbHelper.open();

        fillData();
        registerForContextMenu(getListView());
    }

	private void fillData() {
        // Get all of the Recipe from the database and create the item list
        mRecipesCursor = mDbHelper.fetchAllRecipes();
        startManagingCursor(mRecipesCursor);

        String[] from = new String[] { RecipeDbAdapter.KEY_TITLE };
        int[] to = new int[] { R.id.text1 };
        
        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter recipes =
            new SimpleCursorAdapter(this, R.layout.recipes_row, mRecipesCursor, from, to);
        setListAdapter(recipes);
    }
	
	@Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	super.onListItemClick(l, v, position, id);
        Cursor c = mRecipesCursor;
        c.moveToPosition(position);
        Intent i = new Intent(this, RecipePageActivity.class);
        i.putExtra(RecipeDbAdapter.KEY_ROWID, id);
        startActivity(i);
    }
}
