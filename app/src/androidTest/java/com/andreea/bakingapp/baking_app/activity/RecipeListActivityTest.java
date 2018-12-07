package com.andreea.bakingapp.baking_app.activity;

import android.content.Intent;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.andreea.bakingapp.baking_app.R;
import com.andreea.bakingapp.baking_app.network.RetrofitClientInstance;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static android.support.test.InstrumentationRegistry.getContext;
import static android.support.test.espresso.Espresso.onView;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.not;

@RunWith(AndroidJUnit4.class)
public class RecipeListActivityTest {

    @Rule
    public ActivityTestRule<RecipeListActivity> mActivityRule = new ActivityTestRule<>(
            RecipeListActivity.class, true, false);

    /**
     * Mock server used to avoid making actual network calls while testing.
     */
    private MockWebServer mMockServer;

    @Before
    public void setUp() throws Exception {
        mMockServer = new MockWebServer();
        mMockServer.start();
    }

    @Test
    public void ensureRecipesLoad() throws Exception {
        String fileName = "recipes_successful_response_mock.json";
        RetrofitClientInstance.BASE_URL = mMockServer.url("/").toString();
        mMockServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(TestUtils.readStringFromFile(getContext(), fileName)));

        Intent intent = new Intent();
        mActivityRule.launchActivity(intent);

        onView(withId(R.id.loading_view)).check(matches(not(isDisplayed())));
        onView(withId(R.id.no_data_view)).check(matches(not(isDisplayed())));
    }

    @Test
    public void ensureEmptyViewDisplayedWhenRecipesDoNotLoad() throws Exception {
        String fileName = "recipes_empty_response_mock.json";
        RetrofitClientInstance.BASE_URL = mMockServer.url("/").toString();
        mMockServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(TestUtils.readStringFromFile(getContext(), fileName)));

        Intent intent = new Intent();
        mActivityRule.launchActivity(intent);

        onView(withId(R.id.loading_view)).check(matches(not(isDisplayed())));
        onView(withId(R.id.no_data_view)).check(matches(isDisplayed()));
    }

    @Test
    public void ensureRecipeStepListActivityLaunches() throws Exception {
        String fileName = "recipes_successful_response_mock.json";
        RetrofitClientInstance.BASE_URL = mMockServer.url("/").toString();
        mMockServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(TestUtils.readStringFromFile(getContext(), fileName)));

        Intent intent = new Intent();
        mActivityRule.launchActivity(intent);

        onView(withId(R.id.recipe_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.recipestep_list)).check(matches(isDisplayed()));
    }

    @After
    public void tearDown() throws Exception {
        mMockServer.shutdown();
        RetrofitClientInstance.clearInstance();
    }
}