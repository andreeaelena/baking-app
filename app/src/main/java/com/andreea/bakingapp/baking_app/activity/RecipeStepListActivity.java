package com.andreea.bakingapp.baking_app.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v7.app.ActionBar;

import com.andreea.bakingapp.baking_app.Constants;
import com.andreea.bakingapp.baking_app.R;
import com.andreea.bakingapp.baking_app.adapter.RecipeStepListAdapter;
import com.andreea.bakingapp.baking_app.callback.OnRecipeStepItemClickListener;
import com.andreea.bakingapp.baking_app.data.MemoryCache;
import com.andreea.bakingapp.baking_app.fragment.RecipeStepDetailFragment;
import com.andreea.bakingapp.baking_app.model.Recipe;
import com.andreea.bakingapp.baking_app.model.Step;

import static com.andreea.bakingapp.baking_app.Constants.Extra.SET_INITIAL_FRAGMENT;

/**
 * An activity representing a list of Recipe Steps. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RecipeStepDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RecipeStepListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet.
     */
    private boolean mTwoPane;

    /**
     * Whether or not should preselect the first step and set the fragment (only in two pane mode.
     */
    private boolean shouldSetInitialFragment;

    private Recipe mRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_list);

        shouldSetInitialFragment = savedInstanceState == null || savedInstanceState.getBoolean(SET_INITIAL_FRAGMENT);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Show the up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        int recipeId = getIntent().getIntExtra(Constants.Extra.RECIPE_ID, 0);
        mRecipe = MemoryCache.getInstance().getRecipe(recipeId);

        if (mRecipe != null) {
            setTitle(mRecipe.getName());
            if (findViewById(R.id.recipestep_detail_container) != null) {
                // Two pane mode is set only for tables.
                mTwoPane = true;
            }

            View recyclerView = findViewById(R.id.recipestep_list);
            if (recyclerView != null) {
                setupRecyclerView((RecyclerView) recyclerView, mRecipe);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SET_INITIAL_FRAGMENT, shouldSetInitialFragment);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView, Recipe recipe) {
        RecipeStepListAdapter adapter = new RecipeStepListAdapter(recipe, new RecipeStepItemClickListener());
        recyclerView.setAdapter(adapter);

        // If there are two panes, and the activity was just created the first time,
        // then preselect the first step:
        if (mTwoPane && shouldSetInitialFragment) {
            shouldSetInitialFragment = false;
            showRecipeStepDetailFragment(getSupportFragmentManager(), recipe, recipe.getSteps().get(0));
        }
    }

    /**
     * Loads the RecipeStepDetailFragment for the provided Recipe and Step objects.
     */
    private static void showRecipeStepDetailFragment(FragmentManager fragmentManager, Recipe recipe, Step recipeStep) {
        Bundle arguments = new Bundle();
        arguments.putInt(Constants.Extra.RECIPE_ID, recipe.getId());
        arguments.putInt(Constants.Extra.RECIPE_STEP_ID, recipeStep.getId());

        RecipeStepDetailFragment fragment = new RecipeStepDetailFragment();
        fragment.setArguments(arguments);

        fragmentManager.beginTransaction()
                .replace(R.id.recipestep_detail_container, fragment)
                .commit();
    }

    /**
     * Class that implements View.OnClickListener and is used to listen for item click events.
     */
    class RecipeStepItemClickListener implements OnRecipeStepItemClickListener {
        @Override
        public void onClick(View view, int recipeId, int recipeStepId) {
            Step recipeStep = (Step) view.getTag();
            if (mTwoPane) {
                RecipeStepListActivity.showRecipeStepDetailFragment(getSupportFragmentManager(), mRecipe, recipeStep);
            } else {
                Context context = view.getContext();
                Intent intent = new Intent(context, RecipeStepDetailActivity.class);
                intent.putExtra(Constants.Extra.RECIPE_ID, mRecipe.getId());
                intent.putExtra(Constants.Extra.RECIPE_STEP_ID, recipeStep.getId());
                context.startActivity(intent);
            }
        }
    }
}
