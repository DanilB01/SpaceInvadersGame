package ru.tsu.lab4.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.tsu.lab4.model.PlayerModel

class RenameViewModel(private val app: Application): AndroidViewModel(app) {

    private val playerModel = PlayerModel(getApplication())

    var currentPlayer: MutableLiveData<String> = MutableLiveData()
    var isFinish: MutableLiveData<Boolean> = MutableLiveData()

    init {
        setCurrentPlayer()
        clearFinishFlag()
    }

    fun clearFinishFlag() { isFinish.value = false }
    fun setFinishFlag() { isFinish.value = true }

    private fun setCurrentPlayer() {
        currentPlayer.value = playerModel.getPlayerName()
    }

    fun changePlayerName() {
        val res = playerModel.setNewPlayerName(currentPlayer.value)
        when(res) {
            true -> setFinishFlag()
            false -> Toast.makeText(app, "Пожалуйста, введите ваше имя!", Toast.LENGTH_SHORT).show()
        }
    }

}