package com.andreea.bakingapp.baking_app.activity;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.andreea.bakingapp.baking_app.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class RecipeStepDetailActivityTest {

    @Rule
    public ActivityTestRule<RecipeListActivity> mActivityRule = new ActivityTestRule<>(RecipeListActivity.class);

    @Test
    public void ensureRecipeStepDetailActivityLaunches() {
        onView(withId(R.id.recipe_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.recipestep_list)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        onView(withId(R.id.recipe_step_tab_layout)).check(matches(isDisplayed()));
    }
}