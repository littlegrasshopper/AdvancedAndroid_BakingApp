package com.example.android.bakingapp.ui;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.adapter.RecipeStepArrayAdapter;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.model.RecipeStep;

import org.parceler.Parcel;
import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailActivity extends AppCompatActivity
        implements RecipeStepArrayAdapter.RecipeStepArrayAdapterOnClickHandler {

    // Extra for the recipe object to be received in the intent
    public static final String EXTRA_RECIPE = "extraRecipe";
    public static final String EXTRA_RECIPE_STEP = "extraRecipeStep";
    public static final String EXTRA_SHOW_NEXT = "extraShowNext";
    public static final String EXTRA_CALLBACK = "extraCallback";
    public static final String EXTRA_RECIPE_CURRENT_STEP = "extraRecipeCurrentStep";

    // Instance parameter for the recipe
    public static final String INSTANCE_RECIPE = "instanceRecipe";
    // Instance parameter for fragment
    public static final String INSTANCE_FRAGMENT_STEP = "instanceFragmentStep";
    public static final String INSTANCE_FRAGMENT_STEP_DETAIL = "instanceFragmentStepDetail";

    private Recipe mRecipe;
    RecipeDetailFragment detailFragment;
    RecipeStepDetailFragment stepDetailFragment;
    int mCurrentRecipeStep;
    int mTotalRecipeSteps;


    private boolean mTwoPane;

    public void RecipeDetailActvity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        ButterKnife.bind(this);


        // Enable up icon
        /*
        if (mToolbar != null) {
            getSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }*/


       // if (savedInstanceState != null) {
            if (savedInstanceState == null) {
                // Add the fragment to its container using a FragmentManager and a transaction
                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();

                // Restore the fragment's instance
                /*
            if (savedInstanceState.containsKey(INSTANCE_FRAGMENT)) {
                detailFragment = (RecipeDetailFragment) fragmentManager.getFragment(savedInstanceState, INSTANCE_FRAGMENT);
            }
            if (savedInstanceState.containsKey(INSTANCE_RECIPE)) {
                mRecipe = Parcels.unwrap(savedInstanceState.getParcelable(INSTANCE_RECIPE));
            }*/
                //}

                // Only create new fragments when there is no previously saved state
                //if (detailFragment == null) {

                // Create a new detail fragment
                detailFragment = new RecipeDetailFragment();

                fragmentManager.beginTransaction()
                        .add(R.id.recipe_detail_container, detailFragment)
                        .commit();

                //}
                //if (mRecipe == null) {
                // Get the passed in Recipe object from the intent
                mRecipe = Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_RECIPE));

                detailFragment.setIngredients(mRecipe.getIngredients());
                detailFragment.setSteps(mRecipe.getSteps());



                if (findViewById(R.id.recipe_step_detail_container) != null) {/*
                    stepDetailFragment = new RecipeStepDetailFragment();
                    fragmentManager.beginTransaction()
                            .add(R.id.recipe_step_detail_container, stepDetailFragment)
                            .commit();*/
                    // Hide the Next Step button
                    //Button button = (Button) findViewById(R.id.btnNextStep);
                    //button.setVisibility(View.GONE);
                    mTwoPane = true;
                } else {
                    mTwoPane = false;
                }
        }

    }

    @Override
    public void onClick(RecipeStep recipeStep) {

        if (mTwoPane) {
            //if (stepDetailFragment == null) {
                stepDetailFragment = new RecipeStepDetailFragment();
            //}

            // Replace the old step fragment with a new one
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipe_step_detail_container, stepDetailFragment)
                    .commit();
            //stepDetailFragment.setRecipeStep(recipeStep);
            stepDetailFragment.setRecipe(mRecipe);
            stepDetailFragment.setCurrentRecipeStep(mCurrentRecipeStep);
            //stepDetailFragment.showNextButton(false);

        } else {
            Class destinationActivity = RecipeStepDetailActivity.class;
            Context context = this;
            Bundle b = new Bundle();
            //b.putParcelable(EXTRA_RECIPE_STEP, Parcels.wrap(recipeStep));
            b.putParcelable(EXTRA_RECIPE, Parcels.wrap(mRecipe));
            b.putInt(EXTRA_RECIPE_CURRENT_STEP, mCurrentRecipeStep);
            //b.putBoolean(EXTRA_SHOW_NEXT, showNextButton());
            final Intent intent = new Intent(context, destinationActivity);
            intent.putExtras(b);
            startActivity(intent);
        }
    }


    // Credit: https://stackoverflow.com/questions/15313598/once-for-all-how-to-correctly-save-instance-state-of-fragments-in-back-stack
        /*
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the fragment's instance
        getSupportFragmentManager().putFragment(outState, INSTANCE_FRAGMENT, detailFragment);
        outState.putParcelable(INSTANCE_RECIPE, Parcels.wrap(mRecipe));
    }*/
}
