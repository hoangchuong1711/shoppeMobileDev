package com.example.loginpage.ui.profile;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class ProfileStore {
    private ProfileStore() {}

    private static SharedPreferences preferences(Context context) {
        String username = context.getSharedPreferences("user_data", Context.MODE_PRIVATE)
                .getString("username", "Khách");
        return context.getSharedPreferences("profile_" + username, Context.MODE_PRIVATE);
    }

    public static String get(Context context, String key) {
        return preferences(context).getString(key, "");
    }

    public static void save(Context context, String name, String phone, String address) {
        preferences(context).edit()
                .putString("name", name)
                .putString("phone", phone)
                .putString("address", address)
                .apply();
    }

    public static JSONArray orders(Context context) {
        String saved = preferences(context).getString("orders", "[]");
        try {
            return new JSONArray(saved);
        } catch (JSONException exception) {
            return new JSONArray();
        }
    }

    public static void addOrder(Context context, JSONObject order) {
        JSONArray previous = orders(context);
        JSONArray updated = new JSONArray();
        updated.put(order);
        for (int i = 0; i < previous.length(); i++) {
            updated.put(previous.opt(i));
        }
        preferences(context).edit().putString("orders", updated.toString()).apply();
    }
}
