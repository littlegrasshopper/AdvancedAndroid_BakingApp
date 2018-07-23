package com.example.android.bakingapp.model;

import android.support.annotation.NonNull;
import org.json.JSONObject;
import org.parceler.Parcel;
import java.util.ArrayList;

/**
 * Recipe data object.
 */
@Parcel
public class Recipe {

    private String id;
    private String name;
    private String servings;
    private String image;

    private ArrayList<RecipeIngredient> ingredients;

    private ArrayList<RecipeStep> steps;

    public Recipe() {}

    public Recipe(JSONObject jsonObject) {
        this.id = jsonObject.optString("id");
        this.name = jsonObject.optString("name");
        this.servings = jsonObject.optString("servings");
        this.image = jsonObject.optString("image");
    }

    public String getId() {
        return this.id;
    }

    public void setId(@NonNull String recipeId) {
        this.id = recipeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServings() {
        return servings;
    }

    public void setServings(String servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ArrayList<RecipeIngredient> getIngredients() {
        return this.ingredients;
    }

    public void setIngredients(ArrayList<RecipeIngredient> ingredients) {
        this.ingredients = ingredients;
    }

    public ArrayList<RecipeStep> getSteps() {
        return this.steps;
    }

    public void setSteps(ArrayList<RecipeStep> steps) {
        this.steps = steps;
    }

    /**
     * Convenience class for returning a list of recipe objects.
     * Credit:
     * https://stackoverflow.com/questions/9598707/gson-throwing-expected-begin-object-but-was-begin-array
     * https://stackoverflow.com/questions/34623053/how-to-get-array-of-objects-with-gson-retrofit
     */
    public static class RecipeResult {

        private ArrayList<Recipe> results;

        public RecipeResult() {
            results = new ArrayList<>();
        }

        public ArrayList<Recipe> getResults() {
            return results;
        }
    }
}
