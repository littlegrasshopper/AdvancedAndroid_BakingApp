package com.example.android.bakingapp;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.bakingapp.model.Recipe;

import org.parceler.Parcels;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */

public class BakingAppWidgetService extends IntentService {

    public static final String ACTION_UPDATE_BAKING_WIDGET = "com.example.android.bakingapp.action.update_baking_widget";
    public static final String EXTRA_RECIPE = "com.example.android.bakingapp.extra.recipe";
    public static final String EXTRA_RECIPE_TITLE = "com.example.android.bakingapp.extra.recipeTitle";
    public static final String EXTRA_INGREDIENTS = "com.example.android.bakingapp.extra.ingredients";

    public BakingAppWidgetService() {
        super("BakingAppWidgetService");
    }

    public static void startActionUpdateBakingWidget(Context context, Recipe recipe, String recipeTitle, String ingredients) {
        Intent intent = new Intent(context, BakingAppWidgetService.class);
        intent.putExtra(EXTRA_RECIPE, Parcels.wrap(recipe));
        intent.putExtra(EXTRA_RECIPE_TITLE, recipeTitle);
        intent.putExtra(EXTRA_INGREDIENTS, ingredients);
        intent.setAction(ACTION_UPDATE_BAKING_WIDGET);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE_BAKING_WIDGET.equals(action)) {
                final Recipe recipe = Parcels.unwrap(intent.getParcelableExtra(EXTRA_RECIPE));
                final String recipeTitle = intent.getStringExtra(EXTRA_RECIPE_TITLE);
                final String ingredients = intent.getStringExtra(EXTRA_INGREDIENTS);
                handleActionUpdateBakingWidget(recipe, recipeTitle, ingredients);
            }
        }
    }

    private void handleActionUpdateBakingWidget(Recipe recipe, String recipeTitle, String ingredients) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                new ComponentName(this, BakingAppWidgetProvider.class)
        );
        BakingAppWidgetProvider.updateAppWidgets(this, appWidgetManager, recipe, recipeTitle,
                ingredients, appWidgetIds);
    }
}
