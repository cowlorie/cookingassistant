package com.CookingAssistant;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class SearchActivity extends ListActivity {
	private RecipeDbAdapter mDbHelper;
	private Cursor mRecipesCursor;
	public static final int INSERT_ID = Menu.FIRST;
    
    /** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        mDbHelper = new RecipeDbAdapter(this);
        mDbHelper.open();
        registerForContextMenu(getListView());
    }

	public void search(View view) {
		EditText mEdit = (EditText)findViewById(R.id.searchText);
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(mEdit.getWindowToken(), 0);

        mRecipesCursor = mDbHelper.searchRecipes(mEdit.getText().toString());
        startManagingCursor(mRecipesCursor);

        String[] from = new String[] { RecipeDbAdapter.KEY_NAME };
        int[] to = new int[] { R.id.text1 };
        
        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter recipes =
            new SimpleCursorAdapter(this, R.layout.recipes_row, mRecipesCursor, from, to);
        setListAdapter(recipes);
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
