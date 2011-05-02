package com.CookingAssistant;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class HomeActivity extends Activity {
	
	private ProgressDialog progDailog;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.home);
       
        
        final Button syncButton = (Button) findViewById(R.id.sync_btn);
        final Button acctButton = (Button) findViewById(R.id.account_btn);
        final Button searchButton = (Button) findViewById(R.id.search_btn);
        final Button aboutButton = (Button) findViewById(R.id.about_btn);
        
        Typeface type=Typeface.createFromAsset(getAssets(), "CaviarDreams_Bold.ttf");
        
        syncButton.setTypeface(type);
        acctButton.setTypeface(type);
        searchButton.setTypeface(type);
        aboutButton.setTypeface(type);
        
        syncButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	progDailog = ProgressDialog.show(HomeActivity.this,	"Synchronizing recipes", "Please wait...", true);
            	new Thread() {
            		public void run() {
		        		try{
		        			JsonParser jp = new JsonParser(new RecipeDbAdapter(HomeActivity.this));
		                	jp.parseToDatabase(getResources().getText(R.string.allrecipes_url));
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
        
        aboutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Dialog dialog = new Dialog(HomeActivity.this);
            	dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            	dialog.setCanceledOnTouchOutside(true);
            	dialog.setContentView(R.layout.custom_dialog);
            	dialog.show();
            }
        });
    }  
}