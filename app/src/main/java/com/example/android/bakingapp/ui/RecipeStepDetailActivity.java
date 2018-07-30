package com.example.android.bakingapp.ui;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.model.RecipeStep;
import com.example.android.bakingapp.utilities.RecipeUtils;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Containing activity for details of a recipe step (video, instructions, next button)
 */
public class RecipeStepDetailActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = RecipeStepDetailActivity.class.getSimpleName();

    @BindView(R.id.btnNextStep) Button mNextButton;

    private Recipe mRecipe;
    private int mCurrentRecipeStepIndex;
    RecipeStepDetailFragment mRecipeStepDetailFragment;

    public RecipeStepDetailActivity( ) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_detail);
        ButterKnife.bind(this);

        //Only create new fragments when there is no previously saved state
        if (savedInstanceState == null) {

            // Get the passed in Recipe object from the intent
            Intent intent = getIntent();
            mRecipe = Parcels.unwrap(intent.getParcelableExtra(RecipeUtils.EXTRA_RECIPE));
            mCurrentRecipeStepIndex = intent.getIntExtra(RecipeUtils.EXTRA_RECIPE_STEP_INDEX, 0);

            // Create a new step detail fragment
            mRecipeStepDetailFragment = new RecipeStepDetailFragment();

            // Build the bundle args
            mRecipeStepDetailFragment.setArguments(RecipeUtils.buildRecipeBundle(mRecipe, mCurrentRecipeStepIndex));

            // Add the fragment to its container using a FragmentManager and a transaction
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.recipe_step_detail_container, mRecipeStepDetailFragment)
                    .commit();

            showOrHideNextButton();
        }
        else {
            //Restore the fragment's instance
            mRecipeStepDetailFragment = (RecipeStepDetailFragment) getSupportFragmentManager()
                    .getFragment(savedInstanceState, RecipeUtils.INSTANCE_FRAGMENT);
        }

        mNextButton.setOnClickListener(this);
    }

    /**
     * OnClick handler for the Next Button to see the next step instructions and video
     * @param view Button that was clicked
     */
    @Override
    public void onClick(View view) {

        // Create a new fragment and replace
        // Increment the step count
        mRecipeStepDetailFragment = new RecipeStepDetailFragment();
        Bundle bundle = RecipeUtils.buildRecipeBundle(mRecipe, ++mCurrentRecipeStepIndex);

        mRecipeStepDetailFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.recipe_step_detail_container, mRecipeStepDetailFragment)
                .commit();

        showOrHideNextButton();
    }

    /**
     * Show the Next Step button as needed
     */
    public void showOrHideNextButton() {
        if (mRecipe != null) {
            ArrayList<RecipeStep> steps = mRecipe.getSteps();
            if (steps != null) {
                int numOfSteps = steps.size();
                if (mCurrentRecipeStepIndex >= numOfSteps - 1) {
                    mNextButton.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    // Credit: https://stackoverflow.com/questions/15313598/once-for-all-how-to-correctly-save-instance-state-of-fragments-in-back-stack
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Save the fragment's instance
        getSupportFragmentManager().putFragment(outState, RecipeUtils.INSTANCE_FRAGMENT, mRecipeStepDetailFragment);
        outState.putParcelable(RecipeUtils.INSTANCE_RECIPE, Parcels.wrap(mRecipe));
        outState.putInt(RecipeUtils.INSTANCE_RECIPE_STEP_INDEX, mCurrentRecipeStepIndex);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(RecipeUtils.INSTANCE_RECIPE)) {
                mRecipe = Parcels.unwrap(savedInstanceState.getParcelable(RecipeUtils.INSTANCE_RECIPE));
            }
            if (savedInstanceState.containsKey(RecipeUtils.INSTANCE_RECIPE_STEP_INDEX)) {
                mCurrentRecipeStepIndex = savedInstanceState.getInt(RecipeUtils.INSTANCE_RECIPE_STEP_INDEX, 0);
            }
        }
    }
}
