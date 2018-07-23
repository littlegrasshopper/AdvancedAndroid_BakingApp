package com.example.android.bakingapp.ui;

import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.model.Recipe;

import org.parceler.Parcels;

import butterknife.ButterKnife;

public class RecipeDetailActivity extends AppCompatActivity {

    // Extra for the recipe object to be received in the intent
    public static final String EXTRA_RECIPE = "extraRecipe";
    // Instance parameter for the recipe
    public static final String INSTANCE_RECIPE = "instanceRecipe";
    // Instance parameter for fragment
    public static final String INSTANCE_FRAGMENT = "instanceFragment";

    private Recipe mRecipe;
    RecipeDetailFragment detailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        // Enable up icon
        /*
        if (mToolbar != null) {
            getSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }*/

        // Add the fragment to its container using a FragmentManager and a transaction
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();

        if (savedInstanceState != null) {
            // Restore the fragment's instance
            if (savedInstanceState.containsKey(INSTANCE_FRAGMENT)) {
                detailFragment = (RecipeDetailFragment) fragmentManager.getFragment(savedInstanceState, INSTANCE_FRAGMENT);
            }
            if (savedInstanceState.containsKey(INSTANCE_RECIPE)) {
                mRecipe = Parcels.unwrap(savedInstanceState.getParcelable(INSTANCE_RECIPE));
            }
        }

        // Only create new fragments when there is no previously saved state
        if (detailFragment == null) {

            // Create a new detail fragment
            detailFragment = new RecipeDetailFragment();

            fragmentManager.beginTransaction()
                    .add(R.id.recipe_detail_container, detailFragment)
                    .commit();

        }
        if (mRecipe == null) {
            // Get the passed in Recipe object from the intent
            mRecipe = Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_RECIPE));

            detailFragment.setIngredients(mRecipe.getIngredients());
            detailFragment.setSteps(mRecipe.getSteps());
        }

    }

    // Credit: https://stackoverflow.com/questions/15313598/once-for-all-how-to-correctly-save-instance-state-of-fragments-in-back-stack
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the fragment's instance
        getSupportFragmentManager().putFragment(outState, INSTANCE_FRAGMENT, detailFragment);
        outState.putParcelable(INSTANCE_RECIPE, Parcels.wrap(mRecipe));
    }
}
