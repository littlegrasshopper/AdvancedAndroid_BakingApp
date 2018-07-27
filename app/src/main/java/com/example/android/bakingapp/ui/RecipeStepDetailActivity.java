package com.example.android.bakingapp.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.model.RecipeStep;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Containing activity for details of a recipe step (video, instructions, next button)
 */
public class RecipeStepDetailActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = RecipeStepDetailActivity.class.getSimpleName();

    @BindView(R.id.btnNextStep)
    Button mNextButton;

    private Recipe mRecipe;
    private int mCurrentRecipeStep;

    private RecipeStep mRecipeStep;
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

            // Create a new detail fragment
            mRecipeStepDetailFragment = new RecipeStepDetailFragment();

            // Add the fragment to its container using a FragmentManager and a transaction
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .add(R.id.recipe_step_detail_container, mRecipeStepDetailFragment)
                    .commit();

            // Get the passed in RecipeStep object from the intent
            Intent intent = getIntent();
            //mRecipeStep = Parcels.unwrap(intent.getParcelableExtra(RecipeDetailActivity.EXTRA_RECIPE_STEP));
            //mRecipeStepDetailFragment.setRecipeStep(mRecipeStep);
            mRecipe = Parcels.unwrap(intent.getParcelableExtra(RecipeDetailActivity.EXTRA_RECIPE));
            mCurrentRecipeStep = intent.getIntExtra(RecipeDetailActivity.EXTRA_RECIPE_CURRENT_STEP, 0);
            mRecipeStepDetailFragment.setRecipe(mRecipe);
            mRecipeStepDetailFragment.setCurrentRecipeStepIndex(mCurrentRecipeStep);
            showOrHideNextButton();
        }
        mNextButton.setOnClickListener(this);
    }

    /**
     * OnClick handler for the Next Button
     * @param view Button that was clicked
     */
    @Override
    public void onClick(View view) {
        // Tell the fragment to stop the media player
        if (mRecipeStepDetailFragment != null) {
            mRecipeStepDetailFragment.stopPlayer();
            // TODO: does it need to be released
        }
/*
        mRecipeStepDetailFragment = new RecipeStepDetailFragment();
        mRecipeStepDetailFragment.setRecipe(mRecipe);
        mRecipeStepDetailFragment.setCurrentRecipeStepIndex(++mCurrentRecipeStep);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.recipe_step_detail_container, mRecipeStepDetailFragment)
                .commit();
        showOrHideNextButton();
*/
        //mRecipeStepDetailFragment.setupMediaPlayer(mRecipe.getSteps().get(mCurrentRecipeStep), 0, true);

        Bundle b = new Bundle();
        //b.putParcelable(RecipeDetailActivity.EXTRA_RECIPE_STEP, Parcels.wrap(mRecipeStep));
        b.putParcelable(RecipeDetailActivity.EXTRA_RECIPE, Parcels.wrap(mRecipe));
        b.putInt(RecipeDetailActivity.EXTRA_RECIPE_CURRENT_STEP, ++mCurrentRecipeStep);
        final Intent intent = new Intent(RecipeStepDetailActivity.this, RecipeStepDetailActivity.class);
        intent.putExtras(b);
        startActivity(intent);
        finish();
    }

    public void showOrHideNextButton() {
        if (mRecipe != null) {
            ArrayList<RecipeStep> steps = mRecipe.getSteps();
            if (steps != null) {
                int numOfSteps = steps.size();
                if (mCurrentRecipeStep < numOfSteps) {
                    mNextButton.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    // TODO: Save next button state?
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
