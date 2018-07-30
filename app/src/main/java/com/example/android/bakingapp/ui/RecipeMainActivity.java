package com.example.android.bakingapp.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.bakingapp.BakingAppWidgetService;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.RecipeClient;
import com.example.android.bakingapp.adapter.RecipeArrayAdapter;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.model.RecipeIngredient;
import com.example.android.bakingapp.utilities.RecipeUtils;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.HttpException;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Main activity to display a grid of recipes.
 * Listens to click events on the recipes and instantiates RecipeDetailActivity.
 */
public class RecipeMainActivity extends AppCompatActivity
        implements RecipeArrayAdapter.RecipeArrayAdapterOnClickHandler {

    private static final String TAG = RecipeMainActivity.class.getSimpleName();
    private static final int MAX_WIDTH_RECIPE = 300;

    private static Parcelable scrollState;

    private RecipeArrayAdapter mRecipeAdapter;
    private Subscription subscription;

    @BindView(R.id.rvRecipes)
    RecyclerView mRecipesRecyclerView;
    @BindView(R.id.tvErrorMessage)
    TextView mErrorMessageDisplay;
    @BindView(R.id.pbLoadingIndicator)
    ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recipe_main);
        ButterKnife.bind(this);

        setupRecipes();
        fetchRecipeData();
    }

    @Override
    protected void onDestroy() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
        super.onDestroy();
    }

    /**
     * Setup the recipes recycler view using a GridLayoutManager for single and two-pane layout
     */
    private void setupRecipes() {
        mRecipeAdapter = new RecipeArrayAdapter(this, this);
        mRecipesRecyclerView.setHasFixedSize(true);
        mRecipesRecyclerView.setAdapter(mRecipeAdapter);
        Float dp = RecipeUtils.convertPixelsToDp(RecipeUtils.getScreenResolution(this), this);
        int gridSpan = (int)(dp / MAX_WIDTH_RECIPE);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(
                this,
                gridSpan > 0 ? gridSpan : 1,
                GridLayoutManager.VERTICAL,
                false
        );
        mRecipesRecyclerView.setLayoutManager(gridLayoutManager);
    }

    /**
     * Fetch the recipes
     */
    private void fetchRecipeData() {
        if (isOnline()) {
            showRecipeDataView();
            getRecipes();
        } else {
            showErrorMessage();
        }
    }

    /**
     * RxJava call to retrieve the list of recipes asynchronously
     */
    private void getRecipes() {
        subscription = RecipeClient.getInstance()
                .getRecipes()
                // scheduler where the Observable will do the work
                .subscribeOn(Schedulers.io())
                // scheduler which a subscriber will observe this Observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Recipe>>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "In Completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        // cast to retrofit2.HttpException to get the response code
                        if (e instanceof HttpException) {
                            int code = ((retrofit2.HttpException)e).code();
                        }
                        Log.d(TAG, "In Error");
                    }

                    @Override
                    public void onNext(List<Recipe> recipeResult) {
                        Log.d(TAG, "In OnNext");
                        Log.d(TAG, "Recipe results are: " + recipeResult);
                        mRecipeAdapter.setRecipeData(recipeResult);
                        if (scrollState != null) {
                            mRecipesRecyclerView.getLayoutManager().onRestoreInstanceState(scrollState);
                        }
                    }
                });
    }

    /**
     * Show the view for the recipes data and hide the error message display.
     */
    private void showRecipeDataView() {
        // hide the error message display
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        // show the list of movies
        mRecipesRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * Show the error message display and hide the recipes data.
     */
    private void showErrorMessage() {
        // hide the view for the list of movies
        mRecipesRecyclerView.setVisibility(View.INVISIBLE);
        // show the error message
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    /**
     * Check to make sure there is network connection
     * @return True if network is available, false otherwise.
     * Credit:
     * https://stackoverflow.com/questions/1560788/how-to-check-internet-access-on-android-inetaddress-never-times-out
     */
    // === Start ===
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }
    // === End ===

    /**
     * When a recipe is selected, show the detail activity and update the app widget with the list
     * of ingredients.
     * @param recipe Selected Recipe object.
     */
    @Override
    public void onClick(Recipe recipe) {

        // Show the detail activity
        Class destinationActivity = RecipeDetailActivity.class;
        Context context = RecipeMainActivity.this;
        Intent intent = new Intent(context, destinationActivity);

        intent.putExtra(RecipeUtils.EXTRA_RECIPE, Parcels.wrap(recipe));
        startActivity(intent);

        //Update the Widget service
        StringBuilder builder = new StringBuilder();
        ArrayList<RecipeIngredient> ingredients = recipe.getIngredients();
        for (int i = 0; i < ingredients.size(); i++) {
            // Credit:
            // https://stackoverflow.com/questions/23503642/how-to-use-formatted-strings-together-with-placeholders-in-android
            builder.append(String.format(getString(R.string.bulletedList), ingredients.get(i).getIngredient()));
        }
        BakingAppWidgetService.startActionUpdateBakingWidget(context, recipe,
                recipe.getName(), builder.toString());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //Credit:
        // https://stackoverflow.com/questions/28236390/recyclerview-store-restore-state-between-activities
        // Save scroll state
        scrollState = mRecipesRecyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(RecipeUtils.SCROLL_STATE, scrollState);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        //Credit:
        // https://stackoverflow.com/questions/28236390/recyclerview-store-restore-state-between-activities
        // Retrieve scroll state
        if(savedInstanceState != null) {
            scrollState = savedInstanceState.getParcelable(RecipeUtils.SCROLL_STATE);
        }
    }
}
