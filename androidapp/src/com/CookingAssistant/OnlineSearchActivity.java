package com.CookingAssistant;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class OnlineSearchActivity extends ListActivity {
	
	private EditText incEdit;
	private EditText excEdit;
	private EditText minEdit;
	private EditText maxEdit;
    private ArrayList<Recipe> recipeList;
    private JsonParser jp;
	
    /** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.online_search1);
        
        incEdit = (EditText)findViewById(R.id.incText);
    	excEdit = (EditText)findViewById(R.id.excText);
    	minEdit = (EditText)findViewById(R.id.minText);
    	maxEdit = (EditText)findViewById(R.id.maxText);
		findViewById(R.id.newSearchButton).setVisibility(View.GONE);

    	jp = new JsonParser(new RecipeDbAdapter(OnlineSearchActivity.this));
    	
        registerForContextMenu(getListView());
    }
	
	
	private void hideViews(){
		findViewById(R.id.incText).setVisibility(View.GONE);
		findViewById(R.id.excText).setVisibility(View.GONE);
		findViewById(R.id.maxText).setVisibility(View.GONE);
		findViewById(R.id.minText).setVisibility(View.GONE);
		findViewById(R.id.incTextView).setVisibility(View.GONE);
		findViewById(R.id.excTextView).setVisibility(View.GONE);
		findViewById(R.id.maxTimeTextView).setVisibility(View.GONE);
		findViewById(R.id.minTimeTextView).setVisibility(View.GONE);
		findViewById(R.id.searchButton).setVisibility(View.GONE);
		findViewById(R.id.newSearchButton).setVisibility(View.VISIBLE);
	}


	public void search(View view) {
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(incEdit.getWindowToken(), 0);
		
		Log.v("searchURL", generateURL());
		recipeList = jp.parseToArray(generateURL());
			
        ArrayAdapter<Recipe> recipes = new ArrayAdapter<Recipe>(this, R.layout.recipes_row, recipeList);       
        hideViews();    
        setListAdapter(recipes);
    }
	
	public void newSearch(View view) {
		findViewById(R.id.incText).setVisibility(View.VISIBLE);
		findViewById(R.id.excText).setVisibility(View.VISIBLE);
		findViewById(R.id.maxText).setVisibility(View.VISIBLE);
		findViewById(R.id.minText).setVisibility(View.VISIBLE);
		findViewById(R.id.incTextView).setVisibility(View.VISIBLE);
		findViewById(R.id.excTextView).setVisibility(View.VISIBLE);
		findViewById(R.id.maxTimeTextView).setVisibility(View.VISIBLE);
		findViewById(R.id.minTimeTextView).setVisibility(View.VISIBLE);
		findViewById(R.id.searchButton).setVisibility(View.VISIBLE);
		findViewById(R.id.newSearchButton).setVisibility(View.GONE);
	}
	
	private String generateURL(){
		String incString = incEdit.getText().toString();
		String excString = excEdit.getText().toString();
		String minString = minEdit.getText().toString();
		String maxString = maxEdit.getText().toString();

		String url = (String) getResources().getText(R.string.base_search_url);

		url += "&inc_keys=" + incString;
		url += "&exc_keys=" + excString;
		url += "&min_time=" + minString;
		url += "&max_time=" + maxString;
		
		url += (String) getResources().getText(R.string.end_search_url);
		
		return url;
	}
	
	@Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	super.onListItemClick(l, v, position, id);
    	//get recipe details from server
    	String url = "http://174.129.31.68/~cookingassistant/recipes/" + 
    				recipeList.get(position).id + "/?format=json";
    	Recipe r = jp.parseRecipe(url);
		Intent i = new Intent(this, RecipePageActivity.class);
		i.putExtra("recipe", r);
        startActivity(i);
    }
	
	

}
