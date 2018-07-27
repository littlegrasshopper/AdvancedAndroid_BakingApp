package com.example.android.bakingapp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.adapter.RecipeArrayAdapter;
import com.example.android.bakingapp.adapter.RecipeIngredientArrayAdapter;
import com.example.android.bakingapp.adapter.RecipeStepArrayAdapter;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.model.RecipeIngredient;
import com.example.android.bakingapp.model.RecipeStep;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Fragment for recipe ingredients and steps.
 */
public class RecipeDetailFragment extends Fragment {

    public static final String INSTANCE_RECIPE_STEPS = "recipeSteps";
    public static final String INSTANCE_RECIPE_INGREDIENTS = "recipeIngredients";

    private ArrayList<RecipeIngredient> mIngredients;
    private ArrayList<RecipeStep> mSteps;
    private RecipeArrayAdapter mAdapter;
    private RecipeStep mRecipeStep;

    RecipeStepArrayAdapter.RecipeStepArrayAdapterOnClickHandler mCallback;

    @BindView(R.id.rvIngredients) RecyclerView rvIngredients;
    @BindView(R.id.rvSteps)
    RecyclerView rvSteps;

    // Mandatory constructor for instantiating the fragment
    public RecipeDetailFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)  {

        // Inflate the recipe detail fragment
        View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        ButterKnife.bind(this, rootView);

        // Load the saved state if there is one
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(INSTANCE_RECIPE_STEPS)) {
                mSteps =  Parcels.unwrap(savedInstanceState.getParcelable(INSTANCE_RECIPE_STEPS));
            }
            if (savedInstanceState.containsKey(INSTANCE_RECIPE_INGREDIENTS)) {
                mIngredients = Parcels.unwrap(savedInstanceState.getParcelable(INSTANCE_RECIPE_INGREDIENTS));
            }
        }

        // Populate the list of ingredients by creating a layout manager and applying to the
        // ingredients recycler view
        LinearLayoutManager ingredientsLayoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        rvIngredients.setLayoutManager(ingredientsLayoutManager);
        RecipeIngredientArrayAdapter ingredientArrayAdapter = new RecipeIngredientArrayAdapter(getContext());
        rvIngredients.setAdapter(ingredientArrayAdapter);
        ingredientArrayAdapter.setRecipeIngredientData(mIngredients);


        // Populate the list of steps by creating a layout manager and applying to the
        // steps recycler view
        LinearLayoutManager stepsLayoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        rvSteps.setLayoutManager(stepsLayoutManager);
        RecipeStepArrayAdapter stepArrayAdapter = new RecipeStepArrayAdapter(getContext(), mCallback);
        rvSteps.setAdapter(stepArrayAdapter);
        stepArrayAdapter.setRecipeStepData(mSteps);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    // Setter methods to populate the ingredients and steps for the
    // fragment to display
    public void setIngredients(ArrayList<RecipeIngredient> ingredients) {
        mIngredients = ingredients;
    }

    public void setSteps(ArrayList<RecipeStep> steps) {
        mSteps = steps;
    }

    public void setRecipeStep(RecipeStep recipeStep) {
        mRecipeStep = recipeStep;
    }


/*
    @Override
    public void onClick(RecipeStep recipeStep) {
        Class destinationActivity = RecipeStepDetailActivity.class;

        Context context = getActivity();
        Intent intent = new Intent(context, destinationActivity);

        intent.putExtra(RecipeStepDetailActivity.EXTRA_RECIPE_STEP, Parcels.wrap(recipeStep));
        startActivity(intent);
    }*/

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
        outState.putParcelable(INSTANCE_RECIPE_STEPS, Parcels.wrap(mSteps));
        outState.putParcelable(INSTANCE_RECIPE_INGREDIENTS, Parcels.wrap(mIngredients));
    }
}
