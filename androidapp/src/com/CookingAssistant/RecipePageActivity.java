package com.CookingAssistant;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class RecipePageActivity extends Activity {
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recipe_page);
		
		Bundle extras = getIntent().getExtras();
		long recipeId = extras.getLong(RecipeDbAdapter.KEY_ROWID);
		
		RecipeDbAdapter mDbHelper = new RecipeDbAdapter(this);
	    mDbHelper.open();
	    Recipe recipe = mDbHelper.fetchRecipe(recipeId);
	    mDbHelper.close();
	    
	    String title = recipe.title;
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
	
}
