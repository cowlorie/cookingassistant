package com.CookingAssistant;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class Mainpage extends TabActivity {
    /** Called when the activity is first created. */
	
	TabHost mTabHost;
	
    @Override  
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.main);
    	mTabHost = (TabHost) findViewById(android.R.id.tabhost);
    	mTabHost.getTabWidget().setDividerDrawable(R.drawable.tab_divider);
    	
    	Intent intent;
    	
    	intent = new Intent().setClass(this, HomeActivity.class);
    	setupTab(new TextView(this), "Home", intent, R.drawable.home);
    	
    	intent = new Intent().setClass(this, RecipeListActivity.class);
    	setupTab(new TextView(this), "Recipes", intent, R.drawable.food);
    	
    	intent = new Intent().setClass(this, SearchActivity.class);
    	setupTab(new TextView(this), "Search", intent, R.drawable.search);
    	
    	intent = new Intent().setClass(this, ShoppingListActivity.class);
    	setupTab(new TextView(this), "Shop", intent, R.drawable.list);
    	
    	intent = new Intent().setClass(this, FavoritesActivity.class);
    	setupTab(new TextView(this), "Favorites", intent, R.drawable.star);
    }
    
    private void setupTab(final View view, final String tag, Intent intent, int iconResource) {
    	View tabview = createTabView(mTabHost.getContext(), tag, iconResource);
        TabSpec setContent = mTabHost.newTabSpec(tag).setIndicator(tabview).setContent(intent);
    	mTabHost.addTab(setContent);
    }

    private static View createTabView(final Context context, final String text, int iconResource) {
    	View view = LayoutInflater.from(context).inflate(R.layout.tabs_bg, null);
    	TextView tv = (TextView) view.findViewById(R.id.tabsText);
    	tv.setText(text);
    	ImageView icon = (ImageView) view.findViewById(R.id.tabsIcon);
		icon.setImageResource(iconResource);
    	return view;
    }

}