package com.example.android.bakingapp;

import android.graphics.Movie;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.service.RecipesJSONService;

import java.io.IOException;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * RecipeClient is a singleton that uses Retrofit to interact with the API service.
 */
public class RecipeClient {

    public static final String RECIPE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";

    private static final Object LOCK = new Object();
    private static RecipeClient instance;
    private RecipesJSONService service;

    private RecipeClient() {
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        okHttpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                HttpUrl originalHttpUrl = original.url();
                Log.d("MovieClient", "OriginalURL: " + originalHttpUrl);
                HttpUrl url = originalHttpUrl.newBuilder()
                        //.addQueryParameter("api_key", NetworkUtils.API_KEY)
                        .build();
                Request.Builder requestBuilder = original.newBuilder()
                        .url(url);
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RECIPE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient.build())
                .build();

        service = retrofit.create(RecipesJSONService.class);
    }

    public static RecipeClient getInstance() {
        if (instance == null) {
            synchronized (LOCK) {
                instance = new RecipeClient();
            }
        }
        return instance;
    }

    /**
     * Invokes the network call via the RecipeJSONService
     */
    public Observable<List<Recipe>> getRecipes() {
        return service.getRecipes();
    }
}
