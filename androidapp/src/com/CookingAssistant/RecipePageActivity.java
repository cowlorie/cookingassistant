package com.CookingAssistant;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class RecipePageActivity extends Activity {
	public static final int SHOPPING_LIST_ID = Menu.FIRST;
	RecipeDbAdapter mDbHelper;
	Recipe recipe;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recipe_page);
		
		Bundle extras = getIntent().getExtras();
		long recipeId = extras.getLong(RecipeDbAdapter.KEY_ROWID);
		
		mDbHelper = new RecipeDbAdapter(this);
	    mDbHelper.open();
	    recipe = mDbHelper.fetchRecipe(recipeId);
	    
	    String title = recipe.name;
	    String ingredients = "";
	    for (String ingredient : recipe.ingredients)
	    	ingredients += ingredient + "\n";
	    String instructions = "";
	    for (String step : recipe.steps)
	    	instructions += step + "\n";
	    
        TextView t = (TextView)findViewById(R.id.titleView);
        t.setText(title);
        TextView t1 = (TextView)findViewById(R.id.ingredientView);
        t1.setText(ingredients);
        TextView t2 = (TextView)findViewById(R.id.instructionView);
        t2.setText(instructions);
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        menu.add(0, SHOPPING_LIST_ID, 0, R.string.menu_shopping_list_add);
        return result;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {	    
    	switch (item.getItemId()) {
    	case SHOPPING_LIST_ID:
    		mDbHelper.addRecipeToShoppingList(recipe);
    		return true;
    	}
    	
        return super.onOptionsItemSelected(item);
    }
}
