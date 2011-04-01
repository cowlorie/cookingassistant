package com.CookingAssistant;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class RecipePageActivity extends Activity {
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recipe_page);
		
		Bundle extras = getIntent().getExtras();
		String title = extras.getString(RecipeDbAdapter.KEY_TITLE);
        String ingredient = extras.getString(RecipeDbAdapter.KEY_INGREDIENTS);
        String instruction = extras.getString(RecipeDbAdapter.KEY_INSTRUCTIONS);
		
        TextView t = (TextView)findViewById(R.id.titleView);
        t.setText(title);
        TextView t1 = (TextView)findViewById(R.id.ingredientView);
        t1.setText(ingredient);
        TextView t2 = (TextView)findViewById(R.id.instructionView);
        t2.setText(instruction);
	}
}
