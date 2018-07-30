package com.example.android.bakingapp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.adapter.RecipeIngredientArrayAdapter;
import com.example.android.bakingapp.adapter.RecipeStepArrayAdapter;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.model.RecipeIngredient;
import com.example.android.bakingapp.model.RecipeStep;
import com.example.android.bakingapp.utilities.RecipeUtils;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Fragment for displaying recipe ingredients and steps.
 * Instantiated by RecipeDetailActivity.
 */
public class RecipeDetailFragment extends Fragment {

    private ArrayList<RecipeIngredient> mIngredients;
    private ArrayList<RecipeStep> mSteps;
    private Recipe mRecipe;

    RecipeStepArrayAdapter.RecipeStepArrayAdapterOnClickHandler mCallback;

    @BindView(R.id.rvIngredients) RecyclerView rvIngredients;
    @BindView(R.id.rvSteps) RecyclerView rvSteps;
    @BindView(R.id.recipe_detail_scrollview)

    NestedScrollView mNestedScrollView;

    // Track scroll position
    private static int scrollX;
    private static int scrollY;

    // Mandatory constructor for instantiating the fragment
    public RecipeDetailFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)  {

        // Inflate the recipe detail fragment
        View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        // Load the saved state if there is one
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(RecipeUtils.INSTANCE_RECIPE)) {
                mRecipe =  Parcels.unwrap(savedInstanceState.getParcelable(RecipeUtils.INSTANCE_RECIPE));
            }
            // Restore scroll position
            // Credit:
            // https://stackoverflow.com/questions/29208086/save-the-position-of-scrollview-when-the-orientation-changes

            if (savedInstanceState.containsKey(RecipeUtils.SCROLL_POSITION)) {
                final int[] position = savedInstanceState.getIntArray(RecipeUtils.SCROLL_POSITION);
                scrollX = position[0];
                scrollY = position[1];
                if(position != null) {
                    mNestedScrollView.post(new Runnable() {
                        public void run() {
                            mNestedScrollView.scrollTo(scrollX, scrollY);
                        }
                    });
                }
            }
        }

        if (mRecipe == null) {
            Bundle args = getArguments();
            mRecipe = Parcels.unwrap(args.getParcelable(RecipeUtils.EXTRA_RECIPE));
        }

        mSteps = mRecipe.getSteps();
        mIngredients = mRecipe.getIngredients();

        // Populate the list of ingredients by creating a layout manager and applying to the
        // ingredients recycler view
        LinearLayoutManager ingredientsLayoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        rvIngredients.setLayoutManager(ingredientsLayoutManager);
        RecipeIngredientArrayAdapter ingredientArrayAdapter = new RecipeIngredientArrayAdapter(getContext());
        rvIngredients.setAdapter(ingredientArrayAdapter);
        ingredientArrayAdapter.setRecipeIngredientData(mIngredients);
        rvIngredients.setNestedScrollingEnabled(false);


        // Populate the list of steps by creating a layout manager and applying to the
        // steps recycler view
        LinearLayoutManager stepsLayoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        rvSteps.setLayoutManager(stepsLayoutManager);
        RecipeStepArrayAdapter stepArrayAdapter = new RecipeStepArrayAdapter(getContext(), mCallback);
        rvSteps.setAdapter(stepArrayAdapter);
        stepArrayAdapter.setRecipeStepData(mSteps);
        rvSteps.setNestedScrollingEnabled(false);
    }

    /**
     * Ensure the host activity has implemented the callback interface
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (RecipeStepArrayAdapter.RecipeStepArrayAdapterOnClickHandler) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnClickListener");
        }
    }

    /**
     * Save the current state of the fragment
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(RecipeUtils.INSTANCE_RECIPE, Parcels.wrap(mRecipe));
        // Save scroll position
        // Credit:
        // https://stackoverflow.com/questions/29208086/save-the-position-of-scrollview-when-the-orientation-changes

        outState.putIntArray(RecipeUtils.SCROLL_POSITION,
                new int[]{mNestedScrollView.getScrollX(), mNestedScrollView.getScrollY()});

        super.onSaveInstanceState(outState);
    }
}
