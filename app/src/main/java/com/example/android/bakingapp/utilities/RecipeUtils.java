package com.example.android.bakingapp.utilities;

import android.os.Bundle;
import android.util.Log;

import com.example.android.bakingapp.model.Recipe;

import org.parceler.Parcels;

/**
 * Utility functions and common constants
 */

public final class RecipeUtils {

    // Keys for intent extras
    public static final String EXTRA_RECIPE = "extraRecipe";
    public static final String EXTRA_RECIPE_STEP_INDEX = "extraRecipeStepIndex";
    public static final String EXTRA_RECIPE_STEP = "extraRecipeStep";

    // Keys for saving instance state for recipe
    public static final String INSTANCE_RECIPE = "instanceRecipe";
    public static final String INSTANCE_RECIPE_STEP = "instanceRecipeStep";
    public static final String INSTANCE_RECIPE_STEP_INDEX = "instanceRecipeStepIndex";

    // Keys for saving instance state for fragments
    public static final String INSTANCE_FRAGMENT = "instanceFragment";

    // Keys for saving instance state media player
    public static final String INSTANCE_PLAYER_POSITION = "instancePlayerPosition";
    public static final String INSTANCE_PLAYER_STATE = "instancePlayerState";

    /**
     * Build the bundle with values to pass between activity and fragment
     * @param recipe Primary data object for recipes.
     * @param stepIndex Currently selected step in the recipe.
     * @return Bundle object
     */
    public static Bundle buildRecipeBundle(Recipe recipe, int stepIndex) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(RecipeUtils.EXTRA_RECIPE, Parcels.wrap(recipe));
        bundle.putInt(RecipeUtils.EXTRA_RECIPE_STEP_INDEX, stepIndex);
        return bundle;
    }
}