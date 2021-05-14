package ru.tsu.lab4.model

import android.content.Context
import ru.tsu.lab4.SharedPreference
import ru.tsu.lab4.model.db.AppDatabase

class SettingsModel(app: Context) {
    private var db = AppDatabase.getDatabase(app)
    private var sharedPref = SharedPreference(app)

    fun saveSettings(param1: Boolean,
                     param2: Boolean,
                     param3: Boolean,
                     param4: Boolean) {
        if(param2) {
            sharedPref.saveBool("keyMode", true)
        }
        else {
            sharedPref.saveBool("keyMode", false)
        }
        sharedPref.saveBool("keyMusic", param3)
        sharedPref.saveBool("keyNotification", param4)
    }

    fun getControlSetting() = sharedPref.getBool("keyMode")

    fun getMusicSetting() = sharedPref.getBool("keyMusic")

    fun getNotificationSetting() = sharedPref.getBool("keyNotification")
}