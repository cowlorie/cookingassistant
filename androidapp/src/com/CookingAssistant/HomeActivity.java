package com.CookingAssistant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
            }
        });
    }
}