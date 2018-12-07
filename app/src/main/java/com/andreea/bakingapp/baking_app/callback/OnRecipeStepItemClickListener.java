package com.andreea.bakingapp.baking_app.callback;

import android.view.View;

/**
 * Custom click listener that forwards the Recipe Step Item click event along with the
 * recipe id and step id.
 */
public interface OnRecipeStepItemClickListener {
    void onClick(View view, int recipeId, int recipeStepId);
}
