package com.CookingAssistant;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends Activity {
	
	private ProgressDialog progDailog;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.home);
       
        
        final Button syncButton = (Button) findViewById(R.id.sync_btn);
        final Button acctButton = (Button) findViewById(R.id.account_btn);
        final Button settingsButton = (Button) findViewById(R.id.settings_btn);
        final Button searchButton = (Button) findViewById(R.id.search_btn);
        Typeface type=Typeface.createFromAsset(getAssets(), "CaviarDreams_Bold.ttf");
        syncButton.setTypeface(type);
        acctButton.setTypeface(type);
        settingsButton.setTypeface(type);
        searchButton.setTypeface(type);
        
        syncButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	progDailog = ProgressDialog.show(HomeActivity.this,	"Synchronizing recipes", "Please wait...", true);
            	new Thread() {
            		public void run() {
		        		try{
		        			JsonParser jp = new JsonParser(new RecipeDbAdapter(HomeActivity.this));
		                	jp.parse();
		        		} 
		        		catch (Exception e) { }
		           		progDailog.dismiss(); 
            		}
		        }.start();
            }
        });
        
        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent myIntent = new Intent().setClass(v.getContext(), OnlineSearchActivity.class);
                startActivity(myIntent);
            }
        });
    }  
}