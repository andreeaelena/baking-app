package com.andreea.bakingapp.baking_app.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andreea.bakingapp.baking_app.R;
import com.andreea.bakingapp.baking_app.callback.OnRecipeItemClickListener;
import com.andreea.bakingapp.baking_app.model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class WidgetRecipeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Recipe> mRecipeList = new ArrayList<>();
    private OnRecipeItemClickListener mOnItemClickListener;

    public WidgetRecipeListAdapter(OnRecipeItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View recipeView = inflater.inflate(R.layout.appwidget_recipe_item, parent, false);
        return new WidgetRecipeListAdapter.WidgetRecipeViewHolder(recipeView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final Recipe currentRecipe = mRecipeList.get(position);

        WidgetRecipeListAdapter.WidgetRecipeViewHolder recipeHolder = (WidgetRecipeListAdapter.WidgetRecipeViewHolder) holder;
        recipeHolder.mRecipeName.setText(currentRecipe.getName());
        recipeHolder.mRecipeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onClick(v, currentRecipe.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mRecipeList.size();
    }

    public void setData(List<Recipe> recipeList) {
        mRecipeList.clear();
        mRecipeList.addAll(recipeList);
    }

    static class WidgetRecipeViewHolder extends RecyclerView.ViewHolder {
        private View mRecipeView;
        private TextView mRecipeName;

        WidgetRecipeViewHolder(View itemView) {
            super(itemView);
            mRecipeView = itemView;
            mRecipeName = itemView.findViewById(R.id.recipe_name);
        }
    }
}
