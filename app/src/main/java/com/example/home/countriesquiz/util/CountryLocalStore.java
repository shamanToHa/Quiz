package com.example.home.countriesquiz.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.home.countriesquiz.model.Country;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class CountryLocalStore {

    public static final String COUNTRY_STORAGE_NAME = "Countries";
    private static SharedPreferences countryLocalDatabase;
    private static Type itemsListType = new TypeToken<List<Country>>() {
    }.getType();

    public static List<Country> loadCountries(Context context) {
        countryLocalDatabase = context.getSharedPreferences(COUNTRY_STORAGE_NAME, Context.MODE_PRIVATE);
        return new Gson().fromJson(countryLocalDatabase.getString(COUNTRY_STORAGE_NAME, ""), itemsListType);
    }

    public static boolean isCountriesStored(Context context) {
        return countryLocalDatabase.contains(COUNTRY_STORAGE_NAME);
    }

    public static void storeCountries(List<Country> countries, Context context) {
        countryLocalDatabase = context.getSharedPreferences(COUNTRY_STORAGE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = countryLocalDatabase.edit()
                .putString(COUNTRY_STORAGE_NAME, new Gson().toJson(countries));
        editor.apply();
    }

}
