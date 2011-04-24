package com.CookingAssistant;

import java.io.InputStream;
import java.net.URL;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class RecipePageActivity extends Activity {
	public static final int SHOPPING_LIST_ID = 1;
	public static final int FAVORITES_ID = 2;

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
		for (String ingredient : recipe.ingredients){
			ingredients += ingredient + "\n";
		}
		String instructions = "";
		int num = 1;
		for (String step : recipe.steps){
			instructions += num + ". " + step + "\n";
			num++;
		}

		TextView t = (TextView) findViewById(R.id.titleView);
		t.setText(title);
		TextView t1 = (TextView) findViewById(R.id.ingredientView);
		t1.setText("Ingredients:\n" + ingredients);
		TextView t2 = (TextView) findViewById(R.id.instructionView);
		t2.setText("Instructions:\n" + instructions);
		TextView t3 = (TextView) findViewById(R.id.descView);
		t3.setText(recipe.desc);
		TextView t4 = (TextView) findViewById(R.id.cooktimeView);
		t4.setText("Cooking Time: " + recipe.cook_time + " Minutes");
		TextView t5 = (TextView) findViewById(R.id.servsizeView);
		t5.setText("Serving Size: " + recipe.serv_size + " People");
		
		ImageView imgView =(ImageView)findViewById(R.id.imageView);
		Drawable drawable = LoadImageFromWebOperations(recipe.photo_urls);
		imgView.setImageDrawable(drawable);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		menu.add(0, SHOPPING_LIST_ID, 0, R.string.menu_shopping_list_add);
		menu.add(0, FAVORITES_ID, 0, R.string.menu_fav_add);
		return result;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case SHOPPING_LIST_ID:
			mDbHelper.addRecipeToShoppingList(recipe);
			return true;
		case FAVORITES_ID:
			mDbHelper.addRecipeToFavorites(recipe);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private Drawable LoadImageFromWebOperations(String url) {
		try {
			InputStream is = (InputStream) new URL(url).getContent();
			Drawable d = Drawable.createFromStream(is, "src name");
			return d;
		} catch (Exception e) {
			System.out.println("Exc=" + e);
			return null;
		}
	}
}
