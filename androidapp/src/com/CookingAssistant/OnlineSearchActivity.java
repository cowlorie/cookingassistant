package com.CookingAssistant;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class OnlineSearchActivity extends ListActivity {
    private ArrayList<Recipe> recipeList;
	
    /** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.online_search);
        registerForContextMenu(getListView());
    }

	public void search(View view) {
		EditText incEdit = (EditText)findViewById(R.id.incText);
		EditText excEdit = (EditText)findViewById(R.id.excText);
		EditText minEdit = (EditText)findViewById(R.id.minText);
		EditText maxEdit = (EditText)findViewById(R.id.maxText);
		
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(incEdit.getWindowToken(), 0);
		
		String incString = incEdit.getText().toString();
		String excString = excEdit.getText().toString();
		String minString = minEdit.getText().toString();
		String maxString = maxEdit.getText().toString();
		
		/*Log.v("OnlineSearchActivity~incString", incString);
		Log.v("OnlineSearchActivity~excString", excString);
		Log.v("OnlineSearchActivity~minString", minString);
		Log.v("OnlineSearchActivity~maxString", maxString);*/

		String url = new String("http://174.129.31.68/~cookingassistant/recipes/?page=1&");
        //Log.v("OnlineSearchActivity~url", url);
		
		if(!incString.equals("")){
			
		}
		if(!excString.equals("")){
			
		}
		if(!minString.equals("")){
			
		}
		if(!maxString.equals("")){
			
		}
        
		recipeList = new ArrayList<Recipe>();
		
        ArrayAdapter<Recipe> recipes = new ArrayAdapter<Recipe>(this, R.layout.recipes_row, recipeList);
        setListAdapter(recipes);
    }
	
	@Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	super.onListItemClick(l, v, position, id);
		Intent i = new Intent(this, RecipePageActivity.class);
		i.putExtra("recipe", recipeList.get(position));
        startActivity(i);
    }

}
