package com.example.radhika.demoapp;



        import android.content.Context;
        import android.content.SharedPreferences;
        import android.preference.PreferenceManager;
        import android.widget.Toast;

        import java.util.ArrayList;
        import java.util.Arrays;
        import java.util.Collection;
        import java.util.Collections;
        import java.util.List;

/**
 * Created by dell on 03/05/2016.
 */
public class PreferencesManager {

    public static List<String> getFav(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String fav = preferences.getString("fav", "");
        return new ArrayList<String>(Arrays.asList(fav.split(",")));
    }

    public static void addFav(Context context, String f) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String fav = preferences.getString("fav", "");
        if (fav.isEmpty())
            preferences.edit().putString("fav", f).apply();
        else
            preferences.edit().putString("fav", String.format("%s,%s", fav, f)).apply();
    }

    public static boolean isFav(Context context, String q) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String fav = preferences.getString("fav", "");
        return getFav(context).contains(q);
    }

    public static void removeFav(Context context, String q) {
        List<String> fav = getFav(context);
        if (!fav.contains(q)) return;
        fav.remove(q);

        StringBuilder builder = new StringBuilder();
        for (String f : fav)
            builder.append(f).append(",");

        if (builder.length()!=0)
            builder.deleteCharAt(builder.length() - 1);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putString("fav", builder.toString()).apply();

    }

    public static void toggleFav(Context context, String fav) {
        if (isFav(context, fav)) {
            removeFav(context, fav);

        }
        else {
            addFav(context, fav);

        }
    }
}