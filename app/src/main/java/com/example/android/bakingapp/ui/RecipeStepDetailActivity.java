package com.example.android.bakingapp.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.bakingapp.R;

public class RecipeStepDetailActivity extends AppCompatActivity {

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
        }
    }
}
