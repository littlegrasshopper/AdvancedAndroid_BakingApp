package com.example.android.bakingapp.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Movie;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.RecipeClient;
import com.example.android.bakingapp.adapter.RecipeArrayAdapter;
import com.example.android.bakingapp.model.Recipe;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.HttpException;
import okhttp3.OkHttpClient;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Main activity to display a grid of recipes.
 * Credit: Udacity S03.01-Exercise-RecyclerView (MainActivity.java)
 */
public class RecipeMainActivity extends AppCompatActivity
        implements RecipeArrayAdapter.RecipeArrayAdapterOnClickHandler {

    private static final String TAG = RecipeMainActivity.class.getSimpleName();
    private static final String LIFECYCLE_SCROLL_STATE = "scrollState";
    private static final String LIFECYCLE_SORT_BY_STATE = "sortByState";

    private static Parcelable scrollState;
    private int mSortBy = 0;

    private RecipeArrayAdapter mRecipeAdapter;
    private Subscription subscription;
    //private AppDatabase mDb;

    @BindView(R.id.rvRecipes)
    RecyclerView mRecipesRecyclerView;
    @BindView(R.id.tbToolbar) android.support.v7.widget.Toolbar mToolbar;
    @BindView(R.id.tvErrorMessage)
    TextView mErrorMessageDisplay;
    @BindView(R.id.pbLoadingIndicator)
    ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Stetho.initializeWithDefaults(this);
        new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();
        setContentView(R.layout.activity_recipe_main);
        ButterKnife.bind(this);

        // Sets the mToolbar to act as the ActionBar for this Activity window.
        if (mToolbar != null) {
//            setSupportActionBar(mToolbar);
        }
        mToolbar.setTitle(getResources().getString(R.string.app_name));

        // Restore sortBy selection if one is saved
        if (savedInstanceState != null && savedInstanceState.containsKey(LIFECYCLE_SORT_BY_STATE)) {
            mSortBy = savedInstanceState.getInt(LIFECYCLE_SORT_BY_STATE);
        }
        setupRecipes();
        fetchMovieData();
    }

    @Override
    protected void onDestroy() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
        super.onDestroy();
    }

    /**
     * Setup the recipes recycler view
     */
    private void setupRecipes() {
        mRecipeAdapter = new RecipeArrayAdapter(this, this);
        mRecipesRecyclerView.setHasFixedSize(true);
        mRecipesRecyclerView.setAdapter(mRecipeAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(
                this,
                3,
                GridLayoutManager.VERTICAL,
                false
        );
        mRecipesRecyclerView.setLayoutManager(gridLayoutManager);
    }

    /**
     * Fetch the recipes
     */
    private void fetchMovieData() {
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
        /*
        if (category == NetworkUtils.FAVORITES) {
            setupViewModel();
            return;
        }*/
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
                        Log.d(TAG, "OnNext");
                        Log.d(TAG, "movie results are: " + recipeResult);
                        mRecipeAdapter.setRecipeData(recipeResult/*.getResults()*/);
                        if (scrollState != null) {
                            mRecipesRecyclerView.getLayoutManager().onRestoreInstanceState(scrollState);
                        }
                    }
                });
    }

    /**
     * Setup the ViewModel for the recipes
     */
    /*
    private void setupViewModel() {
        mDb = AppDatabase.getsInstance(getApplicationContext());
        ShowFavoritesViewModel viewModel = ViewModelProviders
                .of(this)
                .get(ShowFavoritesViewModel.class);

        viewModel.getFavorites()
                .observe(this, new android.arch.lifecycle.Observer<List<Movie>>() {
                    @Override
                    public void onChanged(@Nullable final List<Movie> favoritesEntries) {
                        Log.d(TAG, "Updating list of favorites from LiveData in ViewModel: " + favoritesEntries);
                        // Lesson 12.13 4:29 This needs to be run on the UI thread
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mRecipeAdapter.setMovieData((ArrayList<Movie>)favoritesEntries);
                            }
                        });
                    }
                });
    }*/

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

    @Override
    public void onClick(Recipe recipe) {
        Class destinationActivity = RecipeDetailActivity.class;
        Context context = RecipeMainActivity.this;
        Intent intent = new Intent(context, destinationActivity);

        intent.putExtra(RecipeDetailActivity.EXTRA_RECIPE, Parcels.wrap(recipe));
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //Credit:
        // https://stackoverflow.com/questions/28236390/recyclerview-store-restore-state-between-activities
        // Save scroll state
        scrollState = mRecipesRecyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(LIFECYCLE_SCROLL_STATE, scrollState);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        //Credit:
        // https://stackoverflow.com/questions/28236390/recyclerview-store-restore-state-between-activities
        // Retrieve scroll state
        if(savedInstanceState != null) {
            scrollState = savedInstanceState.getParcelable(LIFECYCLE_SCROLL_STATE);
        }
    }
}
