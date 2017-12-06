package eu.huras.marcin.flagiistolice;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.widget.RemoteViews;

import java.util.Random;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {


    private static final String ACTION_NEXT = "ACTION_BROADCASTWIDGETSAMPLE 1";
    private static final String ACTION_PREV = "ACTION_BROADCASTWIDGETSAMPLE 2";
    private static final String ACTION_RAND = "ACTION_BROADCASTWIDGETSAMPLE 3";

    public static int licznik;


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Random rand = new Random();

        String[] txt_nazwa_panstwa = context.getResources().getStringArray(R.array.panstwa_nazwa);
        String[] txt_nazwa_stolicy = context.getResources().getStringArray(R.array.stolice_nazwa);
        TypedArray ar = context.getResources().obtainTypedArray(R.array.flagi_img_res);
        int len = ar.length();
        licznik = rand.nextInt(len-1);

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);

        views.setTextViewText(R.id.appwidget_text, txt_nazwa_panstwa[licznik] + ": " + txt_nazwa_stolicy[licznik]);
      views.setImageViewResource(R.id.appwidget_image_flag, ar.getResourceId(licznik, 0));

       // views.setImageViewResource(R.id.appwidget_image_flag, R.drawable.japonia);
        ustawIntenty(context, views);

        // Instruct the widget manager to update the widgetz
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private static void ustawIntenty(Context context, RemoteViews views) {
        //intent btn next
        Intent intent = new Intent(context, NewAppWidget.class);
        intent.setAction(ACTION_NEXT);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent,
        PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.btn_next, pendingIntent);

        //intent btn prev
        Intent intentPrev = new Intent(context, NewAppWidget.class);
        intentPrev.setAction(ACTION_PREV);
        PendingIntent pendingIntentPrev = PendingIntent.getBroadcast(context, 0, intentPrev,
        PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.btn_prev, pendingIntentPrev);

        //intent btn random
        Intent intentRand = new Intent(context, NewAppWidget.class);
        intentRand.setAction(ACTION_RAND);
        PendingIntent pendingIntentRand = PendingIntent.getBroadcast(context, 0, intentRand,
        PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.btn_rand, pendingIntentRand);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }



    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created

    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
//        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
//        onUpdate(context, appWidgetManager, new int[] {appWidgetId});
        // Toast.makeText(context, "in enables", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);


        if (ACTION_NEXT.equals(intent.getAction())) {
            String[] txt_nazwa_panstwa = context.getResources().getStringArray(R.array.panstwa_nazwa);
            String[] txt_nazwa_stolicy = context.getResources().getStringArray(R.array.stolice_nazwa);
            TypedArray ar = context.getResources().obtainTypedArray(R.array.flagi_img_res);

            if (licznik >= 203) {
                licznik = 0;
            } else {
                licznik++;
            }

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
            views.setTextViewText(R.id.appwidget_text, txt_nazwa_panstwa[licznik] + ": " + txt_nazwa_stolicy[licznik]);

           views.setImageViewResource(R.id.appwidget_image_flag, ar.getResourceId(licznik, 0));


            ComponentName appWidget = new ComponentName(context, NewAppWidget.class);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

            ustawIntenty(context, views);
            appWidgetManager.updateAppWidget(appWidget, views);

        } else if (ACTION_PREV.equals(intent.getAction())) {
            String[] txt_nazwa_panstwa = context.getResources().getStringArray(R.array.panstwa_nazwa);
            String[] txt_nazwa_stolicy = context.getResources().getStringArray(R.array.stolice_nazwa);
            TypedArray ar = context.getResources().obtainTypedArray(R.array.flagi_img_res);

            if (licznik <= 0) {
                licznik = 203;
            } else {
                licznik--;
            }

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
            views.setTextViewText(R.id.appwidget_text, txt_nazwa_panstwa[licznik] + ": " + txt_nazwa_stolicy[licznik]);

            views.setImageViewResource(R.id.appwidget_image_flag, ar.getResourceId(licznik, 0));

            ComponentName appWidget = new ComponentName(context, NewAppWidget.class);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ustawIntenty(context, views);
            appWidgetManager.updateAppWidget(appWidget, views);

        }  else if (ACTION_RAND.equals(intent.getAction())) {
            String[] txt_nazwa_panstwa = context.getResources().getStringArray(R.array.panstwa_nazwa);
            String[] txt_nazwa_stolicy = context.getResources().getStringArray(R.array.stolice_nazwa);
            TypedArray ar = context.getResources().obtainTypedArray(R.array.flagi_img_res);

            Random rand = new Random();
            int len = ar.length();
            licznik = rand.nextInt(len-1);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
            views.setTextViewText(R.id.appwidget_text, txt_nazwa_panstwa[licznik] + ": " + txt_nazwa_stolicy[licznik]);

            views.setImageViewResource(R.id.appwidget_image_flag, ar.getResourceId(licznik, 0));

            ComponentName appWidget = new ComponentName(context, NewAppWidget.class);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ustawIntenty(context, views);
            appWidgetManager.updateAppWidget(appWidget, views);

        }
    }
}

