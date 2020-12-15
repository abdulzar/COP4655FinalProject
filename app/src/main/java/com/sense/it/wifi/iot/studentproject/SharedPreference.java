package com.sense.it.wifi.iot.studentproject;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;
import com.sense.it.wifi.iot.studentproject.model.ResturantModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SharedPreference {

    public static final String PREFS_NAME = "PRODUCT_APP";
    public static final String FAVORITES = "Product_Favorite";

    public SharedPreference() {
        super();
    }

    // This four methods are used for maintaining favorites.
    public void saveFavorites(Context context, List<ResturantModel> favorites) {
        SharedPreferences settings;
        Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites);

        editor.putString(FAVORITES, jsonFavorites);

        editor.commit();
    }

    public void addFavorite(Context context, ResturantModel product) {
        List<ResturantModel> favorites = getFavorites(context);
        if (favorites == null)
            favorites = new ArrayList<ResturantModel>();
        favorites.add(product);
        saveFavorites(context, favorites);
    }

    public void removeFavorite(Context context, ResturantModel product) {
        ArrayList<ResturantModel> favorites = getFavorites(context);
        if (favorites != null) {
            favorites.remove(product);
            saveFavorites(context, favorites);
        }
    }

    public ArrayList<ResturantModel> getFavorites(Context context) {
        SharedPreferences settings;
        List<ResturantModel> favorites;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        if (settings.contains(FAVORITES)) {
            String jsonFavorites = settings.getString(FAVORITES, null);
            Gson gson = new Gson();
            ResturantModel[] favoriteItems = gson.fromJson(jsonFavorites,
                    ResturantModel[].class);

            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList<ResturantModel>(favorites);
        } else
            return null;

        return (ArrayList<ResturantModel>) favorites;
    }
}
