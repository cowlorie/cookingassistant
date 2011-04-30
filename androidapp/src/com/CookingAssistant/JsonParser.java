package com.CookingAssistant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonParser{
	private String TAG = "JsonParser";
	private RecipeDbAdapter mDbHelper;
	private Gson gson;
	
	public JsonParser(RecipeDbAdapter adapter){
		GsonBuilder gsonb = new GsonBuilder();
		mDbHelper = adapter;
		gson = gsonb.create();
	}
	
	public void parseToDatabase(CharSequence charSequence){
		try {
			mDbHelper.open();
		    URL url = new URL((String) charSequence);	    
		    BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
		    String jString;
		    int count = 0;
		    while ((jString = in.readLine()) != null && count < 40) {
		    	jString = cleanString(jString);
				Recipe r = getRecipe(jString);
				mDbHelper.createRecipe(r);
				count++;
		    }
		    in.close();
		    mDbHelper.close();
		} catch (MalformedURLException e) {
			Log.v(TAG, e.toString());
		} catch (IOException e) {
			Log.v(TAG, e.toString());
		}
	}
	
	public ArrayList<Recipe> parseToArray(CharSequence charSequence){
		try {
		    URL url = new URL((String) charSequence);	    
		    BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
		    String jString = in.readLine();
		    
		    if(jString == null)
		    	return null;
		    
		    ArrayList<Recipe> recipes = new ArrayList<Recipe>();
		    int startString = 0;
		    int endString = 0;
		    while ((startString = jString.indexOf('{')) != -1) {
		    	endString = jString.indexOf('}');
		    	String rString = jString.substring(startString, endString + 1);
		    	jString = jString.substring(endString + 1);
				Recipe r = getRecipe(rString);
				recipes.add(r);
		    }
		    in.close();
		    return recipes;
		} catch (MalformedURLException e) {
			Log.v(TAG, e.toString());
		} catch (IOException e) {
			Log.v(TAG, e.toString());
		}
		return null;
	}
	
	public Recipe parseRecipe(CharSequence charSequence) {
		try {
		    URL url = new URL((String) charSequence);	    
		    BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
		    String jString = in.readLine();
		    
		    if(jString == null)
		    	return null;
		    
		    Log.v("parseRecipe", jString);
		   
		    Recipe r = getRecipe(jString);
		    in.close();
		    return r;
		} catch (MalformedURLException e) {
			Log.v(TAG, e.toString());
		} catch (IOException e) {
			Log.v(TAG, e.toString());
		}
		return null;
	}
	
	public Recipe getRecipe(String jsonString) {
		return gson.fromJson(jsonString, Recipe.class);
	}     
	
	private String cleanString(String s){
		if (s.charAt(0) == '[')
			s = s.substring(1);
		if (s.charAt(s.length() - 1) == ',')
			s = s.substring(0, s.length() - 1);
		return s;
	}
}
