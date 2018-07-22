package com.example.android.bakingapp.model;

import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Model for storing ingredients for a recipe.
 */
public class RecipeIngredients {

    private String quantity;
    private String measure;
    private String ingredient;

    public RecipeIngredients() {}

    public RecipeIngredients(JSONObject jsonObject) {
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

        private ArrayList<RecipeIngredients> results;

        public IngredientsResult() {
            results = new ArrayList<>();
        }

        public ArrayList<RecipeIngredients> getResults() {
            return results;
        }
    }
}
