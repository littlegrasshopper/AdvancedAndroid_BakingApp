package com.example.android.bakingapp.service;

import com.example.android.bakingapp.model.Recipe;

import java.util.List;

import retrofit2.http.GET;


public interface RecipesJSONService {
    @GET("baking.json")
    rx.Observable<List<Recipe>> getRecipes ();
}
