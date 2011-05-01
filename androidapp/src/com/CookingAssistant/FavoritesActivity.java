package com.CookingAssistant;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class FavoritesActivity extends ListActivity {
	private RecipeDbAdapter mDbHelper;
	private Cursor mRecipesCursor;

	public static final int CLEAR_ID = Menu.FIRST;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fav_list);
        
        registerForContextMenu(getListView());

        mDbHelper = new RecipeDbAdapter(this);
        mDbHelper.open();

        fillData();
    }
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        menu.add(0, CLEAR_ID, 0, R.string.menu_fav_clear);
        return result;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    	case CLEAR_ID:
    		mDbHelper.clearFavs();
    		fillData();
    		return true;
    	}
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
    	super.onCreateContextMenu(menu, v, menuInfo);
    	menu.add("Remove Favorite");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
    	mDbHelper.deleteRecipe(info.id);
    	fillData();
    	return true;
    }
    
    private void fillData() {
    	// Get all of the shopping list items from the database
    	mRecipesCursor = mDbHelper.fetchFavs();
    	startManagingCursor(mRecipesCursor);
    	
    	String[] from = new String[] { mDbHelper.KEY_NAME };
    	int[] to = new int[] { R.id.text1 };
    	
    	// Now create an array adapter and set it to display using our row
    	SimpleCursorAdapter notes =
    		new SimpleCursorAdapter(this, R.layout.recipes_row, mRecipesCursor, from, to);
    	setListAdapter(notes);
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	super.onListItemClick(l, v, position, id);
		Recipe recipe = mDbHelper.fetchRecipe(id);
		Intent i = new Intent(this, RecipePageActivity.class);
		i.putExtra("recipe", recipe);
        startActivity(i);
    }
}