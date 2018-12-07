package com.andreea.bakingapp.baking_app.callback;

import android.view.View;

/**
 * Custom click listener that forwards the Recipe Item click event along with the recipe id.
 */
public interface OnRecipeItemClickListener {
    void onClick(View view, int recipeId);
}
