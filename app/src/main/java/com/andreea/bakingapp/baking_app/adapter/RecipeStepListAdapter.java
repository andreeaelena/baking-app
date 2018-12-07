package com.andreea.bakingapp.baking_app.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andreea.bakingapp.baking_app.R;
import com.andreea.bakingapp.baking_app.callback.OnRecipeStepItemClickListener;
import com.andreea.bakingapp.baking_app.model.Recipe;
import com.andreea.bakingapp.baking_app.model.Step;

import java.util.Locale;

/**
 * RecyclerView Adapter used to display a list/grid of Recipe Steps.
 */
public class RecipeStepListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_TYPE_INGREDIENTS = -1;
    private static final int ITEM_TYPE_RECIPE = 1;

    private final Recipe mRecipe;
    private final OnRecipeStepItemClickListener mOnItemClickListener;

    public RecipeStepListAdapter(Recipe recipe, OnRecipeStepItemClickListener onItemClickListener) {
        mRecipe = recipe;
        mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder holder;

        if (viewType == ITEM_TYPE_RECIPE) {
            View view = inflater.inflate(R.layout.recipe_step_item, parent, false);
            holder = new RecipeStepListAdapter.RecipeViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.recipe_ingredients, parent, false);
            holder = new RecipeStepListAdapter.IngredientsViewHolder(view);
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == ITEM_TYPE_RECIPE) {
            RecipeViewHolder recipeViewHolder = (RecipeViewHolder) holder;
            recipeViewHolder.mIdView.setText(String.format(Locale.getDefault(), "%d. ", position - 1));
            Step recipeStep = mRecipe.getSteps().get(position - 1);
            recipeViewHolder.mContentView.setText(recipeStep.getShortDescription());
            recipeViewHolder.itemView.setTag(recipeStep);
            recipeViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Step recipeStep = (Step) v.getTag();
                    mOnItemClickListener.onClick(v, mRecipe.getId(), recipeStep.getId());
                }
            });
        } else {
            IngredientsViewHolder ingredientsViewHolder = (IngredientsViewHolder) holder;
            ingredientsViewHolder.mIngredients.setText(mRecipe.getFormattedIngredients());
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0) ? ITEM_TYPE_INGREDIENTS : ITEM_TYPE_RECIPE;
    }

    @Override
    public int getItemCount() {
        return mRecipe.getSteps().size() + 1; // For the Ingredients item
    }

    /**
     * ViewHolder class that holds a reference to the ingredients view.
     */
    class IngredientsViewHolder extends RecyclerView.ViewHolder {
        final TextView mIngredients;

        IngredientsViewHolder(View view) {
            super(view);
            mIngredients = view.findViewById(R.id.ingredients);
        }
    }

    /**
     * ViewHolder class that holds a reference to the recipe view.
     */
    class RecipeViewHolder extends RecyclerView.ViewHolder {
        final TextView mIdView;
        final TextView mContentView;

        RecipeViewHolder(View view) {
            super(view);
            mIdView = view.findViewById(R.id.id_text);
            mContentView = view.findViewById(R.id.content);
        }
    }
}
