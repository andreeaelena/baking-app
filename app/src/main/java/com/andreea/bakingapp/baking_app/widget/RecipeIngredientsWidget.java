package com.andreea.bakingapp.baking_app.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.andreea.bakingapp.baking_app.R;
import com.andreea.bakingapp.baking_app.data.MemoryCache;
import com.andreea.bakingapp.baking_app.model.Recipe;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeIngredientsWidget extends AppWidgetProvider {

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        int recipeId = RecipeIngredientsWidgetConfigureActivity.loadRecipeIdPref(context, appWidgetId);
        Recipe recipe = MemoryCache.getInstance().getRecipe(recipeId);
        if (recipe != null) {
            // Construct the RemoteViews object
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_ingredients_widget);
            views.setTextViewText(R.id.appwidget_recipe_name, recipe.getName());
            views.setTextViewText(R.id.appwidget_ingredients_text, recipe.getFormattedIngredients());

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            RecipeIngredientsWidgetConfigureActivity.deleteRecipeIdPref(context, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String intentAction = intent.getAction();
        if (intentAction != null) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, RecipeIngredientsWidget.class));
            for (int appWidgetId : appWidgetIds) {
                updateAppWidget(context, appWidgetManager, appWidgetId);
            }
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

