package com.example.android.bakingapp.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.model.RecipeStep;

import org.parceler.Parcels;

public class RecipeStepDetailActivity extends AppCompatActivity {

    // Extra for the recipe ID to be received in the intent
    public static final String EXTRA_RECIPE_STEP = "extraRecipeStep";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_detail);

        //Only create new fragments when there is no previously saved state
        if (savedInstanceState == null) {

            // Create a new detail fragment
            RecipeStepDetailFragment stepDetailFragment = new RecipeStepDetailFragment();

            // Add the fragment to its container using a FragmentManager and a transaction
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .add(R.id.recipe_step_detail_container, stepDetailFragment)
                    .commit();

            // Get the passed in RecipeStep object from the intent
            RecipeStep recipeStep = Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_RECIPE_STEP));
            stepDetailFragment.setRecipeStep(recipeStep);
        }
    }
}
