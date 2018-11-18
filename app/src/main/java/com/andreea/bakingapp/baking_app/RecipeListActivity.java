package com.andreea.bakingapp.baking_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class RecipeListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        Intent recipeStepListIntent = new Intent(RecipeListActivity.this, RecipeStepListActivity.class);
        startActivity(recipeStepListIntent);
    }
}
