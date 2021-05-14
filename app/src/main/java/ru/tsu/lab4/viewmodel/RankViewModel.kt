package ru.tsu.lab4.viewmodel

import android.app.Activity
import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.tsu.lab4.model.PlayerModel
import ru.tsu.lab4.model.db.PlayerWithScore


class RankViewModel(app: Application): AndroidViewModel(app) {


    private val playerModel = PlayerModel(getApplication())

    var scoresList : MutableLiveData<Array<PlayerWithScore>> = MutableLiveData()

    init {
        updatePlayersScore()
    }


    fun getPlayersScore() = scoresList

    private fun updatePlayersScore() {
        GlobalScope.launch(Dispatchers.Main) {
            val scores = playerModel.getAllPlayersScore()
            scoresList.value = scores
        }
    }



}