package com.CookingAssistant;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleCursorAdapter;

public class RecipeListActivity extends ListActivity {
	private RecipeDbAdapter mDbHelper;
	private Cursor mRecipesCursor;
	private RadioButton nameOpt, timeOpt;
	public static final int SORT_BY_NAME = 0;
	public static final int SORT_BY_TIME = 1;
	public static final int INSERT_ID = Menu.FIRST;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recipe_list);
		mDbHelper = new RecipeDbAdapter(this);
		mDbHelper.open();

		nameOpt = (RadioButton) findViewById(R.id.nameBtn);
		timeOpt = (RadioButton) findViewById(R.id.timeBtn);
		nameOpt.setOnClickListener(myOptionOnClickListener);
		timeOpt.setOnClickListener(myOptionOnClickListener);
		nameOpt.setChecked(true);

		registerForContextMenu(getListView());
	}

	public void onResume() {
		super.onResume();
		Log.v("RecipeList", "onRestart()");
		if (nameOpt.isChecked()) {
			fillData(SORT_BY_NAME);
		} else {
			fillData(SORT_BY_TIME);
		}
	}

	RadioButton.OnClickListener myOptionOnClickListener = new RadioButton.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (nameOpt.isChecked()) {
				fillData(SORT_BY_NAME);
			} else {
				fillData(SORT_BY_TIME);
			}
		}
	};

	private void fillData(int type) {
		// Get all of the Recipe from the database and create the item list
		mRecipesCursor = mDbHelper.fetchAllRecipes(type);
		startManagingCursor(mRecipesCursor);

		String[] from = new String[] { RecipeDbAdapter.KEY_NAME };
		int[] to = new int[] { R.id.text1 };

		// Now create an array adapter and set it to display using our row
		SimpleCursorAdapter recipes = new SimpleCursorAdapter(this,
				R.layout.recipes_row, mRecipesCursor, from, to);
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
