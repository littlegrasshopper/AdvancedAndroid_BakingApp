package com.example.android.bakingapp.ui;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.example.android.bakingapp.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * Expresso Instrumented tests generated using Expresso Test Recorder.
 * https://developer.android.com/studio/test/espresso-test-recorder
 * Tests are:
 * 1) Click on a recipe in the Recipe main activity.
 * 2) Verify that the detail activity is displayed by searching for Ingredients and Steps labels.
 * 3) Click on a Step.
 * 4) Verify that it goes to the step detail activity where Next Button exists.
 * 5) Click on the Back button and verify it navigates up correctly to the Ingredients/Step activity.
 * 6) Click on the Back button and verify it navigates up correctly to the Recipe main activity.
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class RecipeMainActivityTest {

    @Rule
    public ActivityTestRule<RecipeMainActivity> mActivityTestRule = new ActivityTestRule<>(RecipeMainActivity.class);

    @Test
    public void recipeMainActivityTest() {
        try {
            Thread.sleep(1400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.rvRecipes),
                        childAtPosition(
                                withId(R.id.recipe_main_container),
                                0)));
        recyclerView.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction textView = onView(
                allOf(withId(R.id.tvIngredients), withText("Ingredients"),
                        childAtPosition(
                                allOf(withId(R.id.recipe_detail_fragment),
                                        childAtPosition(
                                                withId(R.id.recipe_detail_scrollview),
                                                0)),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("Ingredients")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.tvSteps), withText("Steps"),
                        childAtPosition(
                                allOf(withId(R.id.recipe_detail_fragment),
                                        childAtPosition(
                                                withId(R.id.recipe_detail_scrollview),
                                                0)),
                                2),
                        isDisplayed()));
        textView2.check(matches(withText("Steps")));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.tvRecipeStep), withText("Recipe Introduction"),
                        childAtPosition(
                                allOf(withId(R.id.step_container),
                                        childAtPosition(
                                                withId(R.id.item_step_layout),
                                                0)),
                                1),
                        isDisplayed()));
        textView3.check(matches(withText("Recipe Introduction")));

        ViewInteraction recyclerView3 = onView(
                allOf(withId(R.id.rvSteps),
                        childAtPosition(
                                withId(R.id.recipe_detail_fragment),
                                3)));
        recyclerView3.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.tvStepInstruction), withText("Recipe Introduction"),
                        childAtPosition(
                                allOf(withId(R.id.recipe_step_detail_container),
                                        childAtPosition(
                                                withId(R.id.recipe_step_detail_container),
                                                0)),
                                1),
                        isDisplayed()));

        ViewInteraction button = onView(
                allOf(withId(R.id.btnNextStep),
                        childAtPosition(
                                allOf(withId(R.id.activity_recipe_step_detail),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                1),
                        isDisplayed()));
        button.check(matches(isDisplayed()));

        ViewInteraction imageButton = onView(
                allOf(withContentDescription("Navigate up"),
                        childAtPosition(
                                allOf(withId(R.id.action_bar),
                                        childAtPosition(
                                                withId(R.id.action_bar_container),
                                                0)),
                                0),
                        isDisplayed()));

        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Navigate up"),
                        childAtPosition(
                                allOf(withId(R.id.action_bar),
                                        childAtPosition(
                                                withId(R.id.action_bar_container),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction textView5 = onView(
                allOf(withId(R.id.tvIngredients), withText("Ingredients"),
                        childAtPosition(
                                allOf(withId(R.id.recipe_detail_fragment),
                                        childAtPosition(
                                                withId(R.id.recipe_detail_scrollview),
                                                0)),
                                0),
                        isDisplayed()));
        textView5.check(matches(isDisplayed()));

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withContentDescription("Navigate up"),
                        childAtPosition(
                                allOf(withId(R.id.action_bar),
                                        childAtPosition(
                                                withId(R.id.action_bar_container),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton2.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction imageView = onView(
                allOf(withId(R.id.ivRecipeImage), withContentDescription("Recipe Image"),
                        childAtPosition(
                                allOf(withId(R.id.recipe_container),
                                        childAtPosition(
                                                withId(R.id.item_recipe_cardview),
                                                0)),
                                0),
                        isDisplayed()));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
