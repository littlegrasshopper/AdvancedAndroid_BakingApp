package com.example.android.bakingapp.ui;

import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.adapter.RecipeStepArrayAdapter;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.model.RecipeStep;
import com.example.android.bakingapp.utilities.RecipeUtils;

import org.parceler.Parcels;

/**
 * Activity container for RecipeDetailFragment to display recipe ingredients and steps.
 * Invoked from RecipeMainActivity and invokes RecipeStepDetailActivity.
 */
public class RecipeDetailActivity extends AppCompatActivity
        implements RecipeStepArrayAdapter.RecipeStepArrayAdapterOnClickHandler {

    public static final String TAG = RecipeDetailActivity.class.getSimpleName();

    private Recipe mRecipe;
    RecipeDetailFragment detailFragment;
    RecipeStepDetailFragment stepDetailFragment;

    public void RecipeDetailActvity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        // Restore if there is a previously saved state
        if (savedInstanceState != null) {
            //Restore the fragment's instance
            detailFragment = (RecipeDetailFragment) getSupportFragmentManager()
                    .getFragment(savedInstanceState, RecipeUtils.INSTANCE_FRAGMENT);
        } else {
            // Determine if a recipe fragment already exists
            if (detailFragment == null) {
                detailFragment = (RecipeDetailFragment) getSupportFragmentManager()
                        .findFragmentByTag(RecipeDetailFragment.class.getCanonicalName());
            }

            // Create a new fragment if one doesn't exist
            if (detailFragment == null) {
                detailFragment = new RecipeDetailFragment();

                // Get the passed in Recipe object from the intent
                Intent intent = getIntent();
                mRecipe = Parcels.unwrap(intent.getParcelableExtra(RecipeUtils.EXTRA_RECIPE));

                // Bundle Recipe object and initial step index to send to the fragment
                detailFragment.setArguments(RecipeUtils.buildRecipeBundle(mRecipe, 0));

                // Add the fragment to its container using a FragmentManager and a transaction
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.recipe_detail_container,
                                detailFragment,
                                RecipeDetailFragment.class.getCanonicalName())
                        .commit();
            }
        }
    }

    /**
     * Handle user clicks on the RecipeStep object.
     * @param recipeStep The step user clicked on.
     * @param pos The index of the selected RecipeStep object in the list.
     */
    @Override
    public void onClick(RecipeStep recipeStep, int pos) {

        // Build the bundle arg
        Bundle bundle = RecipeUtils.buildRecipeBundle(mRecipe, pos);

        /* If in a two-pane/tablet layout, just instantiate a new RecipeStepDetail fragment.
         * Otherwise, start a new activity for the step detail.
         */
        if (RecipeUtils.isTablet(this)) {
            // Just need to create/replace the step detail fragment
            stepDetailFragment = new RecipeStepDetailFragment();
            stepDetailFragment.setArguments(bundle);

            // Replace the old step fragment with a new one
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipe_step_detail_container, stepDetailFragment)
                    .commit();
        } else {
            Class destinationActivity = RecipeStepDetailActivity.class;
            Context context = this;

            final Intent intent = new Intent(context, destinationActivity);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    // Credit: https://stackoverflow.com/questions/15313598/once-for-all-how-to-correctly-save-instance-state-of-fragments-in-back-stack
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Save the fragment's instance
        getSupportFragmentManager().putFragment(outState, RecipeUtils.INSTANCE_FRAGMENT, detailFragment);
        outState.putParcelable(RecipeUtils.INSTANCE_RECIPE, Parcels.wrap(mRecipe));

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(RecipeUtils.INSTANCE_RECIPE)) {
                mRecipe = Parcels.unwrap(savedInstanceState.getParcelable(RecipeUtils.INSTANCE_RECIPE));
            }
        }
    }
}
