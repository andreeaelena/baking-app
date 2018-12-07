package com.andreea.bakingapp.baking_app.data;

import com.andreea.bakingapp.baking_app.model.Recipe;
import com.andreea.bakingapp.baking_app.model.Step;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton that is used as an in-memory cache for the Recipes data.
 */
public class MemoryCache {

    private static MemoryCache sInstance;

    private List<Recipe> mRecipeList;

    private MemoryCache() {
        mRecipeList = new ArrayList<>();
    }

    public static MemoryCache getInstance() {
        if (sInstance == null) {
            sInstance = new MemoryCache();
        }
        return sInstance;
    }

    public void setRecipeList(List<Recipe> recipeList) {
        mRecipeList.clear();
        mRecipeList.addAll(recipeList);
    }

    public List<Recipe> getRecipeList() {
        return mRecipeList;
    }

    public Recipe getRecipe(int id) {
        for (Recipe recipe : mRecipeList) {
            if (recipe.getId() == id) {
                return recipe;
            }
        }
        return null;
    }

    public Step getRecipeStep(int recipeId, int stepId) {
        Recipe recipe = getRecipe(recipeId);
        for (Step step : recipe.getSteps()) {
            if (step.getId() == stepId) {
                return step;
            }
        }
        return null;
    }
}
