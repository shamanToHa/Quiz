package com.example.home.countriesquiz.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.home.countriesquiz.model.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class UserLocalStore {
    public static final String USER_STORAGE_NAME = "userDetails";
    private static SharedPreferences userLocalDatabase;
    private static List<User> userList;
    private static Type itemsListType = new TypeToken<List<User>>() {
    }.getType();


    public static void storeUser(User user, Context context) {
        getUserList(context);
        User hashUser = new User(user.getName(), getMd5Hash(user.getPassword()));
        userList.add(hashUser);
        userLocalDatabase = context.getSharedPreferences(USER_STORAGE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userLocalDatabase.edit()
                .putString(USER_STORAGE_NAME, new Gson().toJson(userList));
        editor.apply();
    }

    public static List<User> getUserList(Context context) {
        userLocalDatabase = context.getSharedPreferences(USER_STORAGE_NAME, Context.MODE_PRIVATE);
        if (userLocalDatabase.contains(USER_STORAGE_NAME)) {
            userList = new Gson().fromJson(userLocalDatabase.getString(USER_STORAGE_NAME, ""), itemsListType);
        }
        if (userList == null) {
            userList = new ArrayList<>();
        }
        return userList;
    }

    public static String getMd5Hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String md5 = number.toString(16);

            while (md5.length() < 32)
                md5 = "0" + md5;

            return md5;
        } catch (NoSuchAlgorithmException e) {
            Log.e("MD5", e.getLocalizedMessage());
            return null;
        }
    }
}
