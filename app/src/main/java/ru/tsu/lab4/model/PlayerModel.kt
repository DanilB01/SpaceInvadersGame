package ru.tsu.lab4.model

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.*
import ru.tsu.lab4.SharedPreference
import ru.tsu.lab4.game.CurrentShip
import ru.tsu.lab4.model.db.AppDatabase
import ru.tsu.lab4.model.db.PlayerWithScore
import ru.tsu.lab4.view.MainActivity

class PlayerModel(app: Context) {

    private var db = AppDatabase.getDatabase(app)
    private var sharedPref = SharedPreference(app)

    suspend fun getAllPlayersScore(): Array<PlayerWithScore>{
        val res = withContext(Dispatchers.IO) {
            db.playerWithScoreDao().getAll()
        }
        res.sortDescending()
        return res
    }

    fun getPlayerName(): String {
        var res = sharedPref.getString("playerName")
        if (res == "") {
            res = "Anonymous"
            sharedPref.saveString("playerName", res)
            GlobalScope.launch {
                db.playerWithScoreDao().addPlayer(PlayerWithScore(res, 0, 0, 0, -1))
            }
        }
        return res!!
    }

    fun setNewPlayerName(newName: String?): Boolean {
        return if (newName != null){
            val oldName = sharedPref.getString("playerName")
            if(newName != "" && newName != oldName) {
                sharedPref.saveString("playerName", newName)
                GlobalScope.launch {
                    db.playerWithScoreDao().addPlayer(PlayerWithScore(newName, 0, 0, 0, -1))
                }
                true
            } else {
                false
            }
        } else {
            false
        }
    }

    suspend fun getCoins():Int {
        val res = withContext(Dispatchers.IO) {
            db.playerWithScoreDao().getPlayerCoins(sharedPref.getString("playerName")!!)
        }
        return res
    }

    fun setCoins(price: Int) {
        GlobalScope.launch {
            val coins = withContext(Dispatchers.IO) {
                db.playerWithScoreDao().getPlayerCoins(sharedPref.getString("playerName")!!)
            }
            val newCoins = coins - price
            db.playerWithScoreDao().updateCoins(newCoins, sharedPref.getString("playerName")!!)
        }
    }

    fun setCurrentShips(shipNum: Int){
        GlobalScope.launch(Dispatchers.IO) {
            db.playerWithScoreDao().setCurSpaceship(sharedPref.getString("playerName")!!, shipNum)
        }
        CurrentShip.curShipNumber = shipNum
    }

    fun getShipPrice(shipNum: Int): Int {
        return when(shipNum){
            1 -> 100
            2 -> 150
            3 -> 200
            4 -> 250
            5 -> 300
            else -> -1
        }
    }
}