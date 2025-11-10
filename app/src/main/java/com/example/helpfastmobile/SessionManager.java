package com.example.helpfastmobile;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREF_NAME = "HelpFastSession";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_CARGO_ID = "cargo_id";
    private static final String KEY_USER_NAME = "user_name"; // NOVO

    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    // --- User ID ---
    public void saveUserId(int userId) {
        editor.putInt(KEY_USER_ID, userId);
        editor.apply();
    }

    public int getUserId() {
        return sharedPreferences.getInt(KEY_USER_ID, -1);
    }

    // --- Cargo ID ---
    public void saveCargoId(int cargoId) {
        editor.putInt(KEY_CARGO_ID, cargoId);
        editor.apply();
    }

    public int getCargoId() {
        return sharedPreferences.getInt(KEY_CARGO_ID, -1);
    }

    // --- User Name ---
    public void saveUserName(String name) {
        editor.putString(KEY_USER_NAME, name);
        editor.apply();
    }

    public String getUserName() {
        return sharedPreferences.getString(KEY_USER_NAME, null);
    }

    // --- Limpar Sessão ---
    public void clearSession() {
        editor.remove(KEY_USER_ID);
        editor.remove(KEY_CARGO_ID);
        editor.remove(KEY_USER_NAME);
        editor.apply();
    }
}
