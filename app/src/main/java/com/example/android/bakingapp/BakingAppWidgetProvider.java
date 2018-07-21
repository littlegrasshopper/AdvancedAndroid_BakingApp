package com.example.android.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                long recipeId, String recipeTitle, String ingredients, int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);

        // == START ==
        // TODO Replace with correct fragment/activity
        // Create an Intent to launch Detail Activity if a valid recipe ID is passed
        // Otherwise launch the MainActivity
        Intent intent;
        if (recipeId != BakingAppWidgetService.INVALID_RECIPE_ID) {
            intent = new Intent(context, QuizActivity.class);
            intent.putExtra(BakingAppWidgetService.EXTRA_RECIPE_ID, recipeId);
        } else {
            intent = new Intent(context, MainActivity.class);
        }
        // Create a PendingIntent
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        // == END ==

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget_provider);
        views.setTextViewText(R.id.appwidget_text, widgetText);

        // == START==
        views.setTextViewText(R.id.widget_recipe_title, recipeTitle);
        views.setTextViewText(R.id.widget_ingredients_list, ingredients);
        // == END ==

        // Widgets allow click handlers to only launch pending intents
        views.setOnClickPendingIntent(R.id.widget_berry_cupcake, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    /*
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, recipeId, recipeTitle, ingredients, appWidgetId);
        }
        //BakingAppWidgetService.startActionUpdateBakingWidget(context);
    }*/

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

