package com.andreea.bakingapp.baking_app.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.andreea.bakingapp.baking_app.R;
import com.andreea.bakingapp.baking_app.adapter.WidgetRecipeListAdapter;
import com.andreea.bakingapp.baking_app.callback.OnRecipeItemClickListener;
import com.andreea.bakingapp.baking_app.data.MemoryCache;
import com.andreea.bakingapp.baking_app.model.Recipe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * The configuration screen for the {@link RecipeIngredientsWidget RecipeIngredientsWidget} AppWidget.
 */
public class RecipeIngredientsWidgetConfigureActivity extends Activity {

    @BindView(R.id.widget_config_label) TextView mWidgetConfigLabel;
    @BindView(R.id.appwidget_recipe_list) RecyclerView mRecipesRecyclerView;

    private static final String PREFS_NAME = "com.andreea.bakingapp.baking_app.RecipeIngredientsWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    public RecipeIngredientsWidgetConfigureActivity() {
        super();
    }

    static void saveRecipeIdPref(Context context, int appWidgetId, int recipeId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putInt(PREF_PREFIX_KEY + appWidgetId, recipeId);
        prefs.apply();
    }

    static int loadRecipeIdPref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getInt(PREF_PREFIX_KEY + appWidgetId, 0);
    }

    static void deleteRecipeIdPref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.recipe_ingredients_widget_configure);
        ButterKnife.bind(this);

        WidgetRecipeListAdapter adapter = new WidgetRecipeListAdapter(new OnRecipeItemClickListener() {
            @Override
            public void onClick(View view, int recipeId) {
                final Context context = RecipeIngredientsWidgetConfigureActivity.this;

                // When a recipe is selected, store the recipe id locally
                saveRecipeIdPref(context, mAppWidgetId, recipeId);

                // It is the responsibility of the configuration activity to update the app widget
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                RecipeIngredientsWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

                // Make sure we pass back the original appWidgetId
                Intent resultValue = new Intent();
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
                setResult(RESULT_OK, resultValue);
                finish();
            }
        });

        mRecipesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecipesRecyclerView.setAdapter(adapter);

        List<Recipe> recipeList = MemoryCache.getInstance().getRecipeList();
        if (recipeList == null || recipeList.size() == 0) {
            mWidgetConfigLabel.setText(R.string.open_app);
        }
        adapter.setData(recipeList);
        adapter.notifyDataSetChanged();

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }
    }
}

