package com.CookingAssistant;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HomeActivity extends Activity {
	
	private ProgressDialog progDailog;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.home);
        
        final Button helpButton = (Button) findViewById(R.id.button2);
        helpButton.setOnClickListener(new View.OnClickListener() {
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
    }  
}