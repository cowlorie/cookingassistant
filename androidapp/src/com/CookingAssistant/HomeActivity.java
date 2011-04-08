package com.CookingAssistant;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HomeActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.home);
        
        final Button recipeButton = (Button) findViewById(R.id.button1);
        recipeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent myIntent = new Intent().setClass(v.getContext(), RecipeListActivity.class);
                startActivity(myIntent);
            }
        });
        
        final Button helpButton = (Button) findViewById(R.id.button2);
        helpButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	JsonParser jp = new JsonParser(new RecipeDbAdapter(HomeActivity.this));
            	jp.parse();
            	
            	Dialog dialog = new Dialog(HomeActivity.this);
            	dialog.setContentView(R.layout.sync_dialog);
                dialog.setTitle("Synchronization Complete");
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);
                TextView text = (TextView) dialog.findViewById(R.id.textView1);
                text.setText("New Recipes Added");
              /*  
                Button button = (Button) dialog.findViewById(R.id.button1);
                button.setOnClickListener(new View.OnClickListener() {
                @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
                */
                dialog.show(); 
            }
        });
    }
}