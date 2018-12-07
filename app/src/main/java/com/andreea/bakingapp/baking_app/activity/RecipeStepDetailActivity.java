package com.andreea.bakingapp.baking_app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;

import com.andreea.bakingapp.baking_app.Constants;
import com.andreea.bakingapp.baking_app.R;
import com.andreea.bakingapp.baking_app.adapter.RecipeStepPagerAdapter;
import com.andreea.bakingapp.baking_app.data.MemoryCache;
import com.andreea.bakingapp.baking_app.model.Recipe;
import com.andreea.bakingapp.baking_app.model.Step;

import static com.andreea.bakingapp.baking_app.Constants.Extra.RECIPE_ID;
import static com.andreea.bakingapp.baking_app.Constants.Extra.RECIPE_STEP_ID;

/**
 * An activity representing a single Recipe Step detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link RecipeStepListActivity}.
 */
public class RecipeStepDetailActivity extends AppCompatActivity {

    private int mRecipeId;
    private int mRecipeStepId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_detail);

        AppBarLayout appBarLayout = findViewById(R.id.app_bar);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar:
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Bundle arguments = new Bundle();
        mRecipeId = getIntent().getIntExtra(RECIPE_ID, 0);
        mRecipeStepId = getIntent().getIntExtra(RECIPE_STEP_ID, 0);
        arguments.putInt(RECIPE_ID, mRecipeId);
        arguments.putInt(RECIPE_STEP_ID, mRecipeStepId);

        final Recipe recipe = MemoryCache.getInstance().getRecipe(mRecipeId);
        Step recipeStep = MemoryCache.getInstance().getRecipeStep(mRecipeId, mRecipeStepId);

        setTitle(recipe.getName());

        // Setup the Fragment ViewPager:
        ViewPager viewPager = findViewById(R.id.recipe_step_pager);
        RecipeStepPagerAdapter pagerAdapter = new RecipeStepPagerAdapter(getSupportFragmentManager(), this, recipe);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(recipe.getStepIndex(recipeStep));

        TabLayout tabLayout = findViewById(R.id.recipe_step_tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        // Iterate over all tabs and set the custom view
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(pagerAdapter.getTabView(i));
            }
        }

        // Determine what to show on the screen based on the device's orientation and available data:
        boolean isPhoneLandscape = getResources().getBoolean(R.bool.is_phone_landscape);
        if (isPhoneLandscape) {
            appBarLayout.setVisibility(View.GONE);
            tabLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent recipeStepListIntent = new Intent(this, RecipeStepListActivity.class);
            recipeStepListIntent.putExtra(Constants.Extra.RECIPE_ID, mRecipeId);
            navigateUpTo(recipeStepListIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
