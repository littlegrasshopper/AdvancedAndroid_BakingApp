package com.example.android.bakingapp.ui;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

/**
 * Container activity for ingredients and steps.
 */
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
    public static final String INSTANCE_RECIPE_STEP = "instanceRecipeStep";

    // Instance parameter for fragment
    public static final String INSTANCE_FRAGMENT = "instanceFragment";
    public static final String INSTANCE_FRAGMENT_STEP_DETAIL = "instanceFragmentStepDetail";

    private Recipe mRecipe;
    int mCurrentRecipeStep;

    RecipeDetailFragment detailFragment;
    RecipeStepDetailFragment stepDetailFragment;

    private boolean mTwoPane;

    public void RecipeDetailActvity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        /*
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(INSTANCE_FRAGMENT)) {
                detailFragment =  Parcels.unwrap(savedInstanceState.getParcelable(INSTANCE_FRAGMENT));
            }
            if (savedInstanceState.containsKey(RecipeStepDetailFragment.INSTANCE_RECIPE)) {
                mRecipe = Parcels.unwrap(savedInstanceState.getParcelable(RecipeStepDetailFragment.INSTANCE_RECIPE));
            }
        }*/
        if (savedInstanceState == null) {

            Log.i("RecipeDetailActivity", "onCreate");
            // Add the fragment to its container using a FragmentManager and a transaction
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();

            // Get the passed in Recipe object from the intent
            mRecipe = Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_RECIPE));

            // Only create new fragments when there is no previously saved state
            detailFragment = new RecipeDetailFragment();

            Bundle b = new Bundle();
            b.putParcelable(EXTRA_RECIPE, Parcels.wrap(mRecipe));
            detailFragment.setArguments(b);

            fragmentManager.beginTransaction()
                    .add(R.id.recipe_detail_container, detailFragment)
                    .commit();


            // Check if this is a two-pane layout (tablet)
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

    /**
     * Click handler for traversing to the next step.
     * @param recipeStep The step user selected.
     */
    @Override
    public void onClick(RecipeStep recipeStep) {

        // Build the bundle arg
        Bundle bundle = new Bundle();
        bundle.putParcelable(RecipeDetailActivity.EXTRA_RECIPE, Parcels.wrap(mRecipe));
        bundle.putInt(RecipeDetailActivity.EXTRA_RECIPE_CURRENT_STEP, (int)Long.parseLong(recipeStep.getId()));
        //bundle.putParcelable(EXTRA_RECIPE_STEP, Parcels.wrap(recipeStep));

        /* If in a two-pane layout, just instantiate a new recipe step detail fragment.
         * Otherwise, start a new activity for the step detail.
         */
        if (mTwoPane) {
            // Just need to create/replace the step detail fragment
            stepDetailFragment = new RecipeStepDetailFragment();
            stepDetailFragment.setArguments(bundle);

            // Replace the old step fragment with a new one
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipe_step_detail_container, stepDetailFragment)
                    .commit();
        } else {
            Log.i("RecipeDetailActivity", "about to start step detail activity");
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
        //getSupportFragmentManager().putFragment(outState, INSTANCE_FRAGMENT, detailFragment);
        outState.putParcelable(RecipeStepDetailFragment.INSTANCE_RECIPE, Parcels.wrap(mRecipe));
        //outState.putInt(RecipeStepDetailFragment.INSTANCE_RECIPE_STEP_INDEX, mCurrentRecipeStep);
        super.onSaveInstanceState(outState);

    }
}
