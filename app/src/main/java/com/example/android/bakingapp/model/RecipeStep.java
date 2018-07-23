package com.example.android.bakingapp.model;

import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Model for storing steps for a Recipe object.
 */
@Parcel
public class RecipeStep {
    public static final String VIDEO_PATH = "https://d17h27t6h515a5.cloudfront.net/topher/2017/%s";

    public static final String TRAILER_BASE_URL = "https://www.youtube.com/watch?v=";
    public static final String TRAILER_IMAGE_BASE_URL = "https://img.youtube.com/vi/";
    public static final String TRAILER_IMAGE_DEFAULT = "/mqdefault.jpg";

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
