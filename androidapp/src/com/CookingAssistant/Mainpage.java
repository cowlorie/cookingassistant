package com.CookingAssistant;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;
import android.app.TabActivity;

public class Mainpage extends TabActivity {
    /** Called when the activity is first created. */
	
	TabHost tabHost;
	
    @Override  
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Resources res = getResources(); // Resource object to get Drawables
        tabHost = getTabHost();  // The activity TabHost
        TabHost.TabSpec spec;  // Resusable TabSpec for each tab
        Intent intent;  // Reusable Intent for each tab

        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent().setClass(this, HomeActivity.class);

        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("Home").setIndicator("Home",
                          res.getDrawable(R.drawable.home))
                      .setContent(intent);
        tabHost.addTab(spec);

        // Do the same for the other tabs
        intent = new Intent().setClass(this, SearchActivity.class);
        spec = tabHost.newTabSpec("Search").setIndicator("Search",
                          res.getDrawable(R.drawable.search))
                      .setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, ShoppingListActivity.class);
        spec = tabHost.newTabSpec("ShoppingList").setIndicator("ShoppingList",
                          res.getDrawable(R.drawable.list))
                      .setContent(intent);
        tabHost.addTab(spec);
        
        intent = new Intent().setClass(this, FavoritesActivity.class);
        spec = tabHost.newTabSpec("Favorites").setIndicator("Favorites",
                          res.getDrawable(R.drawable.star))
                      .setContent(intent);
        tabHost.addTab(spec);

        tabHost.setCurrentTab(0);
    }
    
    public void switchTab(int tab){
        tabHost.setCurrentTab(tab);
    }
}