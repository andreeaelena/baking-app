package com.andreea.bakingapp.baking_app.activity;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.andreea.bakingapp.baking_app.Constants;
import com.andreea.bakingapp.baking_app.R;
import com.andreea.bakingapp.baking_app.adapter.RecipeListAdapter;
import com.andreea.bakingapp.baking_app.callback.OnRecipeItemClickListener;
import com.andreea.bakingapp.baking_app.data.MemoryCache;
import com.andreea.bakingapp.baking_app.model.Recipe;
import com.andreea.bakingapp.baking_app.network.RecipesApi;
import com.andreea.bakingapp.baking_app.network.RetrofitClientInstance;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * An activity representing a list of Recipes. This activity has different presentations
 * for handset and tablet-size devices. On handsets, the activity presents a list of items,
 * and on tablets, the activity presents the recipes as a grid view.
 */
public class RecipeListActivity extends AppCompatActivity {

    @BindView(R.id.loading_view) View mLoadingView;
    @BindView(R.id.recipe_list) RecyclerView mRecipesRecyclerView;
    @BindView(R.id.no_data_view) View mNoDataView;
    @BindView(R.id.retry_button) Button mRetryButton;

    private RecipeListAdapter mRecipeListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        ButterKnife.bind(this);

        int numberOfColumns = getResources().getInteger(R.integer.grid_columns_count);
        mRecipesRecyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));

        mRecipeListAdapter = new RecipeListAdapter(this, new OnRecipeItemClickListener() {
            @Override
            public void onClick(View view, int recipeId) {
                Intent recipeStepListIntent = new Intent(RecipeListActivity.this, RecipeStepListActivity.class);
                recipeStepListIntent.putExtra(Constants.Extra.RECIPE_ID, recipeId);
                startActivity(recipeStepListIntent);
            }
        });
        mRecipesRecyclerView.setAdapter(mRecipeListAdapter);

        mRetryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });

        getData();
    }

    private void getData() {
        mLoadingView.setVisibility(View.VISIBLE);
        mNoDataView.setVisibility(View.GONE);

        RecipesApi recipesApi = RetrofitClientInstance.getInstance().create(RecipesApi.class);
        Call<List<Recipe>> recipesCall = recipesApi.getRecipes();
        recipesCall.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(@NonNull Call<List<Recipe>> call, @NonNull Response<List<Recipe>> response) {
                mLoadingView.setVisibility(View.GONE);
                mRecipesRecyclerView.setVisibility(View.VISIBLE);

                List<Recipe> recipeList = response.body();
                MemoryCache.getInstance().setRecipeList(recipeList);
                mRecipeListAdapter.setData(recipeList);
                mRecipeListAdapter.notifyDataSetChanged();

                Intent widgetUpdateIntent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                sendBroadcast(widgetUpdateIntent);
            }

            @Override
            public void onFailure(@NonNull Call<List<Recipe>> call, @NonNull Throwable t) {
                mLoadingView.setVisibility(View.GONE);
                mNoDataView.setVisibility(View.VISIBLE);
            }
        });
    }
}
