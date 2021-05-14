package ru.tsu.lab4

import android.content.Context
import android.content.SharedPreferences

class SharedPreference(val context: Context) {
    private val PREFS_NAME = "SpaceInvadersGame"
    val sharedPref: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveInt(KEY_NAME: String, value: Int) {
        sharedPref
            .edit()
            .putInt(KEY_NAME, value)
            .apply()
    }
    fun getInt(KEY_NAME: String): Int {

        return sharedPref.getInt(KEY_NAME, -1)
    }

    fun saveBool(KEY_NAME: String, value: Boolean) {
        sharedPref
            .edit()
            .putBoolean(KEY_NAME, value)
            .apply()
    }

    fun getBool(KEY_NAME: String): Boolean {
        return sharedPref.getBoolean(KEY_NAME, false)
    }

    fun saveString(KEY_NAME: String, value: String) {
        sharedPref
            .edit()
            .putString(KEY_NAME, value)
            .apply()
    }
    fun getString(KEY_NAME: String): String? {

        return sharedPref.getString(KEY_NAME, "")
    }

    fun clearSharedPreference() {
        sharedPref
            .edit()
            .clear()
            .apply()
    }

    fun removeValue(KEY_NAME: String) {
        sharedPref
            .edit()
            .remove(KEY_NAME)
            .apply()
    }
}
