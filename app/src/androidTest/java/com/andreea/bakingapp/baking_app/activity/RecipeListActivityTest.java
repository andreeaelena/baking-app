package com.andreea.bakingapp.baking_app.activity;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.andreea.bakingapp.baking_app.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;

import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.not;

@RunWith(AndroidJUnit4.class)
public class RecipeListActivityTest {

    @Rule
    public ActivityTestRule<RecipeListActivity> mActivityRule = new ActivityTestRule<>(RecipeListActivity.class);

    @Test
    public void ensureRecipesLoad() {
        onView(withId(R.id.loading_view)).check(matches(not(isDisplayed())));
    }
}