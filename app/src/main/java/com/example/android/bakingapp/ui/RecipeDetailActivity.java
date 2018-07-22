package com.example.android.bakingapp.ui;

import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.bakingapp.R;

public class RecipeDetailActivity extends AppCompatActivity {

    // Extra for the recipe ID to be received in the intent
    public static final String EXTRA_RECIPE = "extraRecipe";
    // Bundle parameter for the recipe ID to be received after rotation
    public static final String INSTANCE_RECIPE = "instanceRecipe";
    // Bundle parameter for scroll position
    public static final String SCROLL_POSITION = "scrollPosition";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        //Only create new fragments when there is no previously saved state
        if (savedInstanceState == null) {

            // Create a new detail fragment
            RecipeDetailFragment detailFragment = new RecipeDetailFragment();

            // Add the fragment to its container using a FragmentManager and a transaction
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .add(R.id.recipe_detail_container, detailFragment)
                    .commit();
        }
    }
}
