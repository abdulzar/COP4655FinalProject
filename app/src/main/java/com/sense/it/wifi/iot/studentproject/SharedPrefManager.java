package com.sense.it.wifi.iot.studentproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.sense.it.wifi.iot.studentproject.model.ResturantModel;
import com.sense.it.wifi.iot.studentproject.user.LogInActivity;
import com.sense.it.wifi.iot.studentproject.user.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SharedPrefManager {

    public static final String FAVORITES = "Product_Favorite";
    //the constants
    private static final String SHARED_PREF_NAME = "StudentApp";
    private static final String SHARED_SAVE = "save";
    private static final String KEY_USERNAME = "keyusername";
    private static final String KEY_EMAIL = "keyemail";
    private static final String KEY_PHONE = "keyphone";
    private static final String KEY_GENDER = "keygender";
    private static final String KEY_PASS = "keypassword";
    private static final String KEY_IMAGE_URL = "imageurl";
    private static final String IS_FIRST_TIME = "IS_FIRST_TIME";
    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    //method to let the user login
    //this method will store the user data in shared preferences
    public void userLogin(User user) {
        Common.currentUser = user;
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_PASS, user.getPassword());
        editor.putString(KEY_USERNAME, user.getUsername());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_GENDER, user.getGender());
        editor.putString(KEY_PHONE, user.getPhone());
        editor.apply();
    }

    //this method will checker whether user is already logged in or not
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USERNAME, null) != null;
    }

    //this method will give the logged in user
    public User getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                //sharedPreferences.getString(KEY_ID, ""),
                sharedPreferences.getString(KEY_USERNAME, ""),
                sharedPreferences.getString(KEY_EMAIL, ""),
                sharedPreferences.getString(KEY_GENDER, ""),
                sharedPreferences.getString(KEY_PHONE, ""),
                sharedPreferences.getString(KEY_PASS, "")
        );
    }

    //this method will logout the user
    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        mCtx.startActivity(new Intent(mCtx, LogInActivity.class));

    }

    public boolean getIsFirstTime() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_SAVE, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(IS_FIRST_TIME, true);
    }

    public void setIsFirstTime(Boolean value) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_SAVE, Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(IS_FIRST_TIME, value).apply();
    }

    public String getPhoneNo() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_SAVE, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_PHONE, "");
    }

    public void setPhoneNo(String phone) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_SAVE, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(KEY_PHONE, phone).apply();
    }

    // This four methods are used for maintaining favorites.
    public void saveFavorites(List<ResturantModel> favorites) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = mCtx.getSharedPreferences(SHARED_PREF_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites);

        editor.putString(FAVORITES, jsonFavorites);

        editor.commit();
    }

    public void addFavorite(ResturantModel product) {
        List<ResturantModel> favorites = getFavorites();
        if (favorites == null)
            favorites = new ArrayList<>();
        favorites.add(product);
        saveFavorites(favorites);
    }

    public void removeFavorite(ResturantModel product) {
        List<ResturantModel> favorites = getFavorites();
        if (favorites != null) {
            favorites.remove(product);
            saveFavorites(favorites);
        }
    }

    public List<ResturantModel> getFavorites() {
        SharedPreferences settings;
        List<ResturantModel> favorites;

        settings = mCtx.getSharedPreferences(SHARED_PREF_NAME,
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

        return favorites;
    }

}
