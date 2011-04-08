package com.CookingAssistant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonParser{
	private String TAG = "JsonParser";
	private RecipeDbAdapter mDbHelper;
	
	public JsonParser(RecipeDbAdapter adapter){
		mDbHelper = adapter;
		mDbHelper.open();
	}
	
	public void parse(){
		try {
		    URL url = new URL("http://174.129.31.68/~cookingassistant/allrecipes.js");	    
		    BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
		    String jString;
		    int count = 0;
		    
		    while ((jString = in.readLine()) != null && count < 100) {
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
	
	private Recipe getRecipe(String jsonString) {
		Recipe r;
		GsonBuilder gsonb = new GsonBuilder();
		Gson gson = gsonb.create();
		r = gson.fromJson(jsonString, Recipe.class);
		return r;
	}     
	
	private String cleanString(String s){
		if (s.charAt(0) == '[')
			s = s.substring(1);
		if (s.charAt(s.length() - 1) == ',')
			s = s.substring(0, s.length() - 1);
		return s;
	}
}