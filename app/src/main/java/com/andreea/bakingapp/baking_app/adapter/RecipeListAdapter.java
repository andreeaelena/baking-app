package com.andreea.bakingapp.baking_app.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andreea.bakingapp.baking_app.R;
import com.andreea.bakingapp.baking_app.callback.OnRecipeItemClickListener;
import com.andreea.bakingapp.baking_app.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView Adapter used to display a list/grid of Recipes.
 */
public class RecipeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Recipe> mRecipeList = new ArrayList<>();
    private OnRecipeItemClickListener mOnItemClickListener;
    private Context mContext;

    public RecipeListAdapter(Context context, OnRecipeItemClickListener onItemClickListener) {
        mContext = context;
        mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View recipeView = inflater.inflate(R.layout.recipe_item, parent, false);
        return new RecipeViewHolder(recipeView);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final Recipe currentRecipe = mRecipeList.get(position);
        String servingsText = String.format(mContext.getString(R.string.servings_text), currentRecipe.getServings());

        RecipeViewHolder recipeHolder = (RecipeViewHolder) holder;
        recipeHolder.mRecipeText.setText(currentRecipe.getName());
        recipeHolder.mServingsText.setText(servingsText);
        recipeHolder.mRecipeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onClick(v, currentRecipe.getId());
            }
        });

        // Download the image using Picasso
        String imageUrl = currentRecipe.getImage();
        if (!TextUtils.isEmpty(imageUrl)) {
            Picasso.get()
                    .load(imageUrl)
                    .placeholder(R.drawable.restaurant_icon)
                    .into(recipeHolder.mRecipeImage);
        }
    }

    @Override
    public int getItemCount() {
        return mRecipeList.size();
    }

    public void setData(List<Recipe> recipeList) {
        mRecipeList = recipeList;
    }

    static class RecipeViewHolder extends RecyclerView.ViewHolder {
        private View mRecipeView;
        private ImageView mRecipeImage;
        private TextView mRecipeText;
        private TextView mServingsText;

        RecipeViewHolder(View itemView) {
            super(itemView);
            mRecipeView = itemView;
            mRecipeImage = itemView.findViewById(R.id.recipe_image);
            mRecipeText = itemView.findViewById(R.id.recipe_name_view);
            mServingsText = itemView.findViewById(R.id.servings_view);
        }
    }
}
