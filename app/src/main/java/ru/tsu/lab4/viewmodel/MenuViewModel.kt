package ru.tsu.lab4.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.tsu.lab4.model.PlayerModel

class MenuViewModel(app: Application): AndroidViewModel(app) {

    private val playerModel = PlayerModel(getApplication())
    var currentPlayer: MutableLiveData<String> = MutableLiveData()

    init {
        setCurrentPlayer()
    }

    private fun setCurrentPlayer() {
        currentPlayer.value = playerModel.getPlayerName()
    }
}