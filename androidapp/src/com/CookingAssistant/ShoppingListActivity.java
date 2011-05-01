package com.CookingAssistant;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class ShoppingListActivity extends ListActivity {
	private RecipeDbAdapter mDbHelper;

	public static final int CLEAR_ALL_ID = Menu.FIRST;
	public static final int CLEAR_CHECKED_ID = CLEAR_ALL_ID + 1;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_list);
        
        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        
        mDbHelper = new RecipeDbAdapter(this);
        mDbHelper.open();

        fillData();
    }
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        menu.add(0, CLEAR_ALL_ID, 0, R.string.menu_shopping_list_clear);
        menu.add(0, CLEAR_CHECKED_ID, 0, R.string.menu_shopping_list_clear_checked);
        return result;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    	case CLEAR_ALL_ID:
    		mDbHelper.clearShoppingList();
    		fillData();
    		return true;
    	case CLEAR_CHECKED_ID:
    		clearChecked();
    		fillData();
    		return true;
    	}
        return super.onOptionsItemSelected(item);
    }
    
    private void clearChecked() {
		ListView v = getListView();
		long ids[]  = v.getCheckItemIds(); v.getCheckedItemPosition();
		for (long id : ids) {
			mDbHelper.removeShoppingListItem(id);
		}
    }
    
    private void fillData() {
    	// Get all of the shopping list items from the database
    	Cursor c = mDbHelper.fetchShoppingList();
    	startManagingCursor(c);
    	
    	String[] from = new String[] { mDbHelper.KEY_INGREDIENT };
    	int[] to = new int[] { R.id.text2 };
    	
    	// Now create an array adapter and set it to display using our row
    	SimpleCursorAdapter notes =
    		new SimpleCursorAdapter(this, R.layout.shopping_list_row, c, from, to);
    	setListAdapter(notes);
    }
}