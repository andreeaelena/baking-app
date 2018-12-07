package com.andreea.bakingapp.baking_app.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.andreea.bakingapp.baking_app.Constants;
import com.andreea.bakingapp.baking_app.R;
import com.andreea.bakingapp.baking_app.fragment.RecipeStepDetailFragment;
import com.andreea.bakingapp.baking_app.model.Recipe;
import com.andreea.bakingapp.baking_app.model.Step;

/**
 * ViewPager Adapter used to display the Recipe Step details fragments.
 */
public class RecipeStepPagerAdapter extends FragmentStatePagerAdapter {

    private Context mContext;
    private Recipe mRecipe;

    public RecipeStepPagerAdapter(FragmentManager fm, Context context, Recipe recipe) {
        super(fm);
        mContext = context;
        mRecipe = recipe;
    }

    @Override
    public Fragment getItem(int position) {
        Step recipeStep = mRecipe.getSteps().get(position);

        Bundle arguments = new Bundle();
        arguments.putInt(Constants.Extra.RECIPE_ID, mRecipe.getId());
        arguments.putInt(Constants.Extra.RECIPE_STEP_ID, recipeStep.getId());
        RecipeStepDetailFragment fragment = new RecipeStepDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public int getCount() {
        return mRecipe.getSteps().size();
    }

    public View getTabView(int position) {
        View tab = LayoutInflater.from(mContext).inflate(R.layout.recipe_step_item_tab, null);
        TextView stepNameTextView = tab.findViewById(R.id.recipe_step_name);
        stepNameTextView.setText(mRecipe.getSteps().get(position).getShortDescription());
        return tab;
    }
}
