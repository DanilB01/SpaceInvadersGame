package ru.tsu.lab4.viewmodel

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import kotlinx.android.synthetic.main.activity_settings.*
import ru.tsu.lab4.NotificationService
import ru.tsu.lab4.model.PlayerModel
import ru.tsu.lab4.model.SettingsModel

class SettingViewModel(private val app: Application): AndroidViewModel(app) {
    private val settingModel = SettingsModel(getApplication())

    var isRotationControl: MutableLiveData<Boolean> = MutableLiveData()
    var isTapControl: MutableLiveData<Boolean> = MutableLiveData()
    var isMusic: MutableLiveData<Boolean> = MutableLiveData()
    var isNotifications: MutableLiveData<Boolean> = MutableLiveData()

    var isFinish: MutableLiveData<Boolean> = MutableLiveData()

    init {
        getSettings()
        clearFinishFlag()
    }

    fun getSettings() {
        val control = settingModel.getControlSetting()
        if(control) {
            isTapControl.value = true
        } else {
            isRotationControl.value = true
        }
        isMusic.value = settingModel.getMusicSetting()
        isNotifications.value = settingModel.getNotificationSetting()
    }

    fun clearFinishFlag() { isFinish.value = false }
    fun setFinishFlag() { isFinish.value = true }

    fun saveSettings() {
        settingModel.saveSettings(isRotationControl.value!!,
                isTapControl.value!!,
                isMusic.value!!,
                isNotifications.value!!)

        if(isNotifications.value!!) {
            app.startService(Intent(app, NotificationService::class.java))
        }
        else {
            app.stopService(Intent(app, NotificationService::class.java))
        }
        setFinishFlag()
    }
}