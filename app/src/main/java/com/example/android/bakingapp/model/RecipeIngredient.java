package com.example.android.bakingapp.model;

import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Model for storing ingredients for a recipe.
 */
@Parcel
public class RecipeIngredient {

    private String quantity;
    private String measure;
    private String ingredient;

    public RecipeIngredient() {}

    public RecipeIngredient(JSONObject jsonObject) {
        this.quantity = jsonObject.optString("quantity");
        this.measure = jsonObject.optString("measure");
        this.ingredient = jsonObject.optString("ingredient");
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    /**
     * Convenience class for returning list of ingredients for a given recipe
     */
    public static class IngredientsResult {

        private ArrayList<RecipeIngredient> results;

        public IngredientsResult() {
            results = new ArrayList<>();
        }

        public ArrayList<RecipeIngredient> getResults() {
            return results;
        }
    }
}
