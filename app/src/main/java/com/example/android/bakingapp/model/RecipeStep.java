package com.example.android.bakingapp.model;

import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Model for storing steps for a Recipe object.
 */
@Parcel
public class RecipeStep {

    private String id;
    private String shortDescription;
    private String videoURL;
    private String thumbnailURL;

    public RecipeStep() {}

    public RecipeStep(JSONObject jsonObject) {
        this.id = jsonObject.optString("id");
        this.shortDescription = jsonObject.optString("key");
        this.videoURL = jsonObject.optString("name");
        this.thumbnailURL = jsonObject.optString("thumbnailURL");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    /**
     * Convenience class for returning list steps for the recipe.
     */
    public static class StepsResult {

        private ArrayList<RecipeStep> results;

        public StepsResult() {
            results = new ArrayList<>();
        }

        public ArrayList<RecipeStep> getResults() {
            return results;
        }
    }
}
