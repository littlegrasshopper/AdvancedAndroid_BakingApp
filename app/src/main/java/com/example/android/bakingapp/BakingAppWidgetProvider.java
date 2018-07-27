package com.example.android.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.example.android.bakingapp.ui.RecipeDetailActivity;
import com.example.android.bakingapp.ui.RecipeMainActivity;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                long recipeId, String recipeTitle, String ingredients, int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.app_name);

        // Create an Intent to launch Detail Activity if a valid recipe ID is passed
        // Otherwise launch the MainActivity
        Intent intent;
        if (recipeId != BakingAppWidgetService.INVALID_RECIPE_ID) {
            intent = new Intent(context, RecipeDetailActivity.class);
            intent.putExtra(BakingAppWidgetService.EXTRA_RECIPE_ID, recipeId);
        } else {
            intent = new Intent(context, RecipeMainActivity.class);
        }
        // Create a PendingIntent
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget_provider);
        views.setTextViewText(R.id.widget_app_text, widgetText);
        views.setTextViewText(R.id.widget_recipe_title, recipeTitle);
        if (TextUtils.isEmpty(ingredients)) {
            views.setViewVisibility(R.id.widget_ingredients_list, View.GONE);
        } else {
            views.setTextViewText(R.id.widget_ingredients_list, ingredients);
        }

        // Widgets allow click handlers to only launch pending intents
        views.setOnClickPendingIntent(R.id.baking_app_widget_provider_layout, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    /**
     * Custom update method for the widget
     */
    public static void updateAppWidgets(Context context, AppWidgetManager appWidgetManager,
                                        long recipeId, String recipeTitle, String ingredients,
                                        int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, recipeId, recipeTitle, ingredients, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

