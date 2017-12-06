package eu.huras.marcin.flagiistolice;

/**
 * Created by Marcin on 2017-11-05.
 */


import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;
import android.widget.Toast;

import java.util.Locale;

public class LocaleManager {

    public static final  String LANGUAGE_ENGLISH   = "en";
    public static final  String LANGUAGE_POLISH    = "pl";
    public static final  String LANGUAGE_GERMANY    = "de";
    private static final String LANGUAGE_KEY       = "language_key";


    public static Context setLocale(Context c) {
       // return updateResources(c);
        return updateResources(c, getLanguage(c));
    }

    public static Context setNewLocale(Context c, String language) {
        saveLanguage(c, language);
        return updateResources(c, language);
    }

    public static String getLanguage(Context c) {

        String temp = Resources.getSystem().getConfiguration().locale.toString().substring(0,2);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);

        if (temp.equals("pl")) {

            return prefs.getString(LANGUAGE_KEY, LANGUAGE_POLISH);

        } else if (temp.equals("en")) {

            return prefs.getString(LANGUAGE_KEY, LANGUAGE_ENGLISH);

        } else if (temp.equals("de")) {

            return prefs.getString(LANGUAGE_KEY, LANGUAGE_GERMANY);
        } else {

            return prefs.getString(LANGUAGE_KEY, LANGUAGE_ENGLISH);

        }


    }


    private static void saveLanguage(Context c, String language) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
        prefs.edit().putString(LANGUAGE_KEY, language).apply();
    }

    private static Context updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        config.setLocale(locale);
        context = context.createConfigurationContext(config);
        return context;
    }
}